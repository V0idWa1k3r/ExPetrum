package v0id.api.exp.data;

import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import v0id.api.core.markers.StaticStorage;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.tile.crop.EnumCrop;

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
		blockLeaf																										= { "treeLeaves" };
	
	public static final String[]
		itemRock																										= { "itemRock" },
		itemStick																										= { "stickWood" },
		itemToolHeads																									= { "toolheadHammerStone", "toolheadAxeStone", "toolheadShovelStone", "toolheadSpearStone", "toolheadKnifeStone", "toolheadChiselStone", "toolheadArrowStone" },
		itemSeeds																										= { "seeds", "seed" };
	
	public static final String[] rockNames = Stream.of(EnumRockClass.values()).map(EnumRockClass::getName).toArray(String[]::new);
	public static final String[] soilNames = Stream.of(EnumDirtClass.values()).map(EnumDirtClass::getName).toArray(String[]::new);
	public static final String[] treeNames = Stream.of(EnumTreeType.values()).map(EnumTreeType::getName).toArray(String[]::new);
	public static final String[] bushNames = Stream.of(EnumShrubType.values()).map(EnumShrubType::getName).toArray(String[]::new);
	public static final String[] cropNames = Stream.of(EnumCrop.values()).filter(EnumCrop.DEAD::nequals).map(EnumCrop::getName).toArray(String[]::new);
	public static final String[] stickNames = ArrayUtils.addAll(treeNames, bushNames);
}
