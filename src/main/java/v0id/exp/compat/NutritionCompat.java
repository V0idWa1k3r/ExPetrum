package v0id.exp.compat;

import ca.wescook.nutrition.capabilities.CapabilityManager;
import ca.wescook.nutrition.capabilities.INutrientManager;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.item.food.FoodEntry;
import v0id.api.exp.player.EnumFoodGroup;
import v0id.core.logging.LogLevel;

import java.lang.reflect.Field;

public class NutritionCompat
{
    public static Field NUTRITION_CAPABILITY;

    static
    {
        try
        {
            NUTRITION_CAPABILITY = CapabilityManager.class.getDeclaredField("NUTRITION_CAPABILITY");
            NUTRITION_CAPABILITY.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            ExPMisc.modLogger.log(LogLevel.Error, "ExP was unable to reflectively access NUTRITION_CAPABILITY of Nutrition!", e);
        }
    }

    public static void eatFood(EntityPlayer player, FoodEntry entry, float calories)
    {
        float multiplier = Math.min(1, 1 - (0.15F * (entry.getNutrientData().size() - 1)));
        float calMultiplier = calories / 100;
        entry.getNutrientData().forEach((k, v) -> addNutrition(player, k, v * multiplier * calMultiplier));
    }

    public static void addNutrition(EntityPlayer player, EnumFoodGroup group, float value)
    {
        try
        {
            INutrientManager manager = player.getCapability((Capability<INutrientManager>) NUTRITION_CAPABILITY.get(null), null);
            Nutrient nutrient = NutrientList.get().stream().filter(n -> n.name.equalsIgnoreCase(group.name())).findFirst().get();
            manager.add(nutrient, value);
        }
        catch (Exception e)
        {
            ExPMisc.modLogger.log(LogLevel.Error, "ExP was unable to reflectively access NUTRITION_CAPABILITY of Nutrition!", e);
        }
    }
}
