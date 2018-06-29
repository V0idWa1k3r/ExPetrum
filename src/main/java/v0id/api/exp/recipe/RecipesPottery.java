package v0id.api.exp.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class RecipesPottery
{
    public static List<RecipePottery> allRecipes = Lists.newArrayList();

    public static void addRecipe(RecipePottery recipe)
    {
        allRecipes.add(recipe);
    }

    public static void addRecipe(ItemStack output, int divider, ResourceLocation guiTexture)
    {
        addRecipe(new RecipePottery(output, divider, guiTexture));
    }

    public static class RecipePottery
    {
        private final ItemStack item;
        private final int divider;
        private final ResourceLocation guiTexture;

        public RecipePottery(ItemStack item, int divider, ResourceLocation guiTexture)
        {
            this.item = item;
            this.divider = divider;
            this.guiTexture = guiTexture;
        }

        public ItemStack getItem()
        {
            return item;
        }

        public int getDivider()
        {
            return divider;
        }

        public ResourceLocation getGuiTexture()
        {
            return guiTexture;
        }
    }
}
