package v0id.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class ItemBlockWithMetadata extends ItemBlock
{
	public ItemBlockWithMetadata(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
		assert block.getRegistryName() != null : "Can't register ItemBlock for non-registered blocks!";
		this.setRegistryName(block.getRegistryName());
	}

	@Nonnull
    @Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return String.format("%s.%d", super.getUnlocalizedName(stack), stack.getMetadata());
	}
	
	@Override
	public int getMetadata(int damage)
    {
        return damage;
    }
}
