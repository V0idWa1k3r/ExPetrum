package v0id.core.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import v0id.core.VCLoggers;
import v0id.core.VoidApi;
import v0id.core.logging.LogLevel;
import v0id.core.util.MC;
import v0id.core.util.Players;
import v0id.core.util.nbt.NBTChain;

@SuppressWarnings({"WeakerAccess", "unused"})
public class VoidNetwork
{
	public static SimpleNetworkWrapper networkManager;
	public static final BiMap<Short, IPacketHandler> handlers = HashBiMap.create();
	
	public static void registerHandler(PacketType type, IPacketHandler handler)
	{
		handlers.put(type.internalID, handler);
	}
	
	public static void sendRequestToAllAround(PacketType toRequest, TargetPoint pt)
	{
		sendDataToAllAround(PacketType.Request, NBTChain.startChain().withShort("requestID", toRequest.internalID).endChain(), pt);
	}
	
	public static void sendDataToAllAround(PacketType type, NBTTagCompound toSend, TargetPoint pt)
	{
		if (MC.getSide() == Side.CLIENT)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "A mod attempted to send a packet to a client while being on a client side already! [packet - %s:%d]", type.registryName, type.internalID);
			return;
		}
		
		sendData(type, toSend, null, pt);
	}
	
	public static void sendRequestToAll(PacketType toRequest)
	{
		sendDataToAll(PacketType.Request, NBTChain.startChain().withShort("requestID", toRequest.internalID).endChain());
	}
	
	public static void sendDataToAll(PacketType type, NBTTagCompound toSend)
	{
		if (MC.getSide() == Side.CLIENT)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "A mod attempted to send a packet to a client while being on a client side already! [packet - %s:%d]", type.registryName, type.internalID);
			return;
		}
		
		sendData(type, toSend, null, null);
	}
	
	public static void sendRequestToClient(PacketType toRequest, EntityPlayerMP to)
	{
		sendDataToClient(PacketType.Request, NBTChain.startChain().withShort("requestID", toRequest.internalID).endChain(), to);
	}
	
	public static void sendDataToClient(PacketType type, NBTTagCompound toSend, EntityPlayerMP to)
	{
		if (MC.getSide() == Side.CLIENT)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "A mod attempted to send a packet to a client while being on a client side already! [packet - %s:%d]", type.registryName, type.internalID);
			return;
		}
		
		sendData(type, toSend, to, null);
	}
	
	public static void sendRequestToServer(PacketType toRequest)
	{
		sendDataToServer(PacketType.Request, NBTChain.startChain().withShort("requestID", toRequest.internalID).withString("requester", Players.getClientPlayerUsername()).endChain());
	}
	
	public static void sendDataToServer(PacketType type, NBTTagCompound toSend)
	{
		if (MC.getSide() == Side.SERVER)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "A mod attempted to send a packet to a server while being on a server side already! [packet - %s:%d]", type.registryName, type.internalID);
			return;
		}
		
		sendData(type, toSend, null, null);
	}
	
	public static void sendRequest(PacketType toRequest, EntityPlayerMP to, TargetPoint point)
	{
		sendData(PacketType.Request, NBTChain.startChain().withShort("requestID", toRequest.internalID).endChain(), to, point);
	}
	
	public static void sendData(PacketType type, NBTTagCompound toSend, EntityPlayerMP to, TargetPoint point)
	{
		Packet p = new Packet(type, toSend);
		send(p, to, point);
	}
	
	public static void send(Packet packet, EntityPlayerMP to, TargetPoint point)
	{
		Side s = MC.getSide();
		if (s == Side.CLIENT)
		{
			if (packet.id == PacketType.Request.internalID)
			{
				packet.data.setString("requester", VoidApi.proxy.getClientPlayerName());
			}
			
			networkManager.sendToServer(packet);
		}
		else
		{
			if (to != null)
			{
				networkManager.sendTo(packet, to);
			}
			else
			{
				if (point != null)
				{
					networkManager.sendToAllAround(packet, point);
				}
				else
				{
					networkManager.sendToAll(packet);
				}
			}
		}
	}
}
