package v0id.api.exp.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import v0id.exp.util.temperature.TemperatureUtils;

import java.util.List;

public class RecipesAnvil
{
    public static List<IAnvilRecipe> allRecipes = Lists.newArrayList();
    public static List<WeldingRecipe> allWeldingRecipes = Lists.newArrayList();

    public static void addRecipe(IAnvilRecipe recipe)
    {
        allRecipes.add(recipe);
    }

    public static void addWeldingRecipe(WeldingRecipe recipe)
    {
        allWeldingRecipes.add(recipe);
    }

    public static void addWeldingRecipe(ItemStack item1, ItemStack item2, int t1, int t2, ItemStack out, int tier)
    {
        allWeldingRecipes.add(new WeldingRecipe(item1, item2, t1, t2, out, tier));
    }

    public static void addRecipe(ItemStack in, int t, ItemStack out, int progress, int tierReq)
    {
        allRecipes.add(new RecipeAnvil(in, out, t, progress, tierReq));
    }

    public static WeldingRecipe getWeldingRecipe(ItemStack item1, ItemStack item2)
    {
        for (WeldingRecipe rec : allWeldingRecipes)
        {
            if (rec.matches(item1, item2))
            {
                return rec;
            }
        }

        return null;
    }

    public static class WeldingRecipe
    {
        public final ItemStack item1;
        public final ItemStack item2;
        public final float tItem1;
        public final float tItem2;
        public final ItemStack itemOut;
        public final int anvilTier;

        public WeldingRecipe(ItemStack item1, ItemStack item2, float tItem1, float tItem2, ItemStack itemOut, int tier)
        {
            this.item1 = item1;
            this.item2 = item2;
            this.tItem1 = tItem1;
            this.tItem2 = tItem2;
            this.itemOut = itemOut;
            this.anvilTier = tier;
        }

        public boolean matches(ItemStack item1, ItemStack item2)
        {
            return item1.isItemEqual(this.item1) && item2.isItemEqual(this.item2) && TemperatureUtils.getTemperature(item1) >= this.tItem1 && TemperatureUtils.getTemperature(item2) >= this.tItem2;
        }
    }

    public static class RecipeAnvil extends Impl
    {
        private final ItemStack itemIn;
        private final ItemStack itemOut;
        private final int temperature;
        private final int progress;
        private final int tierReq;

        public RecipeAnvil(ItemStack itemIn, ItemStack itemOut, int temperature, int progress, int tierReq)
        {
            this.itemIn = itemIn;
            this.itemOut = itemOut;
            this.temperature = temperature;
            this.progress = progress;
            this.tierReq = tierReq;
        }

        @Override
        public boolean matchesForInterface(ItemStack in, int i)
        {
            return this.tierReq <= i && in.isItemEqual(this.itemIn);
        }

        @Override
        public boolean matches(ItemStack in, int temperature)
        {
            return in.isItemEqual(this.itemIn) && temperature >= this.temperature;
        }

        @Override
        public ItemStack getResult(ItemStack in)
        {
            ItemStack ret = this.itemOut.copy();
            TemperatureUtils.setTemperature(ret, TemperatureUtils.getTemperature(in));
            return ret;
        }

        @Override
        public ItemStack getInput()
        {
            return this.itemIn;
        }

        @Override
        public int getProgressRequired(ItemStack in)
        {
            return this.progress;
        }

        @Override
        public int getRequiredTier()
        {
            return this.tierReq;
        }

        @Override
        public int getRequiredTemperature()
        {
            return this.temperature;
        }
    }

    public static abstract class Impl implements IAnvilRecipe
    {
        private final int id;

        public Impl()
        {
            this.id = allRecipes.size();
        }

        @Override
        public int getID()
        {
            return this.id;
        }
    }

    public interface IAnvilRecipe
    {
        boolean matchesForInterface(ItemStack in, int anvilTier);

        boolean matches(ItemStack in, int temperature);

        ItemStack getResult(ItemStack in);

        ItemStack getInput();

        int getID();

        int getProgressRequired(ItemStack in);

        int getRequiredTier();

        int getRequiredTemperature();
    }
}
