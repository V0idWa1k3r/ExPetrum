package v0id.core.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPacketHandler 
{
	default void handleData(NBTTagCompound data, EntityPlayer player)
    {
        handleData(data);
    }

	void handleData(NBTTagCompound data);
	
	NBTTagCompound handleRequest(String requester);
}
