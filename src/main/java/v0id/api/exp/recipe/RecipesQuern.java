package v0id.api.exp.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipesQuern
{
    public static final List<IRecipeQuern> allRecipes = Lists.newArrayList();

    public static void addRecipe(IRecipeQuern recipe)
    {
        allRecipes.add(recipe);
    }

    public static void addRecipe(ItemStack in, ItemStack out)
    {
        addRecipe(new RecipeQuern(in, out));
    }

    public static IRecipeQuern getRecipe(ItemStack in)
    {
        for (IRecipeQuern rec : allRecipes)
        {
            if (rec.matches(in))
            {
                return rec;
            }
        }

        return null;
    }

    public interface IRecipeQuern
    {
        boolean matches(ItemStack in);

        ItemStack getOut(ItemStack in);
    }

    public static class RecipeQuern implements IRecipeQuern
    {
        public final ItemStack itemIn;
        public final ItemStack itemOut;

        public RecipeQuern(ItemStack itemIn, ItemStack itemOut)
        {
            this.itemIn = itemIn;
            this.itemOut = itemOut;
        }

        @Override
        public boolean matches(ItemStack in)
        {
            return in.isItemEqual(this.itemIn);
        }

        @Override
        public ItemStack getOut(ItemStack in)
        {
            return this.itemOut;
        }
    }
}
