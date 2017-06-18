package v0id.exp.block.item;

import net.minecraft.block.Block;

public class ItemBlockWithMetadata extends ItemBlockWeighted
{
	public ItemBlockWithMetadata(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int damage)
    {
        return damage;
    }
}
