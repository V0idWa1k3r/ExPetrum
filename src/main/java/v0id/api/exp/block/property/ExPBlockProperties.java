package v0id.api.exp.block.property;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import v0id.api.core.markers.StaticStorage;
import v0id.api.exp.block.EnumBerry;
import v0id.api.exp.block.EnumGrassAmount;
import v0id.api.exp.block.EnumLeafState;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.tile.crop.EnumCrop;

@StaticStorage
public class ExPBlockProperties
{	
	public static final PropertyEnum<EnumRockClass> ROCK_CLASS = PropertyEnum.create("class", EnumRockClass.class);
	public static final PropertyEnum<EnumDirtClass> DIRT_CLASS = PropertyEnum.create("class", EnumDirtClass.class);
	public static final PropertyBool PLANT_BLOOMING = PropertyBool.create("blooming");
	public static final PropertyEnum<EnumWaterLilyType> LILY_TYPE = PropertyEnum.create("type", EnumWaterLilyType.class);
	public static final PropertyEnum<EnumGrassAmount> VEGETATION_LEVEL = PropertyEnum.create("level", EnumGrassAmount.class);
	public static final PropertyInteger VEGETATION_GROWTH = PropertyInteger.create("growth", 0, 3);
	// public static PropertyEnum<EnumTreeType>[] TREE_TYPES = new PropertyEnum[6];
    public static PropertyEnum<EnumTreeType> TREE_TYPE = PropertyEnum.create("ttype", EnumTreeType.class);
	public static final PropertyEnum<EnumLeafState> LEAF_STATE = PropertyEnum.create("leafstate", EnumLeafState.class);
	public static final PropertyInteger ORE_TEXTURE_ID = PropertyInteger.create("oretexture", 0, 2);
	public static final PropertyEnum<EnumShrubType> SHRUB_TYPE = PropertyEnum.create("type", EnumShrubType.class);
	public static final PropertyEnum<EnumBerry> BERRY_BUSH_TYPE = PropertyEnum.create("type", EnumBerry.class);
	public static final PropertyBool SHRUB_IS_TALL = PropertyBool.create("istall");
	public static final PropertyBool ICE_IS_SALT = PropertyBool.create("salt");
	public static final PropertyInteger WORKED_BOULDER_INDEX = PropertyInteger.create("workindex", 0, 7);
	// Note that some values of this property are unused, use the CropData from EnumCrop obtained from a capability.
	public static final PropertyInteger CROP_GROWTH_STAGE = PropertyInteger.create("stage", 0, 15);
	public static final PropertyEnum<EnumCrop> CROP_TYPE = PropertyEnum.create("type", EnumCrop.class);
	
	// Currently not used
	public static final PropertyEnum<EnumOre> ORE_TYPE = PropertyEnum.create("oretype", EnumOre.class);
}
