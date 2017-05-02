package v0id.exp.world.gen.tree;

import net.minecraft.util.WeightedRandom.Item;
import v0id.api.exp.block.EnumTreeType;

public class TreeEntry extends Item
{
	public final EnumTreeType treeType;
	
	public TreeEntry(int itemWeightIn, EnumTreeType tti)
	{
		super(itemWeightIn);
		this.treeType = tti;
	}

}
