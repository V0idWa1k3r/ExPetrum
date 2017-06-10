package v0id.api.exp.block.property;

import net.minecraft.util.IStringSerializable;

public enum EnumRockClass implements IStringSerializable
{
	//IGNEOUS		// 64.7%
		ANDESITE(6, 10),
		BASALT(7, 12),
		DACITE(5.5, 8),
		DIORITE(6.7, 16),
		GABBRO(5.3, 11),
		GRANITE(8, 16),
	//SEDIMENTARY	// 7.9%
		CHALK(3.5, 5),
		CHERT(4, 7),
		CLAYSTONE(2.7, 5),
		LIMESTONE(4.4, 9),
		SANDSTONE(4, 8),
	//METAMORPHIC	// 27.4%
		GNEISS(5.5, 12),
		MARBLE(5.2, 13),
		QUARTZITE(4.8, 11),
		SCHIST(5.9, 9),
		SLATE(6.2, 14);
	
	EnumRockClass(double f, double f1)
	{
		this.setHardness((float) f);
		this.setResistance((float) f1);
	}
	
	private float hardness;
	private float resistance;

	@Override
	public String getName()
	{
		return this.name().toLowerCase();
	}

	public float getHardness()
	{
		return this.hardness;
	}

	public void setHardness(float hardness)
	{
		this.hardness = hardness;
	}

	public float getResistance()
	{
		return this.resistance;
	}

	public void setResistance(float resistance)
	{
		this.resistance = resistance;
	}
}
