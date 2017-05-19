package v0id.api.exp.data;

import net.minecraft.util.ResourceLocation;
import v0id.api.core.markers.StaticStorage;

@StaticStorage
public class ExPRegistryNames
{
	public static final String modid = "exp";
	
	public static final ResourceLocation
		blockStone																							= new ResourceLocation(modid, "rock"),
		blockSoil																							= new ResourceLocation(modid, "soil"),
		blockGrass																							= new ResourceLocation(modid, "grass"),
		blockGrass_dry																						= new ResourceLocation(modid, "grass_dry"),
		blockGrass_dead																						= new ResourceLocation(modid, "grass_dead"),
		blockSaltWater																						= new ResourceLocation(modid, "salt_water"),
		blockFreshWater																						= new ResourceLocation(modid, "fresh_water"),
		blockLava																							= new ResourceLocation(modid, "lava"),
		blockWaterLily																						= new ResourceLocation(modid, "lilypad"),
		blockCattail																						= new ResourceLocation(modid, "cattail"),
		blockVegetation																						= new ResourceLocation(modid, "vegetation"),
		blockSand																							= new ResourceLocation(modid, "sand"),
		blockSeaweed																						= new ResourceLocation(modid, "seaweed"),
		blockCoralRock																						= new ResourceLocation(modid, "coralrock"),
		blockCoralPlant																						= new ResourceLocation(modid, "coralplant"),
		blockLog																							= new ResourceLocation(modid, "log"),
		blockLogDeco																						= new ResourceLocation(modid, "logDeco"),
		blockLeaves																							= new ResourceLocation(modid, "leaves"),
		blockOre																							= new ResourceLocation(modid, "ore"),
		blockPebble																							= new ResourceLocation(modid, "pebble"),
		blockBoulder																						= new ResourceLocation(modid, "boulder"),
		blockBoulderOre																						= new ResourceLocation(modid, "boulder_ore"),
		blockOil																							= new ResourceLocation(modid, "oil"),
		blockShrubNormal																					= new ResourceLocation(modid, "shrub_normal"),
		blockShrubBlooming																					= new ResourceLocation(modid, "shrub_blooming"),
		blockShrubAutumn																					= new ResourceLocation(modid, "shrub_autumn"),
		blockShrubDead																						= new ResourceLocation(modid, "shrub_dead"),
		blockSnow																							= new ResourceLocation(modid, "snow"),
		blockIce																							= new ResourceLocation(modid, "ice"),
		blockBoulderWorked																					= new ResourceLocation(modid, "boulder_worked"),
		blockCrop																							= new ResourceLocation(modid, "crop");

	public static final ResourceLocation
		itemRock																							= new ResourceLocation(modid, "item_rock"),
		itemStick																							= new ResourceLocation(modid, "item_stick"),
		itemToolhead																						= new ResourceLocation(modid, "item_toolhead");
	
	public static final ResourceLocation
		entityGravFallingBlock																				= new ResourceLocation(modid, "gravFallingBlock"),
		entityFallingTree																					= new ResourceLocation(modid, "fallingTree");
	
	public static final String
		fluidSaltWater																						= "exp.salt_water",
		fluidFreshWater																						= "water",
		fluidLava																							= "exp.lava",
		fluidOil																							= "oil";
}
