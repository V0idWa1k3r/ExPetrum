package v0id.api.exp.item.food;

import net.minecraft.item.ItemStack;
import v0id.api.exp.player.FoodGroup;

import java.util.EnumMap;

@SuppressWarnings("SameReturnValue")
public interface IExPFood
{
	float getCalories(ItemStack stack);
	
	EnumMap<FoodGroup, Float> getFoodGroup(ItemStack stack);
	
	boolean skipHandlers(ItemStack stack);
}
