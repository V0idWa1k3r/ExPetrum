package v0id.exp.net;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import v0id.api.core.VoidApi;
import v0id.api.core.network.IPacketHandler;
import v0id.api.core.network.VoidNetwork;
import v0id.api.exp.data.ExPPackets;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.world.ExPWorld;

public class PacketHandlerWorldData implements IPacketHandler
{

	@Override
	public void handleData(NBTTagCompound data)
	{
		World w = VoidApi.proxy.getClientWorld();
		if (w != null && w.provider != null && w.provider.getDimension() == 0)
		{
			IExPWorld.of(w).deserializeNBT(data);
		}
	}

	@Override
	public NBTTagCompound handleRequest(String requester)
	{
		WorldServer ws = DimensionManager.getWorld(0);
		return IExPWorld.of(ws).serializeNBT();
	}

	public static void sendSyncPacket(World w)
	{
		IExPWorld world = IExPWorld.of(w);
		NBTTagCompound worldTag = ((ExPWorld)world).serializePartialNBT();
		VoidNetwork.sendDataToAll(ExPPackets.WORLD_DATA, worldTag);
	}
	
	public static void sendRequestPacket()
	{
		VoidNetwork.sendRequestToServer(ExPPackets.WORLD_DATA);
	}
}
