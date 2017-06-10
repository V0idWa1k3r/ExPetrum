package v0id.exp.world.gen;

import net.minecraft.util.WeightedRandom.Item;
import v0id.api.exp.block.EnumShrubType;

public class ShrubEntry extends Item
{
	private final EnumShrubType shrubType;
	
	public ShrubEntry(int itemWeightIn, EnumShrubType est)
	{
		super(itemWeightIn);
		this.shrubType = est;
	}

	public EnumShrubType getShrubType()
	{
		return this.shrubType;
	}

}
