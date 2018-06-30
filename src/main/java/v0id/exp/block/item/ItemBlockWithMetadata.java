package v0id.exp.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import v0id.api.exp.block.IHasSpecialName;

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

    public Block getBlock()
    {
        return this.block;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return this.block instanceof IHasSpecialName ? ((IHasSpecialName) this.block).getUnlocalizedName(stack) : super.getUnlocalizedName(stack);
    }
}
