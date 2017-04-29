package v0id.exp.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import v0id.api.core.VoidApi;
import v0id.api.core.network.IPacketHandler;
import v0id.api.core.network.VoidNetwork;
import v0id.api.exp.data.ExPPackets;
import v0id.api.exp.player.IExPPlayer;
import v0id.exp.player.ExPPlayer;

public class PacketHandlerPlayerData implements IPacketHandler
{

	@Override
	public void handleData(NBTTagCompound data)
	{
		EntityPlayer reciever = VoidApi.proxy.getClientPlayer();
		if (reciever.getName().equals(data.getString("owner")))
		{
			IExPPlayer.of(reciever).deserializeNBT(data);
		}
	}

	// If a client sent a request packet it wants the full data!
	@Override
	public NBTTagCompound handleRequest(String requester)
	{
		EntityPlayerMP requesterPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(requester);
		IExPPlayer player = IExPPlayer.of(requesterPlayer);
		return player.serializeNBT();
	}
	
	// Only syncs partial!
	public static void sendSyncPacket(EntityPlayerMP to)
	{
		IExPPlayer player = IExPPlayer.of(to);
		NBTTagCompound dataTag = ((ExPPlayer)player).serializePartialNBT();
		VoidNetwork.sendDataToClient(ExPPackets.PLAYER_DATA, dataTag, to);
	}
	
	public static void sendRequestPacket()
	{
		VoidNetwork.sendRequestToServer(ExPPackets.PLAYER_DATA);
	}

}
