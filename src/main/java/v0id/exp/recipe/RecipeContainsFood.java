package v0id.exp.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import v0id.exp.item.ItemFood;

import javax.annotation.Nonnull;

public class RecipeContainsFood extends ShapelessOreRecipe
{
    public RecipeContainsFood(NonNullList<Ingredient> input, @Nonnull ItemStack result)
    {
        super(null, input, result);
    }

    public RecipeContainsFood(@Nonnull ItemStack result, Object... recipe)
    {
        super(null, result, recipe);
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1)
    {
        ItemStack out = super.getCraftingResult(var1);
        ItemStack foodIS = ItemStack.EMPTY;
        for (int i = 0; i < var1.getWidth() * var1.getHeight(); ++i)
        {
            ItemStack is = var1.getStackInSlot(i);
            if (is.getItem() instanceof ItemFood)
            {
                foodIS = is;
                break;
            }
        }

        if (!foodIS.isEmpty())
        {
            ItemFood food = (ItemFood) foodIS.getItem();
            food.setTotalWeight(out, food.getTotalWeight(foodIS));
            food.setTotalRot(out, food.getTotalRot(foodIS));
            food.setItemRotMultiplier(out, food.getItemRotMultiplier(foodIS));
            food.setPreservationType(out, food.getPreservationType(foodIS));
            food.setLastTickTime(out, food.getLastTickTime(foodIS));
        }

        return out;
    }
}
