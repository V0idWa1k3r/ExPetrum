package v0id.exp.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.ForgeRegistry;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
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
    }
}
