package v0id.api.exp.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import v0id.exp.util.OreDictManager;
import v0id.exp.util.temperature.TemperatureUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class RecipesSmelting
{
    public static final List<IRecipeSmelting> allRecipes = Lists.newArrayList();

    public static void addRecipe(IRecipeSmelting recipe)
    {
        allRecipes.add(recipe);
    }

    public static void sort()
    {
        allRecipes.sort((l, r) -> -(int)(r.getTemperature() - l.getTemperature()));
    }

    public static void checkForSmelting(Consumer<ItemStack> itemStackSetter, ItemStack item, boolean shrinkStack, boolean replaceStack)
    {
        float temp = TemperatureUtils.getTemperature(item);
        for (IRecipeSmelting rec : allRecipes)
        {
            if (rec.matches(item, temp))
            {
                ItemStack is = rec.getOutput(item).copy();
                TemperatureUtils.setTemperature(is, temp);
                if (shrinkStack)
                {
                    item.shrink(1);
                }
                else
                {
                    if (replaceStack)
                    {
                        is.setCount(item.getCount());
                        itemStackSetter.accept(is);
                        return;
                    }
                }

                itemStackSetter.accept(rec.getOutput(item).copy());
                return;
            }
        }
    }

    public static class RecipeOreSmelting implements IRecipeSmelting
    {
        public ItemStack output;
        public String ore;
        public float temperature;

        public RecipeOreSmelting(ItemStack output, String ore, float temperature)
        {
            this.output = output;
            this.ore = ore;
            this.temperature = temperature;
        }

        @Override
        public boolean matches(ItemStack is, float temperature)
        {
            return temperature >= this.temperature && Arrays.stream(OreDictManager.getOreNames(is)).anyMatch(s -> s.equalsIgnoreCase(this.ore));
        }

        @Override
        public ItemStack getOutput(ItemStack is)
        {
            return this.output;
        }

        @Override
        public float getTemperature()
        {
            return this.temperature;
        }
    }

    public static class RecipeSmelting implements IRecipeSmelting
    {
        public ItemStack itemIn;
        public ItemStack itemOut;
        public float temperature;

        public RecipeSmelting(ItemStack itemIn, ItemStack itemOut, float temperature)
        {
            this.itemIn = itemIn;
            this.itemOut = itemOut;
            this.temperature = temperature;
        }

        @Override
        public boolean matches(ItemStack is, float temperature)
        {
            return temperature >= this.temperature && is.isItemEqual(this.itemIn);
        }

        @Override
        public ItemStack getOutput(ItemStack is)
        {
            return this.itemOut;
        }

        @Override
        public float getTemperature()
        {
            return this.temperature;
        }
    }

    public interface IRecipeSmelting
    {
        boolean matches(ItemStack is, float temperature);

        ItemStack getOutput(ItemStack is);

        float getTemperature();
    }
}
