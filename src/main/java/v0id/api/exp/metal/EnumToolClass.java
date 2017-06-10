package v0id.api.exp.metal;

import net.minecraft.util.IStringSerializable;

public enum EnumToolClass implements IStringSerializable
{
	PICKAXE("pickaxe"),
	AXE("axe"),
	SHOVEL("spade"),
	HOE("hoe"),
	SWORD("sword"),
	KNIFE("knife"),
	SCYTHE("scythe"),
	BATTLEAXE("axe"),
	HAMMER("hammer"),
	SPEAR("sword"),
	WATERING_CAN("watering_can"),
	GARDENING_SPADE("spade");

	EnumToolClass(String s)
	{
		this.toolClass = s;
	}
	
	private final String toolClass;
	
	@Override
	public String getName()
	{
		return this.toolClass;
	}
}
