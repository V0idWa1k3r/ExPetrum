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
		blockLeaves																							= new ResourceLocation(modid, "leaves");

	public static final ResourceLocation
		entityGravFallingBlock																				= new ResourceLocation(modid, "gravFallingBlock"),
		entityFallingTree																					= new ResourceLocation(modid, "fallingTree");
	
	public static final String
		fluidSaltWater																						= "exp.salt_water",
		fluidFreshWater																						= "water",
		fluidLava																							= "exp.lava";
}
