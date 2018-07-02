package v0id.exp.registry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.ForgeRegistry;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.item.IMeltableMetal;
import v0id.api.exp.metal.EnumAnvilRequirement;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
import v0id.api.exp.recipe.RecipesAnvil;
import v0id.api.exp.recipe.RecipesPottery;
import v0id.api.exp.recipe.RecipesQuern;
import v0id.api.exp.recipe.RecipesSmelting;
import v0id.exp.item.ItemGeneric;
import v0id.exp.item.ItemPottery;
import v0id.exp.item.ItemToolHead;
import v0id.exp.recipe.RecipeMold;
import v0id.exp.recipe.RecipePlanks;

public class ExPRecipeRegistry extends AbstractRegistry
{
    public static ExPRecipeRegistry instance;

    @GameRegistry.ObjectHolder("chiselsandbits:chisel_stone")
    private static final Item chiselStone = null;

    @GameRegistry.ObjectHolder("chiselsandbits:chisel_iron")
    private static final Item chiselIron = null;

    public ExPRecipeRegistry()
    {
        super();
        instance = this;
    }

    public void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        final ResourceLocation mcloc = new ResourceLocation("minecraft:misc");
        event.getRegistry().register(new RecipeMold().setRegistryName("exp:recipe_hardcoded_mold"));
        event.getRegistry().register(new RecipePlanks().setRegistryName("exp:recipe_hardcoded_planks"));
        ForgeRegistry<IRecipe> reg = (ForgeRegistry<IRecipe>) event.getRegistry();
        if (Loader.isModLoaded("chiselsandbits"))
        {
            ResourceLocation[] toRemove = new ResourceLocation[]{ new ResourceLocation("chiselsandbits", "chisel_stone"), new ResourceLocation("chiselsandbits", "chisel_iron"), new ResourceLocation("chiselsandbits", "chisel_gold"), new ResourceLocation("chiselsandbits", "chisel_diamond") };
            for (ResourceLocation loc : toRemove)
            {
                reg.remove(loc);
            }

            event.getRegistry().register(new ShapelessOreRecipe(mcloc, new ItemStack(chiselStone, 1, 0), new ItemStack(ExPItems.chisel, 1, 0)).setRegistryName("exp:recipe_hardcoded_compat_cnb_chisel_stone"));
            event.getRegistry().register(new ShapelessOreRecipe(mcloc, new ItemStack(chiselIron, 1, 0), new ItemStack(ExPItems.chisel, 1, 7)).setRegistryName("exp:recipe_hardcoded_compat_cnb_chisel_iron"));
        }

        String[] toRemove = new String[]{ "minecraft:torch", "minecraft:crafting_table", "minecraft:chest" };
        for (String loc : toRemove)
        {
            reg.remove(new ResourceLocation(loc));
        }

