package v0id.core.network;

import com.google.common.collect.HashBiMap;

import net.minecraft.util.ResourceLocation;
import v0id.core.network.handlers.HandlerBiome;
import v0id.core.network.handlers.HandlerRequest;
import v0id.core.network.handlers.HandlerTileData;

@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
public class PacketType
{
	public short getInternalID()
	{
		return internalID;
	}

	public String getRegistryName()
	{
		return registryName;
	}

	short internalID;
	final String registryName;
	
	private static short idCounter = -1;
	public static final HashBiMap<Short, PacketType> registry =  HashBiMap.create();
	
	private PacketType(String s)
	{
		this.registryName = s;
		this.internalID = ++idCounter;
	}
	
	public static PacketType obtain(ResourceLocation loc)
	{
		return obtain(loc.toString());
	}
	
	public static PacketType obtain(String mod, String name)
	{
		return obtain(mod + ":" + name);
	}
	
	public static PacketType obtain(String name)
	{
		PacketType ret = new PacketType(name);
		registry.put(ret.internalID, ret);
		return ret;
	}
	
	//Built-in packet types
	public static final PacketType Request 	= obtain("voidapi", "request"	);
	public static final PacketType TileData = obtain("voidapi", "tiledata"	);
	public static final PacketType Particle = obtain("voidapi", "particle"	);
	public static final PacketType Sound 	= obtain("voidapi", "sound"		);
	public static final PacketType Biome 	= obtain("voidapi", "biome"		);
	
	static
	{
		VoidNetwork.registerHandler(Request, new HandlerRequest());
		VoidNetwork.registerHandler(TileData, new HandlerTileData());
		VoidNetwork.registerHandler(Biome, new HandlerBiome());
	}
}
