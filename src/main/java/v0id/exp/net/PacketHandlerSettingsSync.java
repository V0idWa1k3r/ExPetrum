package v0id.exp.net;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import v0id.core.network.IPacketHandler;
import v0id.exp.settings.SettingsManager;

import java.util.stream.StreamSupport;

/**
 * Created by V0idWa1k3r on 19-Jun-17.
 */
public class PacketHandlerSettingsSync implements IPacketHandler
{
    @Override
    public void handleData(NBTTagCompound nbtTagCompound)
    {
        StreamSupport.stream(nbtTagCompound.getTagList("settings", Constants.NBT.TAG_COMPOUND).spliterator(), false).map(b -> (NBTTagCompound)b).forEach(tag ->
        {
            String id = tag.getString("id");
            String value = tag.getString("json");
            SettingsManager.acceptServerConfig(id, value);
        });
    }

    @Override
    public NBTTagCompound handleRequest(String s)
    {
        return null;
    }
}
