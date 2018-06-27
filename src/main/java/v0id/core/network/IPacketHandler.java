package v0id.core.network;

import net.minecraft.nbt.NBTTagCompound;

public interface IPacketHandler 
{
	void handleData(NBTTagCompound data);
	
	NBTTagCompound handleRequest(String requester);
}
