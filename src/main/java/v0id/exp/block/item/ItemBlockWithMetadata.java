package v0id.exp.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockWithMetadata extends ItemBlockWeighted
{
	public ItemBlockWithMetadata(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack);
	}
	
	@Override
	public int getMetadata(int damage)
    {
        return damage;
    }
}
