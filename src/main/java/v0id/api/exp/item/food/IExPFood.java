package v0id.api.exp.item.food;

import net.minecraft.item.ItemStack;
import v0id.api.exp.player.EnumFoodGroup;

import java.util.EnumMap;

@SuppressWarnings("SameReturnValue")
public interface IExPFood
{
	float getCalories(ItemStack stack);
	
	EnumMap<EnumFoodGroup, Float> getFoodGroup(ItemStack stack);
	
	boolean skipHandlers(ItemStack stack);
}
