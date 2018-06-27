package v0id.api.exp.metal;

import net.minecraft.util.IStringSerializable;

public enum EnumToolClass implements IStringSerializable
{
	PICKAXE("pickaxe", 1.5F),
	AXE("axe", 1.2F),
	SHOVEL("spade", 1.4F),
	HOE("hoe", 1.0F),
	SWORD("sword", 1.0F),
	KNIFE("knife", 0.6F),
	SCYTHE("scythe", 1.2F),
	BATTLEAXE("axe", 1.5F),
	HAMMER("hammer", 1.8F),
	SPEAR("sword", 1.0F),
	WATERING_CAN("watering_can", 0.2F),
	GARDENING_SPADE("spade", 0.1F),
	SAW("saw", 0.5F),
    CHISEL("chisel", 0.1F);

	EnumToolClass(String s, float f)
	{
		this.toolClass = s;
		this.weight = f;
	}
	
	private final String toolClass;
	private final float weight;
	
	@Override
	public String getName()
	{
		return this.toolClass;
	}

    public float getWeight()
    {
        return weight;
    }
}
