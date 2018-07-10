package v0id.exp.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import v0id.api.exp.block.EnumAnvilMaterial;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.recipe.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@JEIPlugin
public class ExPPlugin implements IModPlugin
{
    @Override
    public void registerIngredients(IModIngredientRegistration registry)
    {

    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        registry.addRecipeCategories(
                new Categories.CategorySmelting(registry.getJeiHelpers().getGuiHelper()),
                new Categories.CategoryPottery(registry.getJeiHelpers().getGuiHelper()),
                new Categories.CategoryAnvilWeld(registry.getJeiHelpers().getGuiHelper()),
                new Categories.CategoryAnvil(registry.getJeiHelpers().getGuiHelper()),
                new Categories.CategoryAlloying(registry.getJeiHelpers().getGuiHelper()),
                new Categories.CategoryQuern(registry.getJeiHelpers().getGuiHelper()),
                new Categories.CategoryScrapingRack(registry.getJeiHelpers().getGuiHelper()),
                new Categories.CategorySpinningWheel(registry.getJeiHelpers().getGuiHelper()),
                new Categories.CategoryBloomery(registry.getJeiHelpers().getGuiHelper()),
                new Categories.CategoryBarrel(registry.getJeiHelpers().getGuiHelper()),
                new Categories.CategoryPress(registry.getJeiHelpers().getGuiHelper()),
                new Categories.CategoryBlastFurnace(registry.getJeiHelpers().getGuiHelper())
            );
    }

    @Override
    public void register(IModRegistry registry)
    {
        registry.addRecipes(RecipesSmelting.allRecipes.stream().map(Wrappers.WrapperSmelting::new).collect(Collectors.toList()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategorySmelting).toString());
        registry.addRecipes(RecipesPottery.allRecipes.stream().map(Wrappers.WrapperPottery::new).collect(Collectors.toList()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryPottery).toString());
        registry.addRecipes(RecipesAnvil.allWeldingRecipes.stream().map(Wrappers.WrapperAnvilWeld::new).collect(Collectors.toList()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryAnvilWeld).toString());
        registry.addRecipes(RecipesAnvil.allRecipes.stream().map(Wrappers.WrapperAnvil::new).collect(Collectors.toList()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryAnvil).toString());
        registry.addRecipes(RecipesQuern.allRecipes.stream().map(Wrappers.WrapperQuern::new).collect(Collectors.toList()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryQuern).toString());
        registry.addRecipes(Arrays.stream(EnumMetal.values()).filter(metal -> metal.getComposition() != null).map(Wrappers.WrapperAlloy::new).collect(Collectors.toList()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryAlloying).toString());
        registry.addRecipes(Collections.singleton(new Wrappers.WrapperScrapingRack()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryScrapingRack).toString());
        registry.addRecipes(Collections.singleton(new Wrappers.WrapperSpinningWheel()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategorySpinningWheel).toString());
        registry.addRecipes(Arrays.asList(EnumOre.HEMATITE, EnumOre.MAGNETITE, EnumOre.PENTLANDITE).stream().map(Wrappers.WrapperBloomery::new).collect(Collectors.toList()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryBloomery).toString());
        registry.addRecipes(RecipesBarrel.allRecipes.stream().map(Wrappers.WrapperBarrel::new).collect(Collectors.toList()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryBarrel).toString());
        registry.addRecipes(RecipesPress.allRecipes.stream().map(Wrappers.WrapperPress::new).collect(Collectors.toList()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryPress).toString());
        registry.addRecipes(RecipesBlastFurnace.allRecipes.stream().map(Wrappers.WrapperBlastFurnace::new).collect(Collectors.toList()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryBlastFurnace).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.campfire, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategorySmelting).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.forge, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategorySmelting).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.potteryStation, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryPottery).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.mechanicalPotteryStation, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryPottery).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.crucible, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryAlloying).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.quern, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryQuern).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.mechanicalQuern, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryQuern).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.scrapingRack, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryScrapingRack).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.spinningWheel, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategorySpinningWheel).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.bloomery, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryBloomery).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.barrel, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryBarrel).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.fruitPress, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryPress).toString());
        registry.addRecipeCatalyst(new ItemStack(ExPBlocks.blastFurnace, 1, 0), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryBlastFurnace).toString());
        for (EnumAnvilMaterial tier : EnumAnvilMaterial.values())
        {
            registry.addRecipeCatalyst(new ItemStack(ExPBlocks.anvil, 1, tier.ordinal()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryAnvilWeld).toString());
            registry.addRecipeCatalyst(new ItemStack(ExPBlocks.anvil, 1, tier.ordinal()), ExPRegistryNames.asLocation(ExPRegistryNames.jeiCategoryAnvil).toString());
        }
    }
}
