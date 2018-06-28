package v0id.exp.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
import v0id.exp.item.ItemToolHead;

public class ExPRecipeRegistry extends AbstractRegistry
{
    public static ExPRecipeRegistry instance;
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
    }
}
