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
		blockCrop																							= new ResourceLocation(modid, "crop"),
		blockFarmland																						= new ResourceLocation(modid, "farmland"),
		blockBerryBushNormal																				= new ResourceLocation(modid, "berry_bush_normal"),
		blockBerryBushBerries																				= new ResourceLocation(modid, "berry_bush_berries"),
		blockBerryBushAutumn																				= new ResourceLocation(modid, "berry_bush_autumn"),
		blockBerryBushDead																					= new ResourceLocation(modid, "berry_bush_dead");

	public static final ResourceLocation
		itemRock																							= new ResourceLocation(modid, "item_rock"),
		itemStick																							= new ResourceLocation(modid, "item_stick"),
		itemToolhead																						= new ResourceLocation(modid, "item_toolhead"),
		itemSeeds																							= new ResourceLocation(modid, "item_seeds"),
		itemFood																							= new ResourceLocation(modid, "item_food"),
		itemIngot																							= new ResourceLocation(modid, "item_ingot"),
		itemKnife																							= new ResourceLocation(modid, "item_knife"),
		itemPickaxe																							= new ResourceLocation(modid, "item_pickaxe"),
		itemAxe																								= new ResourceLocation(modid, "item_axe"),
		itemShovel																							= new ResourceLocation(modid, "item_shovel"),
		itemHoe																								= new ResourceLocation(modid, "item_hoe"),
		itemSword																							= new ResourceLocation(modid, "item_sword"),
		itemScythe																							= new ResourceLocation(modid, "item_scythe"),
		itemBattleaxe																						= new ResourceLocation(modid, "item_battleaxe"),
		itemHammer																							= new ResourceLocation(modid, "item_hammer"),
		itemSpear																							= new ResourceLocation(modid, "item_spear"),
		itemWateringCan																						= new ResourceLocation(modid, "item_watering_can"),
		itemGardeningSpade																					= new ResourceLocation(modid, "item_gardening_spade");
	
	public static final ResourceLocation
		entityGravFallingBlock																				= new ResourceLocation(modid, "gravFallingBlock"),
		entityFallingTree																					= new ResourceLocation(modid, "fallingTree"),
		entityThrownWeapon																					= new ResourceLocation(modid, "thrownWeapon");
	
	public static final ResourceLocation
		specialAttackPiercingDash																			= new ResourceLocation(modid, "piercingDash"),
		specialAttackSlash																					= new ResourceLocation(modid, "slash"),
		specialAttackDownStrike																				= new ResourceLocation(modid, "downStrike"),
		specialAttackSpin																					= new ResourceLocation(modid, "spin"),
		specialAttackShieldSlam																				= new ResourceLocation(modid, "shieldSlam"),
		specialAttackBehead																					= new ResourceLocation(modid, "behead"),
		specialAttackStab																					= new ResourceLocation(modid, "stab"),
		specialAttackThrow																					= new ResourceLocation(modid, "entityThrow");
	
	public static final ResourceLocation
		potionStunned																						= new ResourceLocation(modid, "stunned");
	
	public static final String
		fluidSaltWater																						= "exp.salt_water",
		fluidFreshWater																						= "water",
		fluidLava																							= "exp.lava",
		fluidOil																							= "oil";
}
