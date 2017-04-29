package v0id.exp.registry;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.core.network.VoidNetwork;
import v0id.api.exp.data.ExPPackets;
import v0id.exp.net.PacketHandlerFallingTree;
import v0id.exp.net.PacketHandlerPlayerData;
import v0id.exp.net.PacketHandlerWorldData;

public class ExPNetworkRegistry extends AbstractRegistry
{
	public ExPNetworkRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		super.preInit(evt);
		VoidNetwork.registerHandler(ExPPackets.FALLING_TREE, new PacketHandlerFallingTree());
		VoidNetwork.registerHandler(ExPPackets.PLAYER_DATA, new PacketHandlerPlayerData());
		VoidNetwork.registerHandler(ExPPackets.WORLD_DATA, new PacketHandlerWorldData());
	}
}
