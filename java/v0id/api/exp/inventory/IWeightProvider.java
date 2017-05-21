package v0id.api.exp.inventory;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;

public interface IWeightProvider
{
	float provideWeight(ItemStack item);
	
	Pair<Byte, Byte> provideVolume(ItemStack item);
}
