package v0id.exp.settings;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.commons.io.IOUtils;
import v0id.core.logging.LogLevel;
import v0id.core.network.VoidNetwork;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.data.ExPPackets;
import v0id.api.exp.settings.ISettingsManager;
import v0id.api.exp.settings.SyncableSettings;
import v0id.exp.settings.impl.SettingsClient;
import v0id.exp.settings.impl.SettingsFlags;
import v0id.exp.settings.impl.WritableSettings;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by V0idWa1k3r on 16-Jun-17.
 */
public class SettingsManager implements ISettingsManager
{
    public final Map<String, ISettingsManager> children = Maps.newHashMap();
    public final SettingsClient settingsClient;
    public final SettingsFlags settingsFlags;
    public final Map<String, ISettingsManager> savedClientSettingsState = Maps.newHashMap();
    public static SettingsManager instance;

    public SettingsManager(File cfgFolderRoot)
    {
        instance = this;
        if (!cfgFolderRoot.exists())
        {
            cfgFolderRoot.mkdirs();
        }

        this.settingsClient = this.getOrCreateChild(SettingsClient.class, new File(cfgFolderRoot, "ClientSettings.json"), false);
        this.children.put("client", this.settingsClient);
        this.settingsFlags = this.getOrCreateChild(SettingsFlags.class, new File(cfgFolderRoot, "FeaturesSettings.json"), false);
        this.children.put("flags", this.settingsFlags);
    }

    public static void sendSettingsToClient(EntityPlayerMP client)
    {
        final NBTTagList settingsList = new NBTTagList();
        instance.children.values().stream().filter(m -> m.getClass().isAnnotationPresent(SyncableSettings.class)).forEach(m ->
        {
            NBTTagCompound appended = new NBTTagCompound();
            appended.setString("id", m.getClass().getAnnotation(SyncableSettings.class).value());
            appended.setString("json", WritableSettings.serializer.toJson(m));
            settingsList.appendTag(appended);
        });

        NBTTagCompound sent = new NBTTagCompound();
        sent.setTag("settings", settingsList);
        VoidNetwork.sendDataToClient(ExPPackets.SETTINGS_SYNC, sent, client);
    }

    public static void restoreAllClientData()
    {
        instance.savedClientSettingsState.forEach((String id, ISettingsManager settings) -> {
            if (!instance.children.containsKey(id))
            {
                return;
            }

            try
            {
                ISettingsManager current = instance.children.get(id);
                Field instanceField = current.getClass().getDeclaredField("instance");
                if (Modifier.isPublic(instanceField.getModifiers()) && Modifier.isStatic(instanceField.getModifiers()))
                {
                    instanceField.set(null, settings);
                    instance.children.put(id, settings);
                }
                else
                {
                    throw new IllegalStateException("Child has an instance field but it is not public and static!");
                }
            }
            catch (Exception ex)
            {
                ExPMisc.modLogger.log(LogLevel.Error, "Could not receive server settings for %s!", ex, id);
            }
        });

        instance.savedClientSettingsState.clear();
    }

    public static void acceptServerConfig(String id, String json)
    {
        if (!instance.children.containsKey(id))
        {
            return;
        }

        try
        {
            ISettingsManager current = instance.children.get(id);
            ISettingsManager newData = WritableSettings.serializer.fromJson(json, current.getClass());
            instance.savedClientSettingsState.put(id, current);
            Field instanceField = current.getClass().getDeclaredField("instance");
            if (Modifier.isPublic(instanceField.getModifiers()) && Modifier.isStatic(instanceField.getModifiers()))
            {
                instanceField.set(null, newData);
                instance.children.put(id, newData);
            }
            else
            {
                throw new IllegalStateException("Child has an instance field but it is not public and static!");
            }
        }
        catch (Exception ex)
        {
            ExPMisc.modLogger.log(LogLevel.Error, "Could not receive server settings for %s!", ex, id);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T get(String key)
    {
        return (T) children.getOrDefault(key, null);
    }

    @Override
    public <T> void set(String key, @Nullable T value)
    {
        throw new UnsupportedOperationException();
    }

    public <T extends ISettingsManager>T getOrCreateChild(Class<T> clazz, File f, boolean ignoreFile)
    {
        if (f.exists() && !ignoreFile)
        {
            try (FileInputStream fis = new FileInputStream(f))
            {
                String json = IOUtils.toString(fis, StandardCharsets.UTF_8);
                T child = WritableSettings.serializer.fromJson(json, clazz);
                if (child instanceof WritableSettings)
                {
                    ((WritableSettings) child).setFile(f);
                }

                return child;
            }
            catch (Exception e)
            {
                ExPMisc.modLogger.log(LogLevel.Warning, "Could not load settings of %s from %s!", e, clazz.toString(), f.toString());
                return this.getOrCreateChild(clazz, f, true);
            }
        }
        else
        {
            T child = null;
            try
            {
                child = clazz.newInstance();
                if (child instanceof WritableSettings)
                {
                    f.createNewFile();
                    ((WritableSettings) child).setFile(f);
                    ((WritableSettings) child).saveData();
                }
            }
            catch (Exception ex)
            {
                // Impossible. If this happens let the world burn and the game crash.
            }

            return child;
        }
    }
}
