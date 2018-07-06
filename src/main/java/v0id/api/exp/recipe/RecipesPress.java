package v0id.api.exp.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class RecipesPress
{
    public static List<IRecipePress> allRecipes = Lists.newArrayList();

    public static void addRecipe(IRecipePress recipe)
    {
        allRecipes.add(recipe);
    }

    public static class RecipePress implements IRecipePress
    {
        private final ItemStack itemIn;
        private final FluidStack fsOut;

        public RecipePress(ItemStack itemIn, FluidStack fsOut)
        {
            this.itemIn = itemIn;
            this.fsOut = fsOut;
        }

        @Override
        public boolean matches(ItemStack is)
        {
            return is.isItemEqual(itemIn);
        }

        @Override
        public FluidStack getOutput(ItemStack is)
        {
            return this.fsOut.copy();
        }

        @Override
        public ItemStack getInput()
        {
            return this.itemIn;
        }
    }

    public interface IRecipePress
    {
        boolean matches(ItemStack is);

        FluidStack getOutput(ItemStack is);

        ItemStack getInput();
    }
}
