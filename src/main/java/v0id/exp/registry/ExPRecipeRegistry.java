package v0id.exp.registry;

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
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
import v0id.api.exp.recipe.RecipesPottery;
import v0id.api.exp.recipe.RecipesSmelting;
import v0id.exp.item.ItemGeneric;
import v0id.exp.item.ItemPottery;
import v0id.exp.item.ItemToolHead;

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
        for (EnumMetal metal : EnumMetal.values())
        {
            event.getRegistry().register(new ShapelessOreRecipe(mcloc, new ItemStack(ExPItems.ingot, 1, metal.ordinal()), new ItemStack(ExPItems.moldIngot, 1, 2 + metal.ordinal())).setRegistryName("exp:recipe_hardcoded_ingot_" + metal.name().toLowerCase()));
        }

        for (EnumToolClass toolClass : EnumToolClass.values())
        {
            event.getRegistry().register(new ShapelessOreRecipe(mcloc, ItemToolHead.createToolHead(toolClass, EnumToolStats.COPPER), new ItemStack(ExPItems.moldTool, 1, EnumToolClass.values().length * 2 + toolClass.ordinal())).setRegistryName("exp:recipe_hardcoded_toolhead_" + toolClass.name().toLowerCase()));
        }

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
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeOreSmelting(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.ASH.ordinal()), "stickWood", 200F));
        for (EnumToolClass tool : EnumToolClass.values())
        {
            RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPItems.moldTool, 1, tool.ordinal()), new ItemStack(ExPItems.moldTool, 1, tool.ordinal() + EnumToolClass.values().length), 540F));
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent evt)
    {
        RecipesSmelting.sort();
    }
}
