package v0id.exp.util;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.WorldEvent;
import v0id.core.logging.LogLevel;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.entity.IPackInfo;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * Created by V0idWa1k3r on 19-Jun-17.
 */
public class EntityPackManager
{
    public static final Map<UUID, IPackInfo> INFO_MAP = Maps.newHashMap();
    public static File saveDirectory;
    public static final ExecutorService pool = Executors.newFixedThreadPool(4);

    public static boolean isPackInfoLoaded(UUID id)
    {
        return INFO_MAP.containsKey(id);
    }

    public static boolean isPackInfoAvailable(UUID id)
    {
        return getPackSaveFile(id).exists();
    }

    public static File getPackSaveFile(UUID id)
    {
        assert saveDirectory != null : "Pack info file management can only be performed on the server!";
        return new File(saveDirectory, id.toString() + ".dat");
    }

    public static void cleanup()
    {
        INFO_MAP.clear();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void findOrCreateSaveDirectory(WorldEvent.Load event)
    {
        if (event.getWorld() != null && !event.getWorld().isRemote && event.getWorld().provider != null && event.getWorld().provider.getDimension() == 0)
        {
            saveDirectory = new File(event.getWorld().getSaveHandler().getWorldDirectory(), "ExPetrumExtraData");
            if (!saveDirectory.exists())
            {
                saveDirectory.mkdirs();
            }
        }
    }

    public static boolean deletePackInfo(UUID id)
    {
        INFO_MAP.remove(id);
        return getPackSaveFile(id).delete();
    }

    public static void savePackInfo(UUID id, boolean async)
    {
        assert isPackInfoLoaded(id) : "Pack info must be present in memory before saving!";
        final IPackInfo info = INFO_MAP.get(id);
        final File saveFile = getPackSaveFile(id);
        if (!saveFile.exists())
        {
            try
            {
                saveFile.createNewFile();
            }
            catch (Exception ex)
            {
                ExPMisc.modLogger.log(LogLevel.Error, "Could not save pack info %s - unable to create new file!", ex, id);
            }
        }

        if (async)
        {
            pool.execute(() -> savePackInfoInternal(info, saveFile));
        }
        else
        {
            savePackInfoInternal(info, saveFile);
        }
    }

    // Should only be done on-demand!
    public static void loadPackInfo(UUID id, Class<? extends IPackInfo> instanceClazz)
    {
        assert isPackInfoAvailable(id) : "Can't load non-existent pack data!";
        File saveFile = getPackSaveFile(id);
        try (FileInputStream fis = new FileInputStream(saveFile))
        {
            NBTTagCompound tag = CompressedStreamTools.readCompressed(fis);
            IPackInfo info = instanceClazz.newInstance();
            info.deserializeNBT(tag);
            INFO_MAP.put(id, info);
        }
        catch (Exception ex)
        {
            ExPMisc.modLogger.log(LogLevel.Error, "Could not load pack info %s!", ex, id.toString());
        }
    }

    public static IPackInfo getPackInfo(UUID id)
    {
        assert isPackInfoLoaded(id) : String.format("Pack info with ID %s is not loaded!", id.toString());
        return INFO_MAP.get(id);
    }

    public static IPackInfo getOrLoadPackInfo(UUID id, Class<? extends IPackInfo> infoClazz)
    {
        if (!isPackInfoLoaded(id))
        {
            loadPackInfo(id, infoClazz);
        }

        return getPackInfo(id);
    }

    public static void createPackInfo(@Nonnull IPackInfo info)
    {
        INFO_MAP.put(info.getID(), info);
        savePackInfo(info.getID(), true);
    }

    public static IPackInfo getLoadOrCreatePackInfo(UUID id, Class<? extends IPackInfo> infoClazz, Function<UUID, IPackInfo> provider)
    {
        if (isPackInfoLoaded(id))
        {
            return getPackInfo(id);
        }

        if (isPackInfoAvailable(id))
        {
            loadPackInfo(id, infoClazz);
            return getPackInfo(id);
        }

        createPackInfo(provider.apply(id));
        return getPackInfo(id);
    }

    public static void saveAllPacksInfo(boolean async)
    {
        INFO_MAP.forEach((UUID id, IPackInfo info) -> savePackInfo(id, async));
    }

    private static void savePackInfoInternal(IPackInfo info, File saveFile)
    {
        NBTTagCompound tag = info.serializeNBT();
        try (FileOutputStream fos = new FileOutputStream(saveFile))
        {
            CompressedStreamTools.writeCompressed(tag, fos);
        }
        catch (Exception ex)
        {
            ExPMisc.modLogger.log(LogLevel.Error, "Could not save pack info %s!", ex, info.getID().toString());
        }
    }
}
