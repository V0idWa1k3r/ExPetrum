package v0id.api.exp.event.world.gen;

import net.minecraft.util.WeightedRandom.Item;
import v0id.api.exp.block.EnumOre;

public class OreEntry extends Item
{
	private EnumOre oreType;
	
	public OreEntry(EnumOre oreType, int itemWeightIn)
	{
		super(itemWeightIn);
		this.setOreType(oreType);
	}

	public EnumOre getOreType()
	{
		return this.oreType;
	}

	public void setOreType(EnumOre oreType)
	{
		this.oreType = oreType;
	}

}
