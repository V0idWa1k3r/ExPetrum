package v0id.api.exp.data;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import v0id.api.exp.block.*;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.block.property.EnumKaolinType;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.block.property.EnumWaterLilyType;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.core.markers.StaticStorage;
import v0id.exp.block.BlockPottery;

@StaticStorage
public class ExPBlockProperties
{	
	public static final PropertyEnum<EnumRockClass> ROCK_CLASS = PropertyEnum.create("class", EnumRockClass.class);
	public static final PropertyEnum<EnumDirtClass> DIRT_CLASS = PropertyEnum.create("class", EnumDirtClass.class);
	public static final PropertyBool PLANT_BLOOMING = PropertyBool.create("blooming");
	public static final PropertyBool POTTERYSTATION_HASCLAY = PropertyBool.create("has_clay");
	public static final PropertyEnum<EnumWaterLilyType> LILY_TYPE = PropertyEnum.create("type", EnumWaterLilyType.class);
	public static final PropertyInteger VEGETATION_GROWTH = PropertyInteger.create("growth", 0, 3);
    public static final PropertyEnum<EnumTreeType> TREE_TYPE = PropertyEnum.create("ttype", EnumTreeType.class);
    public static final PropertyEnum<EnumFruit> FRUIT_TYPE = PropertyEnum.create("type", EnumFruit.class);
	public static final PropertyEnum<EnumLeafState> LEAF_STATE = PropertyEnum.create("leafstate", EnumLeafState.class);
	public static final PropertyInteger ORE_TEXTURE_ID = PropertyInteger.create("oretexture", 0, 2);
	public static final PropertyEnum<EnumShrubType> SHRUB_TYPE = PropertyEnum.create("type", EnumShrubType.class);
	public static final PropertyEnum<EnumBerry> BERRY_BUSH_TYPE = PropertyEnum.create("type", EnumBerry.class);
	public static final PropertyEnum<BlockPottery.EnumPotteryType> POTTERY_TYPE = PropertyEnum.create("type", BlockPottery.EnumPotteryType.class);
	public static final PropertyEnum<EnumKaolinType> KAOLIN_TYPE = PropertyEnum.create("type", EnumKaolinType.class);
	public static final PropertyEnum<EnumAnvilMaterial> ANVIL_MATERIAL = PropertyEnum.create("material", EnumAnvilMaterial.class);
	public static final PropertyBool SHRUB_IS_TALL = PropertyBool.create("istall");
	public static final PropertyBool ICE_IS_SALT = PropertyBool.create("salt");
	public static final PropertyBool LOGPILE_IS_ROTATED = PropertyBool.create("rotated");
	public static final PropertyBool FORGE_ISLIT = PropertyBool.create("active");
	public static final PropertyBool CRUCIBLE_HASMETAL = PropertyBool.create("has_metal");
	public static final PropertyBool BLOOMERY_ISLIT = PropertyBool.create("active");
	public static final PropertyInteger WORKED_BOULDER_INDEX = PropertyInteger.create("workindex", 0, 7);
	public static final PropertyInteger LOGPILE_COUNT = PropertyInteger.create("count", 1, 8);
	public static final PropertyInteger CHARCOAL_COUNT = PropertyInteger.create("count", 1, 16);
	public static final PropertyInteger TROUGH_WATER = PropertyInteger.create("water", 0, 10);
	public static final PropertyInteger PRESS_VALUE = PropertyInteger.create("value", 0, 8);
	// Note that some values of this property are unused, use the CropData from EnumCrop obtained from a capability.
	public static final PropertyInteger CROP_GROWTH_STAGE = PropertyInteger.create("stage", 0, 15);
	public static final PropertyEnum<EnumCrop> CROP_TYPE = PropertyEnum.create("type", EnumCrop.class);
    public static final PropertyEnum<EnumShrubberyType> SHRUBBERY_TYPE = PropertyEnum.create("type", EnumShrubberyType.class);
    public static final PropertyEnum<EnumMoltenMetalState> MOLTEN_METAL_STATE = PropertyEnum.create("state", EnumMoltenMetalState.class);

	// Currently not used
	public static final PropertyEnum<EnumOre> ORE_TYPE = PropertyEnum.create("oretype", EnumOre.class);
    public static final PropertyEnum<EnumGrassAmount> VEGETATION_LEVEL = PropertyEnum.create("level", EnumGrassAmount.class);
}
