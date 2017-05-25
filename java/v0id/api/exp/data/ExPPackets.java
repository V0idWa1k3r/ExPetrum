package v0id.api.exp.data;

import v0id.api.core.network.PacketType;

public class ExPPackets
{
	public static final String modid = "exp";
	
	public static final PacketType FALLING_TREE = PacketType.obtain(modid, "fallingTree");
	public static final PacketType PLAYER_DATA = PacketType.obtain(modid, "playerData");
	public static final PacketType WORLD_DATA = PacketType.obtain(modid, "worldData");
	public static final PacketType SPECIAL_ATTACK = PacketType.obtain(modid, "specialAttack");
}
