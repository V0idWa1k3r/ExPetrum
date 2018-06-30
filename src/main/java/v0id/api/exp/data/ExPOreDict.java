package v0id.api.exp.data;

import org.apache.commons.lang3.ArrayUtils;
import v0id.core.markers.StaticStorage;
import v0id.api.exp.block.EnumBerry;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.tile.crop.EnumCrop;

import java.util.stream.Stream;

@StaticStorage
public class ExPOreDict
{
	public static final String[]
		blockBoulder																									= { "boulder" },
		blockBoulderOre																									= { "boulderOre", "blockOreHint" },
		blockCoralRock																									= { "coral", "rockCoral", "stoneCoral" },
		blockGrass																										= { "grass" },
		blockIce																										= { "ice" },
		blockIceSalt																									= { "iceSalt" },
		blockIceFresh																									= { "iceFresh" },
		blockOre																										= { "oreExP", "oreExPetrum", "oreUnknown" },
		blockPebble																										= { "pebble", "smallRocks" },
		blockSand																										= { "sand" },
		blockSnow																										= { "snowLayer" },
		blockSoil																										= { "dirt", "soil" },
		blockStone																										= { "stone" },
		blockShrub																										= { "shrub", "bush" },
		blockVegetation																									= { "tallgrass" },
		blockWaterLily																									= { "waterlily", "waterLily", "lilyPad" },
		blockLog																										= { "logWood" },
		blockLeaf																										= { "treeLeaves" },
		blockFlint                                                                                                      = { "blockFlint" };
	
	public static final String[]
		itemRock																										= { "itemRock", "rock" },
		itemStick																										= { "stickWood" },
		itemToolHeads																									= { "toolheadHammerStone", "toolheadAxeStone", "toolheadShovelStone", "toolheadSpearStone", "toolheadKnifeStone", "toolheadChiselStone", "toolheadArrowStone" },
		itemSeeds																										= { "seeds", "seed" },
		itemAxe                                                                                                         = { "axe" },
        itemBasket                                                                                                      = { "basket" },
        itemBattleaxe                                                                                                   = { "battleaxe", "axe" },
        itemChisel                                                                                                      = { "chisel" },
        itemGardeningSpade                                                                                              = { "gardeningSpade" },
        itemHammer                                                                                                      = { "hammer" },
        itemHoe                                                                                                         = { "hoe" },
        itemKnife                                                                                                       = { "knife" },
        itemPickaxe                                                                                                     = { "pickaxe" },
        itemSaw                                                                                                         = { "saw" },
        itemScythe                                                                                                      = { "scythe" },
        itemShovel                                                                                                      = { "shovel", "spade" },
        itemSpear                                                                                                       = { "spear" },
        itemSword                                                                                                       = { "sword" },
        itemWateringCan                                                                                                 = { "wateringCan" };
	
	public static final String[] rockNames = Stream.of(EnumRockClass.values()).map(EnumRockClass::getName).toArray(String[]::new);
	public static final String[] soilNames = Stream.of(EnumDirtClass.values()).map(EnumDirtClass::getName).toArray(String[]::new);
	public static final String[] treeNames = Stream.of(EnumTreeType.values()).map(EnumTreeType::getName).toArray(String[]::new);
	public static final String[] bushNames = Stream.of(EnumShrubType.values()).map(EnumShrubType::getName).toArray(String[]::new);
	public static final String[] cropNames = Stream.of(EnumCrop.values()).filter(EnumCrop.DEAD::nequals).map(EnumCrop::getName).toArray(String[]::new);
	public static final String[] berryNames = Stream.of(EnumBerry.values()).map(EnumBerry::getName).toArray(String[]::new);
	public static final String[] stickNames = ArrayUtils.addAll(ArrayUtils.addAll(treeNames, bushNames), berryNames);
}
