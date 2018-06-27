package v0id.core.util;

import net.minecraft.item.ItemStack;

public interface IColoredItem
{
	int getColor(ItemStack stk);
	
	void removeColor(ItemStack stk);
	
	boolean hasColor(ItemStack stk);

	void setColor(ItemStack stk, int color);
}
