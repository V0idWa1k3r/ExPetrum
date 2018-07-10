package v0id.api.exp.recipe;

import com.google.common.collect.Lists;
import com.google.common.primitives.Booleans;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import v0id.api.exp.metal.EnumMetal;

import java.util.List;

public class RecipesBlastFurnace
{
    public static final List<IBlastFurnaceRecipe> allRecipes = Lists.newArrayList();

    public static void addRecipe(IBlastFurnaceRecipe recipe)
    {
        allRecipes.add(recipe);
    }

    public static class BlastFurnaceRecipe implements IBlastFurnaceRecipe
    {
        public final ItemStack[] input;
        public final EnumMetal output;
        public final float temp;
        public final int work;
        public final float resultQuantity;

        public BlastFurnaceRecipe(ItemStack[] input, EnumMetal output, float temp, int work, float resultQuantity)
        {
            this.input = input;
            this.output = output;
            this.temp = temp;
            this.work = work;
            this.resultQuantity = resultQuantity;
        }

        @Override
        public boolean matches(List<EntityItem> items)
        {
            if (items.size() != this.input.length)
            {
                return false;
            }

            boolean[] found = new boolean[this.input.length];
            for (EntityItem item : items)
            {
                ItemStack is = item.getItem();
                int i = 0;
                for (ItemStack in : this.input)
                {
                    if (is.isItemEqual(in) && is.getCount() >= in.getCount())
                    {
                        found[i] = true;
                        break;
                    }

                    ++i;
                }
            }

            return Booleans.asList(found).stream().allMatch(b -> b);
        }

        @Override
        public EnumMetal getResult()
        {
            return this.output;
        }

        @Override
        public float getResultAmount()
        {
            return this.resultQuantity;
        }

        @Override
        public ItemStack[] getInput()
        {
            return this.input;
        }

        @Override
        public float getTemperature()
        {
            return this.temp;
        }

        @Override
        public int getWorkRequired()
        {
            return this.work;
        }

        @Override
        public void consumeItems(List<EntityItem> items)
        {
            for (EntityItem item : items)
            {
                ItemStack is = item.getItem();
                for (ItemStack in : this.input)
                {
                    if (is.isItemEqual(in))
                    {
                        is.shrink(in.getCount());
                        if (is.getCount() <= 0)
                        {
                            item.setDead();
                        }

                        break;
                    }
                }
            }
        }
    }

    public interface IBlastFurnaceRecipe
    {
        boolean matches(List<EntityItem> items);

        EnumMetal getResult();

        float getResultAmount();

        ItemStack[] getInput();

        float getTemperature();

        int getWorkRequired();

        void consumeItems(List<EntityItem> items);
    }
}
