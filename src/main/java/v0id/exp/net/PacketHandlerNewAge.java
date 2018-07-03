package v0id.exp.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import v0id.api.exp.data.ExPPackets;
import v0id.api.exp.player.EnumPlayerProgression;
import v0id.api.exp.player.IExPPlayer;
import v0id.core.VoidApi;
import v0id.core.network.IPacketHandler;
import v0id.core.network.VoidNetwork;

public class PacketHandlerNewAge implements IPacketHandler
{

	@Override
	public void handleData(NBTTagCompound data)
	{
		EntityPlayer reciever = VoidApi.proxy.getClientPlayer();
		EnumPlayerProgression age = EnumPlayerProgression.values()[data.getByte("age")];
		if (reciever != null)
		{
            IExPPlayer.of(reciever).triggerStage(age);
		}
	}

	// If a client sent a request packet it wants the full data!
	@Override
	public NBTTagCompound handleRequest(String requester)
	{
		return null;
	}
	
	// Only syncs partial!
	public static void sendNewAge(EntityPlayerMP to, EnumPlayerProgression age)
	{
	    NBTTagCompound tag = new NBTTagCompound();
	    tag.setByte("age", (byte) age.ordinal());
		VoidNetwork.sendDataToClient(ExPPackets.NEW_AGE, tag, to);
	}
	
	public static void sendRequestPacket()
	{
	}

}
