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
        private final ItemStack itemOut;

        public RecipePress(ItemStack itemIn, FluidStack fsOut, ItemStack itemOut)
        {
            this.itemIn = itemIn;
            this.fsOut = fsOut;
            this.itemOut = itemOut;
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

        @Override
        public ItemStack getOutput()
        {
            return this.itemOut;
        }
    }

    public interface IRecipePress
    {
        boolean matches(ItemStack is);

        FluidStack getOutput(ItemStack is);

        ItemStack getInput();

        ItemStack getOutput();
    }
}
