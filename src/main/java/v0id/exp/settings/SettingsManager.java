package v0id.exp.settings;

import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import v0id.api.core.logging.LogLevel;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.settings.ISettingsManager;
import v0id.exp.settings.impl.SettingsClient;
import v0id.exp.settings.impl.WritableSettings;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by V0idWa1k3r on 16-Jun-17.
 */
public class SettingsManager implements ISettingsManager
{
    public final Map<String, ISettingsManager> children = Maps.newHashMap();

    public final SettingsClient settingsClient;

    public SettingsManager(File cfgFolderRoot)
    {
        if (!cfgFolderRoot.exists())
        {
            cfgFolderRoot.mkdirs();
        }

        this.settingsClient = this.getOrCreateChild(SettingsClient.class, new File(cfgFolderRoot, "ClientSettings.json"), false);
        this.children.put("client", this.settingsClient);
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
