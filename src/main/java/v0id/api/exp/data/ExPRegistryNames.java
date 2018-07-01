package v0id.api.exp.data;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import v0id.core.markers.StaticStorage;

import java.util.Map;

@StaticStorage
public class ExPRegistryNames
{
	public static final String modid = "exp";
	
	public static final String
		blockStone																							= "rock",
		blockSoil																							= "soil",
		blockGrass																							= "grass",
		blockGrass_dry																						= "grass_dry",
		blockGrass_dead																						= "grass_dead",
		blockSaltWater																						= "salt_water",
		blockFreshWater																						= "fresh_water",
		blockLava																							= "lava",
		blockWaterLily																						= "lilypad",
		blockCattail																						= "cattail",
		blockVegetation																						= "vegetation",
		blockSand																							= "sand",
		blockSeaweed																						= "seaweed",
		blockCoralRock																						= "coralrock",
		blockCoralPlant																						= "coralplant",
		blockLog																							= "log",
		blockLogDeco																						= "logDeco",
		blockLeaves																							= "leaves",
		blockOre																							= "ore",
		blockPebble																							= "pebble",
		blockBoulder																						= "boulder",
		blockBoulderOre																						= "boulder_ore",
		blockOil																							= "oil",
		blockShrubNormal																					= "shrub_normal",
		blockShrubBlooming																					= "shrub_blooming",
		blockShrubAutumn																					= "shrub_autumn",
		blockShrubDead																						= "shrub_dead",
		blockSnow																							= "snow",
		blockIce																							= "ice",
		blockBoulderWorked																					= "boulder_worked",
		blockCrop																							= "crop",
		blockFarmland																						= "farmland",
		blockBerryBushNormal																				= "berry_bush_normal",
		blockBerryBushBerries																				= "berry_bush_berries",
		blockBerryBushAutumn																				= "berry_bush_autumn",
		blockBerryBushDead																					= "berry_bush_dead",
        blockFruit																			        		= "fruit",
	    blockGenericShrubbery                                                                               = "generic_shrubbery",
	    blockFlint                                                                                          = "flint",
        blockClay                                                                                           = "clay",
		blockDecoratedStoneTile																	            = "rock_decorated_tile",
        blockDecoratedStoneBrick                                                                            = "rock_decorated_brick",
        blockDecoratedStoneBrickSmall                                                                       = "rock_decorated_brick_small",
        blockCampfire                                                                                       = "campfire",
        blockPotteryStation                                                                                 = "pottery_station",
        blockPottery                                                                                        = "pottery",
		blockLogPile                                                                                        = "log_pile",
        blockCharcoal                                                                                       = "charcoal",
		blockKaolin																							= "kaolin",
        blockPlanks                                                                                         = "planks",
        blockCraftingTable                                                                                  = "crafting_table";

	public static final String
		itemRock																							= "item_rock",
		itemStick																							= "item_stick",
		itemToolhead																						= "item_toolhead",
		itemSeeds																							= "item_seeds",
		itemFood																							= "item_food",
		itemIngot																							= "item_ingot",
		itemKnife																							= "item_knife",
		itemPickaxe																							= "item_pickaxe",
		itemAxe																								= "item_axe",
		itemShovel																							= "item_shovel",
		itemHoe																								= "item_hoe",
		itemSword																							= "item_sword",
		itemScythe																							= "item_scythe",
		itemBattleaxe																						= "item_battleaxe",
		itemHammer																							= "item_hammer",
		itemSpear																							= "item_spear",
		itemWateringCan																						= "item_watering_can",
		itemGardeningSpade																					= "item_gardening_spade",
		itemBasket																							= "item_basket",
		itemSaw																								= "item_saw",
        itemGeneric                                                                                         = "item_generic",
        itemChisel                                                                                          = "item_chisel",
		itemOre                                                                                             = "item_ore",
        itemMoldTool                                                                                        = "item_tool_mold",
        itemMoldIngot                                                                                       = "item_ingot_mold",
		itemPottery                                                                                         = "item_pottery";

	public static final String
        biomeBeach                                                                                          = "beach",
        biomeColdForest                                                                                     = "coldforest",
        biomeColdPlains                                                                                     = "coldplains",
        biomeDenseColdForest                                                                                = "densecoldforest",
        biomeDenseForest                                                                                    = "denseforest",
        biomeDenseWarmForest                                                                                = "densewarmforest",
        biomeDesert                                                                                         = "desert",
        biomeForest                                                                                         = "forest",
        biomeHills                                                                                          = "hills",
        biomeJungle                                                                                         = "jungle",
        biomeLake                                                                                           = "lake",
        biomeMountains                                                                                      = "mountains",
        biomeOcean                                                                                          = "ocean",
        biomePlains                                                                                         = "plains",
        biomeRareForest                                                                                     = "rareforest",
        biomeRiver                                                                                          = "river",
        biomeSavanna                                                                                        = "savanna",
        biomeWarmForest                                                                                     = "warmforest",
        biomeWarmPlains                                                                                     = "warmplains";

	public static final ResourceLocation
		entityGravFallingBlock																				= new ResourceLocation(modid, "gravFallingBlock"),
		entityFallingTree																					= new ResourceLocation(modid, "fallingTree"),
		entityThrownWeapon																					= new ResourceLocation(modid, "thrownWeapon"),
        entityChicken																					    = new ResourceLocation(modid, "chicken");
	
	public static final ResourceLocation
		specialAttackPiercingDash																			= new ResourceLocation(modid, "piercingDash"),
		specialAttackSlash																					= new ResourceLocation(modid, "slash"),
		specialAttackDownStrike																				= new ResourceLocation(modid, "downStrike"),
		specialAttackSpin																					= new ResourceLocation(modid, "spin"),
		specialAttackShieldSlam																				= new ResourceLocation(modid, "shieldSlam"),
		specialAttackBehead																					= new ResourceLocation(modid, "behead"),
		specialAttackStab																					= new ResourceLocation(modid, "stab"),
		specialAttackThrow																					= new ResourceLocation(modid, "entityThrow");
	
	public static final String
		potionStunned																						="stunned";
	
	public static final String
		fluidSaltWater																						= "exp.salt_water",
		fluidFreshWater																						= "water",
		fluidLava																							= "exp.lava",
		fluidOil																							= "oil",
        fluidClay                                                                                           = "clay";

	private static final Map<String, ResourceLocation> cache = Maps.newHashMap();

	public static ResourceLocation asLocation(String name)
    {
        return asLocation(name, true);
    }

    public static ResourceLocation asLocation(String name, boolean doCache)
    {
        if (doCache)
        {
            if (!cache.containsKey(name))
            {
                cache.put(name, new ResourceLocation(modid, name));
            }

            return cache.get(name);
        }
        else
        {
            return new ResourceLocation(modid, name);
        }
    }
}
