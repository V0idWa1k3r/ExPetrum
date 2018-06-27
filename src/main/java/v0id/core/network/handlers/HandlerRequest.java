package v0id.core.network.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import v0id.core.network.IPacketHandler;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.core.util.MC;
import v0id.core.util.Players;

public class HandlerRequest implements IPacketHandler {

	@Override
	public void handleData(NBTTagCompound data) 
	{
		PacketType type = PacketType.registry.get(data.getShort("requestID"));
		NBTTagCompound dataTag = VoidNetwork.handlers.get(data.getShort("requestID")).handleRequest(data.getString("requester"));
		if (dataTag == null)
		{
			return;
		}
		
		if (MC.getSide() == Side.SERVER)
		{
			EntityPlayerMP to = Players.getPlayerByUsername(data.getString("requester"));
			VoidNetwork.sendDataToClient(type, dataTag, to);
		}
		else
		{
			VoidNetwork.sendDataToServer(type, dataTag);
		}
	}

	@Override
	public NBTTagCompound handleRequest(String s) 
	{
		return null;
	}

}
