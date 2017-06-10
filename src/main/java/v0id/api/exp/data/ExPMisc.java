package v0id.api.exp.data;

import java.util.Random;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.util.EnumHelper;
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
	
	// This is a placeholder. Every tool stat is exposed differently! This only exists to populate toolMaterial field in ItemTool! Do not use/reference!
	public static final ToolMaterial materialExPetrum = EnumHelper.addToolMaterial("EXPETRUM", 1, 1, 1, 1, 1);
	public static VoidLogger modLogger;
}
