package v0id.exp.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.recipe.*;
import v0id.exp.item.ItemGeneric;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Wrappers
{
    public static class WrapperSmelting implements IRecipeWrapper
    {
        public final RecipesSmelting.IRecipeSmelting recipe;

        public WrapperSmelting(RecipesSmelting.IRecipeSmelting recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInputs(ItemStack.class, this.recipe.getInput(ItemStack.EMPTY));
            ingredients.setOutput(ItemStack.class, this.recipe.getOutput(ItemStack.EMPTY));
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
        {
            FontRenderer fr = minecraft.fontRenderer;
            fr.drawString(I18n.format("exp.txt.temperature", (int)this.recipe.getTemperature()), 34, 24, 0xffffff);
        }
    }

    public static class WrapperPottery implements IRecipeWrapper
    {
        public final RecipesPottery.RecipePottery recipe;

        public WrapperPottery(RecipesPottery.RecipePottery recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInput(ItemStack.class, new ItemStack(ExPItems.generic, 8, 0));
            ingredients.setOutput(ItemStack.class, this.recipe.getItem());
        }
    }

    public static class WrapperAnvilWeld implements IRecipeWrapper
    {
        public final RecipesAnvil.WeldingRecipe recipe;
        public final int temperaure;
        public final int tier;

        public WrapperAnvilWeld(RecipesAnvil.WeldingRecipe recipe)
        {
            this.recipe = recipe;
            this.temperaure = (int) Math.max(recipe.tItem1, recipe.tItem2);
            this.tier = recipe.anvilTier;
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInputs(ItemStack.class, Arrays.asList(this.recipe.item1, this.recipe.item2));
            ingredients.setOutput(ItemStack.class, this.recipe.itemOut);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
        {
            FontRenderer fr = minecraft.fontRenderer;
            fr.drawString(I18n.format("exp.txt.temperature", this.temperaure), 0, 20, 0xffffff);
            fr.drawString(I18n.format("exp.txt.anvilTier") + " " + I18n.format("exp.txt.anvil.tier." + this.tier), 0, 30, 0xffffff);
        }
    }

    public static class WrapperAnvil implements IRecipeWrapper
    {
        public final RecipesAnvil.IAnvilRecipe recipe;

        public WrapperAnvil(RecipesAnvil.IAnvilRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInput(ItemStack.class, this.recipe.getInput());
            ingredients.setOutput(ItemStack.class, this.recipe.getResult(ItemStack.EMPTY));
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
        {
            FontRenderer fr = minecraft.fontRenderer;
            fr.drawString(I18n.format("exp.txt.temperature", this.recipe.getRequiredTemperature()), 0, 20, 0xffffff);
            fr.drawString(I18n.format("exp.txt.progress", this.recipe.getProgressRequired(ItemStack.EMPTY)), 50, 20, 0xffffff);
            fr.drawString(I18n.format("exp.txt.anvilTier") + " " + I18n.format("exp.txt.anvil.tier." + this.recipe.getRequiredTier()), 0, 30, 0xffffff);
        }
    }

    public static class WrapperAlloy implements IRecipeWrapper
    {
        public final EnumMetal metal;

        public WrapperAlloy(EnumMetal metal)
        {
            this.metal = metal;
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setOutput(ItemStack.class, new ItemStack(ExPItems.ingot, 1, this.metal.ordinal()));
            List<ItemStack> ins = Lists.newArrayList();
            for (Pair<EnumMetal, Pair<Float, Float>> data : this.metal.getComposition().compositionData)
            {
                ins.add(new ItemStack(ExPItems.ingot, 1, data.getKey().ordinal()));
                for (EnumOre ore : EnumOre.values())
                {
                    if (ore.getMeltsInto() == data.getLeft())
                    {
                        ins.add(new ItemStack(ExPItems.ore, 1, ore.ordinal()));
                    }
                }
            }

            ingredients.setInputs(ItemStack.class, ins);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
        {
            FontRenderer fr = minecraft.fontRenderer;
            String name = I18n.format("exp.item.ingot.desc.type." + this.metal.getName());
            fr.drawStringWithShadow(name, 64 - fr.getStringWidth(name) / 2, 0, 0xffffff);
            int i = 0;
            for (Pair<EnumMetal, Pair<Float, Float>> data : this.metal.getComposition().compositionData)
            {
                fr.drawStringWithShadow(I18n.format("exp.item.ingot.desc.type." + data.getLeft().getName()) + ": " + (int)(data.getRight().getLeft() * 100) + "% - " + (int)(data.getRight().getRight() * 100) + "%", 4, 14 + i * 10, 0xffffff);
                ++i;
            }
        }
    }

    public static class WrapperQuern implements IRecipeWrapper
    {
        public final RecipesQuern.IRecipeQuern recipe;

        public WrapperQuern(RecipesQuern.IRecipeQuern recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInput(ItemStack.class, this.recipe.getIn());
            ingredients.setOutput(ItemStack.class, this.recipe.getOut(ItemStack.EMPTY));
        }
    }

    public static class WrapperScrapingRack implements IRecipeWrapper
    {
        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInput(ItemStack.class, new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.SOAKED_HIDE.ordinal()));
            ingredients.setOutput(ItemStack.class, new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.PREPARED_HIDE.ordinal()));
        }
    }

    public static class WrapperSpinningWheel implements IRecipeWrapper
    {
        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInput(ItemStack.class, new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.CARDED_WOOL.ordinal()));
            ingredients.setOutput(ItemStack.class, new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.WOOL_YARN.ordinal()));
        }
    }

    public static class WrapperBloomery implements IRecipeWrapper
    {
        public final EnumOre oreMeta;

        public WrapperBloomery(EnumOre oreMeta)
        {
            this.oreMeta = oreMeta;
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInput(ItemStack.class, new ItemStack(ExPItems.ore, 1, this.oreMeta.ordinal()));
            ingredients.setOutput(ItemStack.class, new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.IRON_BLOOM.ordinal()));
        }
    }

    public static class WrapperBarrel implements IRecipeWrapper
    {
        public final RecipesBarrel.IRecipeBarrel recipe;

        public WrapperBarrel(RecipesBarrel.IRecipeBarrel recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInput(ItemStack.class, this.recipe.getInput());
            ingredients.setInput(FluidStack.class, this.recipe.getInputFluid());
            ingredients.setOutput(ItemStack.class, this.recipe.getResult(ItemStack.EMPTY, null));
            ingredients.setOutput(FluidStack.class, this.recipe.getOutputFluid(ItemStack.EMPTY));
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
        {

        }
    }

    public static class WrapperPress implements IRecipeWrapper
    {
        public final RecipesPress.IRecipePress recipe;

        public WrapperPress(RecipesPress.IRecipePress recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients)
        {
            ingredients.setInput(ItemStack.class, this.recipe.getInput());
            ingredients.setOutput(FluidStack.class, this.recipe.getOutput(ItemStack.EMPTY));
            ingredients.setOutput(ItemStack.class, this.recipe.getOutput());
        }
    }

    public static class WrapperBlastFurnace implements IRecipeWrapper
    {
        public final RecipesBlastFurnace.IBlastFurnaceRecipe recipe;

        public WrapperBlastFurnace(RecipesBlastFurnace.IBlastFurnaceRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients iIngredients)
        {
            iIngredients.setInputLists(ItemStack.class, Arrays.stream(this.recipe.getInput()).map(Collections::singletonList).collect(Collectors.toList()));
            iIngredients.setOutput(ItemStack.class, new ItemStack(ExPItems.ingot, 1, this.recipe.getResult().ordinal()));
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
        {
            FontRenderer fr = minecraft.fontRenderer;
            fr.drawStringWithShadow(String.format("%.2f", this.recipe.getResultAmount() / 100), 108, 20, 0xffffff);
            fr.drawStringWithShadow(I18n.format("exp.txt.temperature", this.recipe.getTemperature()), 5, 20, 0xffffff);
            fr.drawStringWithShadow(I18n.format("exp.txt.ticks", this.recipe.getWorkRequired()), 5, 30, 0xffffff);
        }
    }
}
