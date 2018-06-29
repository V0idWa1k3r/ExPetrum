package v0id.exp.registry;

import v0id.core.network.VoidNetwork;
import v0id.api.exp.data.ExPPackets;
import v0id.exp.net.*;

public class ExPNetworkRegistry extends AbstractRegistry
{
	static
	{
		VoidNetwork.registerHandler(ExPPackets.FALLING_TREE, new PacketHandlerFallingTree());
		VoidNetwork.registerHandler(ExPPackets.PLAYER_DATA, new PacketHandlerPlayerData());
		VoidNetwork.registerHandler(ExPPackets.WORLD_DATA, new PacketHandlerWorldData());
		VoidNetwork.registerHandler(ExPPackets.SPECIAL_ATTACK, new PacketHandlerSpecialAttack());
        VoidNetwork.registerHandler(ExPPackets.SETTINGS_SYNC, new PacketHandlerSettingsSync());
        VoidNetwork.registerHandler(ExPPackets.CRAFT_POTTERY, new PacketHandlerCraftPottery());
	}
	
	public ExPNetworkRegistry()
	{
		super();
	}
}
