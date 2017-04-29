package v0id.exp.net;

import net.minecraft.nbt.NBTTagCompound;
import v0id.api.core.network.IPacketHandler;

public class PacketHandlerWorldData implements IPacketHandler
{

	@Override
	public void handleData(NBTTagCompound data)
	{
		
	}

	@Override
	public NBTTagCompound handleRequest(String requester)
	{
		return null;
	}

}