        RecipesPottery.addRecipe(new ItemStack(ExPItems.pottery, 1, ItemPottery.EnumPotteryType.CLAY_POT.ordinal()), 3, new ResourceLocation("exp", "textures/items/pottery/clay_pot.png"));
        RecipesPottery.addRecipe(new ItemStack(ExPItems.pottery, 1, ItemPottery.EnumPotteryType.CLAY_BOWL.ordinal()), 3, new ResourceLocation("exp", "textures/items/pottery/clay_bowl.png"));
        RecipesPottery.addRecipe(new ItemStack(ExPItems.pottery, 1, ItemPottery.EnumPotteryType.CLAY_JUG.ordinal()), 3, new ResourceLocation("exp", "textures/items/pottery/clay_jug.png"));
        RecipesPottery.addRecipe(new ItemStack(ExPItems.moldIngot, 1, 0), 4, new ResourceLocation("exp", "textures/items/molds/clay_mold_ingot.png"));
        for (EnumToolClass tool : EnumToolClass.values())
        {
            RecipesPottery.addRecipe(new ItemStack(ExPItems.moldTool, 1, tool.ordinal()), 4, new ResourceLocation("exp", "textures/items/molds/clay_mold_" + tool.name().toLowerCase() + ".png"));
        }

        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPItems.pottery, 1, ItemPottery.EnumPotteryType.CLAY_POT.ordinal()), new ItemStack(ExPItems.pottery, 1, ItemPottery.EnumPotteryType.CERAMIC_POT.ordinal()), 540F));
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPItems.pottery, 1, ItemPottery.EnumPotteryType.CLAY_JUG.ordinal()), new ItemStack(ExPItems.pottery, 1, ItemPottery.EnumPotteryType.CERAMIC_JUG.ordinal()), 540F));
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPItems.pottery, 1, ItemPottery.EnumPotteryType.CLAY_BOWL.ordinal()), new ItemStack(ExPItems.pottery, 1, ItemPottery.EnumPotteryType.CERAMIC_BOWL.ordinal()), 540F));
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPItems.moldIngot, 1, 0), new ItemStack(ExPItems.moldIngot, 1, 1), 540F));
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.CLAY.ordinal()), new ItemStack(Items.BRICK, 1, 0), 540F));
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.KAOLIN.ordinal()), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.KAOLIN_BRICK.ordinal()), 570F));
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.FIRE_CLAY.ordinal()), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.FIRE_BRICK.ordinal()), 600F));
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeOreSmelting(new ItemStack(Blocks.TORCH, 1, 0), "stickWood", 200F));
        RecipesSmelting.addRecipe(new RecipeSmeltingMeltable());
        for (EnumToolClass tool : EnumToolClass.values())
        {
            RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPItems.moldTool, 1, tool.ordinal()), new ItemStack(ExPItems.moldTool, 1, tool.ordinal() + EnumToolClass.values().length), 540F));
        }

        RecipesQuern.addRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.KAOLIN.ordinal()), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.KAOLIN_POWDER.ordinal()));
        RecipesQuern.addRecipe(new ItemStack(Items.FLINT, 1, 0), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.FLINT_POWDER.ordinal()));
        RecipesQuern.addRecipe(new ItemStack(ExPItems.ore, 1, EnumOre.CINNABAR.ordinal()), new ItemStack(Items.REDSTONE, 8, 0));
        RecipesQuern.addRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.CHARCOAL.ordinal()), new ItemStack(ExPItems.generic, 8, ItemGeneric.EnumGenericType.FLUX.ordinal()));
        for (EnumMetal metal : EnumMetal.values())
        {
            RecipesAnvil.addWeldingRecipe(new ItemStack(ExPItems.ingot, 1, metal.ordinal()), new ItemStack(ExPItems.ingot, 1, metal.ordinal()), (int)(metal.getMeltingTemperature() * 0.85F), (int)(metal.getMeltingTemperature() * 0.85F), new ItemStack(ExPItems.metalGeneric, 1, metal.ordinal()), metal.getRequiredAnvilTier() - 1);
            RecipesAnvil.addRecipe(new ItemStack(ExPItems.ingot, 1, metal.ordinal()), (int)(metal.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.metalGeneric, 1, metal.ordinal() + EnumMetal.values().length), 30, metal.getRequiredAnvilTier());
            RecipesAnvil.addRecipe(new ItemStack(ExPItems.metalGeneric, 1, metal.ordinal()), (int)(metal.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.metalGeneric, 1, metal.ordinal() + EnumMetal.values().length * 2), 40, metal.getRequiredAnvilTier());
        }

        for (EnumToolStats material : EnumToolStats.values())
        {
            if (material == EnumToolStats.STONE)
            {
                continue;
            }

            for (EnumToolClass toolClass : EnumToolClass.values())
            {
                ItemStack head = ItemToolHead.createToolHead(toolClass, material);
                Item itemMaterial = toolClass.getAnvilMaterial() == EnumAnvilRequirement.INGOT ? ExPItems.ingot : ExPItems.metalGeneric;
                int metaMaterial = material.getMaterial().ordinal() + EnumMetal.values().length * (toolClass.getAnvilMaterial().ordinal());
                RecipesAnvil.addRecipe(new ItemStack(itemMaterial, 1, metaMaterial), (int)(material.getMaterial().getMeltingTemperature() * 0.75F), head, toolClass.getProgressReq(), material.getMaterial().getRequiredAnvilTier());
            }
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent evt)
    {
        RecipesSmelting.sort();
    }

    private static class RecipeSmeltingMeltable implements RecipesSmelting.IRecipeSmelting
    {
        @Override
        public boolean matches(ItemStack is, float temperature)
        {
            return is.getItem() instanceof IMeltableMetal && temperature >= ((IMeltableMetal) is.getItem()).getMeltingTemperature(is);

        }

        @Override
        public ItemStack getOutput()
        {
            return ItemStack.EMPTY;
        }

        @Override
        public float getTemperature()
        {
            return 0;
        }
    }
}
