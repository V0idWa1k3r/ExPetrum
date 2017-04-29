package v0id.api.exp.data;

import java.util.Random;

import net.minecraft.world.WorldType;
import v0id.api.core.logging.VoidLogger;
import v0id.api.core.markers.StaticStorage;

@StaticStorage
public class ExPMisc
{
	public static WorldType worldTypeExP;
	public static final double SEAWEED_GROWTH_RATE = 0.1;
	public static final int SEAWEED_SPREAD_OFFSET = 3;
	public static final int GRASS_SPREAD_OFFSET = 2;
	public static final Random modelVariantRandom = new Random();
	
	public static VoidLogger modLogger;
}
