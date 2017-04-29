package v0id.api.exp.inventory;

import net.minecraft.item.ItemStack;

public interface IWeightProvider
{
	float provideWeight(ItemStack item);
	
	float provideVolume(ItemStack item);
}
