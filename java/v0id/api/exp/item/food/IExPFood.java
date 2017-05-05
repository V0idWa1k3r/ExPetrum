package v0id.api.exp.item.food;

import java.util.EnumMap;

import net.minecraft.item.ItemStack;
import v0id.api.exp.player.Nutrient;

public interface IExPFood
{
	float getCalories(ItemStack stack);
	
	EnumMap<Nutrient, Float> getNutrients(ItemStack stack);
}
