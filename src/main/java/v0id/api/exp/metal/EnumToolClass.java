package v0id.api.exp.metal;

import net.minecraft.util.IStringSerializable;

public enum EnumToolClass implements IStringSerializable
{
	PICKAXE("pickaxe", 1.5F, EnumAnvilRequirement.INGOT, 60),
	AXE("axe", 1.2F, EnumAnvilRequirement.INGOT, 60),
	SHOVEL("spade", 1.4F, EnumAnvilRequirement.INGOT, 60),
	HOE("hoe", 1.0F, EnumAnvilRequirement.INGOT, 50),
	SWORD("sword", 1.0F, EnumAnvilRequirement.SHEET, 50),
	KNIFE("knife", 0.6F, EnumAnvilRequirement.INGOT, 20),
	SCYTHE("scythe", 1.2F, EnumAnvilRequirement.SHEET, 70),
	BATTLEAXE("axe", 1.5F, EnumAnvilRequirement.DOUBLE_SHEET, 80),
	HAMMER("hammer", 1.8F, EnumAnvilRequirement.DOUBLE_SHEET, 50),
	SPEAR("sword", 1.0F, EnumAnvilRequirement.INGOT, 40),
	WATERING_CAN("watering_can", 0.2F, EnumAnvilRequirement.SHEET, 70),
	GARDENING_SPADE("spade", 0.1F, EnumAnvilRequirement.INGOT, 40),
	SAW("saw", 0.5F, EnumAnvilRequirement.SHEET, 40),
    CHISEL("chisel", 0.1F, EnumAnvilRequirement.INGOT, 30);

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
