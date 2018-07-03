package v0id.api.exp.metal;

import net.minecraft.util.IStringSerializable;

public enum EnumToolClass implements IStringSerializable
{
	PICKAXE("pickaxe", 1.5F, EnumAnvilRequirement.INGOT, 120),
	AXE("axe", 1.2F, EnumAnvilRequirement.SHEET, 120),
	SHOVEL("spade", 1.4F, EnumAnvilRequirement.SHEET, 120),
	HOE("hoe", 1.0F, EnumAnvilRequirement.INGOT, 100),
	SWORD("sword", 1.0F, EnumAnvilRequirement.SHEET, 100),
	KNIFE("knife", 0.6F, EnumAnvilRequirement.INGOT, 40),
	SCYTHE("scythe", 1.2F, EnumAnvilRequirement.SHEET, 140),
	BATTLEAXE("axe", 1.5F, EnumAnvilRequirement.DOUBLE_SHEET, 160),
	HAMMER("hammer", 1.8F, EnumAnvilRequirement.DOUBLE_SHEET, 100),
	SPEAR("sword", 1.0F, EnumAnvilRequirement.INGOT, 80),
	WATERING_CAN("watering_can", 0.2F, EnumAnvilRequirement.SHEET, 140),
	GARDENING_SPADE("spade", 0.1F, EnumAnvilRequirement.SHEET, 140),
	SAW("saw", 0.5F, EnumAnvilRequirement.SHEET, 80),
    CHISEL("chisel", 0.1F, EnumAnvilRequirement.INGOT, 60);

	EnumToolClass(String s, float f, EnumAnvilRequirement ear, int i)
	{
		this.toolClass = s;
		this.weight = f;
		this.anvilMaterial = ear;
		this.progressReq = i;
	}
	
	private final String toolClass;
	private final float weight;
	private final EnumAnvilRequirement anvilMaterial;
	private final int progressReq;
	
	@Override
	public String getName()
	{
		return this.toolClass;
	}

    public float getWeight()
    {
        return weight;
    }

    public EnumAnvilRequirement getAnvilMaterial()
    {
        return anvilMaterial;
    }

    public int getProgressReq()
    {
        return progressReq;
    }
}
