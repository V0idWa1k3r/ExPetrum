package v0id.exp.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.ExPTextures;

import java.util.Collections;
import java.util.stream.Collectors;

public class Categories
{
    public static class CategorySmelting implements IRecipeCategory<Wrappers.WrapperSmelting>
    {
        private final IDrawable drawable;

        public CategorySmelting(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 0, 0, 54, 36);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategorySmelting).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.smelting");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperSmelting recipeWrapper, IIngredients ingredients)
        {
            recipeLayout.getItemStacks().init(0, true, 0, 0);
            recipeLayout.getItemStacks().init(1, false, 36, 0);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }
    }

    public static class CategoryPottery implements IRecipeCategory<Wrappers.WrapperPottery>
    {
        private final IDrawable drawable;

        public CategoryPottery(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 0, 0, 54, 18);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryPottery).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.pottery");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperPottery recipeWrapper, IIngredients ingredients)
        {
            recipeLayout.getItemStacks().init(0, true, 0, 0);
            recipeLayout.getItemStacks().init(1, false, 36, 0);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }
    }

    public static class CategoryAnvilWeld implements IRecipeCategory<Wrappers.WrapperAnvilWeld>
    {
        private final IDrawable drawable;

        public CategoryAnvilWeld(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 0, 36, 108, 36);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryAnvilWeld).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.anvilWeld");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperAnvilWeld recipeWrapper, IIngredients ingredients)
        {
            recipeLayout.getItemStacks().init(0, true, 0, 0);
            recipeLayout.getItemStacks().init(1, true, 36, 0);
            recipeLayout.getItemStacks().init(2, false, 72, 0);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(1, ingredients.getInputs(ItemStack.class).get(1));
            recipeLayout.getItemStacks().set(2, ingredients.getOutputs(ItemStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }
    }

    public static class CategoryAnvil implements IRecipeCategory<Wrappers.WrapperAnvil>
    {
        private final IDrawable drawable;

        public CategoryAnvil(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 0, 72, 108, 36);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryAnvil).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.anvil");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperAnvil recipeWrapper, IIngredients ingredients)
        {
            recipeLayout.getItemStacks().init(0, true, 27, 0);
            recipeLayout.getItemStacks().init(1, false, 63, 0);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }
    }

    public static class CategoryAlloying implements IRecipeCategory<Wrappers.WrapperAlloy>
    {
        private final IDrawable drawable;

        public CategoryAlloying(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 128, 0, 128, 80);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryAlloying).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.alloying");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperAlloy recipeWrapper, IIngredients ingredients)
        {
        }
    }

    public static class CategoryQuern implements IRecipeCategory<Wrappers.WrapperQuern>
    {
        private final IDrawable drawable;

        public CategoryQuern(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 0, 0, 54, 18);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryQuern).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.quern");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperQuern recipeWrapper, IIngredients ingredients)
        {
            recipeLayout.getItemStacks().init(0, true, 0, 0);
            recipeLayout.getItemStacks().init(1, false, 36, 0);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }
    }

    public static class CategoryScrapingRack implements IRecipeCategory<Wrappers.WrapperScrapingRack>
    {
        private final IDrawable drawable;

        public CategoryScrapingRack(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 0, 0, 54, 18);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.blockScrapingRack).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.scrapingRack");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperScrapingRack recipeWrapper, IIngredients ingredients)
        {
            recipeLayout.getItemStacks().init(0, true, 0, 0);
            recipeLayout.getItemStacks().init(1, false, 36, 0);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }
    }

    public static class CategorySpinningWheel implements IRecipeCategory<Wrappers.WrapperSpinningWheel>
    {
        private final IDrawable drawable;

        public CategorySpinningWheel(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 0, 0, 54, 18);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategorySpinningWheel).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.spinningWheel");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperSpinningWheel recipeWrapper, IIngredients ingredients)
        {
            recipeLayout.getItemStacks().init(0, true, 0, 0);
            recipeLayout.getItemStacks().init(1, false, 36, 0);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }
    }

    public static class CategoryBloomery implements IRecipeCategory<Wrappers.WrapperBloomery>
    {
        private final IDrawable drawable;

        public CategoryBloomery(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 0, 0, 54, 36);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryBloomery).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.bloomery");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperBloomery recipeWrapper, IIngredients ingredients)
        {
            recipeLayout.getItemStacks().init(0, true, 0, 0);
            recipeLayout.getItemStacks().init(1, false, 36, 0);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }
    }

    public static class CategoryBarrel implements IRecipeCategory<Wrappers.WrapperBarrel>
    {
        private final IDrawable drawable;

        public CategoryBarrel(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 0, 108, 90, 36);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryBarrel).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.barrel");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperBarrel recipeWrapper, IIngredients ingredients)
        {
            recipeLayout.getItemStacks().init(0, true, 18, 0);
            recipeLayout.getItemStacks().init(1, false, 54, 0);
            recipeLayout.getFluidStacks().init(0, true, 5, 1, 8, 16, 10000, true, null);
            recipeLayout.getFluidStacks().init(1, false, 77, 1, 8, 16, 10000, true, null);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).size() == 0 ? Collections.singletonList(ItemStack.EMPTY) : ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).size() == 0 || ingredients.getOutputs(ItemStack.class).get(0).size() == 0 ? Collections.singletonList(ItemStack.EMPTY) : ingredients.getOutputs(ItemStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
            recipeLayout.getFluidStacks().set(0, ingredients.getInputs(FluidStack.class).get(0));
            recipeLayout.getFluidStacks().set(1, ingredients.getOutputs(FluidStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }
    }

    public static class CategoryPress implements IRecipeCategory<Wrappers.WrapperPress>
    {
        private final IDrawable drawable;

        public CategoryPress(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 0, 180, 54, 18);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryPress).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.press");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperPress recipeWrapper, IIngredients ingredients)
        {
            recipeLayout.getItemStacks().init(0, true, 0, 0);
            recipeLayout.getFluidStacks().init(0, false, 41, 1, 8, 16, 10000, true, null);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getFluidStacks().set(0, ingredients.getOutputs(FluidStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }
    }

    public static class CategoryBlastFurnace implements IRecipeCategory<Wrappers.WrapperBlastFurnace>
    {
        private final IDrawable drawable;

        public CategoryBlastFurnace(IGuiHelper helper)
        {
            this.drawable = helper.createDrawable(ExPTextures.guiJEI, 0, 144, 126, 36);
        }

        @Override
        public String getUid()
        {
            return ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryBlastFurnace).toString();
        }

        @Override
        public String getTitle()
        {
            return I18n.format("exp.jei.category.blast_furnace");
        }

        @Override
        public String getModName()
        {
            return ExPRegistryNames.modid;
        }

        @Override
        public IDrawable getBackground()
        {
            return this.drawable;
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, Wrappers.WrapperBlastFurnace recipeWrapper, IIngredients ingredients)
        {
            recipeLayout.getItemStacks().init(0, true, 0, 0);
            recipeLayout.getItemStacks().init(1, true, 36, 0);
            recipeLayout.getItemStacks().init(2, true, 72, 0);
            recipeLayout.getItemStacks().init(3, false, 108, 0);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
            if (ingredients.getInputs(ItemStack.class).size() > 1)
            {
                recipeLayout.getItemStacks().set(1, ingredients.getInputs(ItemStack.class).get(1));
                if (ingredients.getInputs(ItemStack.class).size() > 2)
                {
                    recipeLayout.getItemStacks().set(2, ingredients.getInputs(ItemStack.class).get(2));
                }
            }

            recipeLayout.getItemStacks().set(3, ingredients.getOutputs(ItemStack.class).stream().map(l -> l.get(0)).collect(Collectors.toList()));
        }
    }
}
