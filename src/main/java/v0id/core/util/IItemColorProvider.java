package v0id.core.util;

import net.minecraft.item.ItemStack;

public interface IItemColorProvider
{
	int getColor(ItemStack stk, int layer);
}
