package v0id.exp.registry;

import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.item.EnumArmorStats;
import v0id.api.exp.item.IMeltableMetal;
import v0id.api.exp.item.food.FoodEntry;
import v0id.api.exp.metal.EnumAnvilRequirement;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
import v0id.api.exp.recipe.*;
import v0id.exp.item.*;
import v0id.exp.item.tool.IExPTool;
import v0id.exp.recipe.RecipeFoodCombine;
import v0id.exp.recipe.RecipeMold;

import java.util.Collections;
import java.util.List;

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
            event.getRegistry().register(new RecipeMold(new ItemStack(ExPItems.ingot, 1, metal.ordinal()), new ItemStack(ExPItems.moldIngot, 1, 2 + metal.ordinal())).setRegistryName("exp:recipe_hardcoded_mold_ingot_" + metal.getName()));
        }

        for (EnumToolClass tool : EnumToolClass.values())
        {
            event.getRegistry().register(new RecipeMold(ItemToolHead.createToolHead(tool, EnumToolStats.COPPER), new ItemStack(ExPItems.moldTool, 1, ItemMold.EnumMoldType.FULL.ordinal() * EnumToolClass.values().length + tool.ordinal())).setRegistryName("exp:recipe_hardcoded_mold_toolhead_" + tool.name().toLowerCase()));
        }

        event.getRegistry().register(new RecipeFoodCombine().setRegistryName("exp:recipe_hardcoded_food"));
        IForgeRegistryModifiable<IRecipe> reg = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();
        if (Loader.isModLoaded("chiselsandbits"))
        {
            ResourceLocation[] toRemove = new ResourceLocation[]{ new ResourceLocation("chiselsandbits", "chisel_stone"), new ResourceLocation("chiselsandbits", "chisel_iron"), new ResourceLocation("chiselsandbits", "chisel_gold"), new ResourceLocation("chiselsandbits", "chisel_diamond") };
            for (ResourceLocation loc : toRemove)
            {
                reg.remove(loc);
            }

            event.getRegistry().register(new ShapelessOreRecipe(mcloc, new ItemStack(chiselStone, 1, 0), new ItemStack(IExPTool.allTools.get(Pair.of(EnumToolClass.CHISEL, EnumToolStats.STONE)), 1, 0)).setRegistryName("exp:recipe_hardcoded_compat_cnb_chisel_stone"));
            event.getRegistry().register(new ShapelessOreRecipe(mcloc, new ItemStack(chiselIron, 1, 0), new ItemStack(IExPTool.allTools.get(Pair.of(EnumToolClass.CHISEL, EnumToolStats.IRON)), 1, 7)).setRegistryName("exp:recipe_hardcoded_compat_cnb_chisel_iron"));
        }

        String[] toRemove = new String[]{ "minecraft:torch", "minecraft:crafting_table", "minecraft:chest", "minecraft:bone_meal_from_bone", "minecraft:string_to_wool", "minecraft:fishing_rod", "minecraft:bow", "minecraft:leather_helmet", "minecraft:leather_chestplate", "minecraft:leather_leggings", "minecraft:leather_boots", "minecraft:iron_block", "minecraft:gold_block", "minecraft:flint_and_steel", "minecraft:iron_helmet", "minecraft:iron_chestplate", "minecraft:iron_leggings", "minecraft:iron_boots" };
        ExPMisc.modLogger.info("A fair warning.");
        ExPMisc.modLogger.info("Forge is about to spew a bunch of \"Dangerous alternative prefix\" warnings.");
        ExPMisc.modLogger.info("No, ExPetrum isn't broken.");
        ExPMisc.modLogger.info("It simply overrides vanilla recipes to a dummy implementation to effectively remove them from the game.");
        ExPMisc.modLogger.info("Thanks for reading. Have a nice day.");
        for (String loc : toRemove)
        {
            reg.remove(new ResourceLocation(loc));
            reg.register(new DummyRecipe().setRegistryName(loc));
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
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(Items.PAPER), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.ASH.ordinal()), 120F));
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.PRESSED_WOOD_PULP.ordinal()), new ItemStack(Items.PAPER), 100F));
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(Items.BONE), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.CHARRED_BONE.ordinal()), 1400F));
        RecipesSmelting.addRecipe(new RecipesSmelting.RecipeOreSmelting(new ItemStack(Blocks.TORCH, 1, 0), "stickWood", 200F));
        RecipesSmelting.addRecipe(new RecipeSmeltingMeltable());
        RecipesSmelting.addRecipe(new RecipeSmeltingFood(FoodEntry.CHICKEN_RAW, FoodEntry.CHICKEN_COOKED, 200));
        RecipesSmelting.addRecipe(new RecipeSmeltingFood(FoodEntry.BEEF_RAW, FoodEntry.BEEF_COOKED, 200));
        RecipesSmelting.addRecipe(new RecipeSmeltingFood(FoodEntry.LAMB_RAW, FoodEntry.LAMB_COOKED, 200));
        RecipesSmelting.addRecipe(new RecipeSmeltingFood(FoodEntry.PORK_RAW, FoodEntry.PORK_COOKED, 200));
        for (EnumToolClass tool : EnumToolClass.values())
        {
            RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPItems.moldTool, 1, tool.ordinal()), new ItemStack(ExPItems.moldTool, 1, tool.ordinal() + EnumToolClass.values().length), 540F));
        }

        for (EnumRockClass rock : EnumRockClass.values())
        {
            RecipesSmelting.addRecipe(new RecipesSmelting.RecipeSmelting(new ItemStack(ExPBlocks.sand, 1, rock.ordinal()), new ItemStack(Blocks.GLASS), 600F));
        }

        RecipesQuern.addRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.KAOLIN.ordinal()), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.KAOLIN_POWDER.ordinal()));
        RecipesQuern.addRecipe(new ItemStack(Items.FLINT, 1, 0), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.FLINT_POWDER.ordinal()));
        RecipesQuern.addRecipe(new ItemStack(ExPItems.ore, 1, EnumOre.CINNABAR.ordinal()), new ItemStack(Items.REDSTONE, 8, 0));
        RecipesQuern.addRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.CHARCOAL.ordinal()), new ItemStack(ExPItems.generic, 8, ItemGeneric.EnumGenericType.FLUX.ordinal()));
        RecipesQuern.addRecipe(new ItemStack(Items.BONE, 1, 0), new ItemStack(Items.DYE, 4, 15));
        RecipesQuern.addRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.CHARRED_BONE.ordinal()), new ItemStack(ExPItems.generic, 2, ItemGeneric.EnumGenericType.BONE_ASH.ordinal()));
        RecipesQuern.addRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.ROCK_SALT.ordinal()), new ItemStack(ExPItems.generic, 2, ItemGeneric.EnumGenericType.SALT.ordinal()));
        for (EnumRockClass rock : EnumRockClass.values())
        {
            RecipesQuern.addRecipe(new ItemStack(ExPBlocks.rock, 1, rock.ordinal()), new ItemStack(ExPBlocks.sand, 1, rock.ordinal()));
        }

        for (EnumMetal metal : EnumMetal.values())
        {
            RecipesAnvil.addWeldingRecipe(new ItemStack(ExPItems.ingot, 1, metal.ordinal()), new ItemStack(ExPItems.ingot, 1, metal.ordinal()), (int)(metal.getMeltingTemperature() * 0.85F), (int)(metal.getMeltingTemperature() * 0.85F), new ItemStack(ExPItems.metalGeneric, 1, metal.ordinal()), metal.getRequiredAnvilTier() - 1);
            RecipesAnvil.addRecipe(new ItemStack(ExPItems.ingot, 1, metal.ordinal()), (int)(metal.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.metalGeneric, 1, metal.ordinal() + EnumMetal.values().length), 30, metal.getRequiredAnvilTier());
            RecipesAnvil.addRecipe(new ItemStack(ExPItems.metalGeneric, 1, metal.ordinal()), (int)(metal.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.metalGeneric, 1, metal.ordinal() + EnumMetal.values().length * 2), 40, metal.getRequiredAnvilTier());
        }

        RecipesAnvil.addWeldingRecipe(new ItemStack(ExPItems.ingot, 1, EnumMetal.COPPER.ordinal()), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.FIRE_BRICK.ordinal()), (int)(EnumMetal.COPPER.getMeltingTemperature() * 0.85F), 0, new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.COPPER_COATED_FIRE_BRICK.ordinal()), 0);
        RecipesAnvil.addWeldingRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.FIRE_BRICK.ordinal()), new ItemStack(ExPItems.ingot, 1, EnumMetal.COPPER.ordinal()), 0, (int)(EnumMetal.COPPER.getMeltingTemperature() * 0.85F), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.COPPER_COATED_FIRE_BRICK.ordinal()), 0);
        RecipesAnvil.addRecipe(new ItemStack(ExPItems.ingot, 1, EnumMetal.COPPER.ordinal()), (int)(EnumMetal.COPPER.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.generic, 2, ItemGeneric.EnumGenericType.COPPER_RIM.ordinal()), 40, 0);
        RecipesAnvil.addRecipe(new ItemStack(ExPItems.ingot, 1, EnumMetal.COPPER.ordinal()), (int)(EnumMetal.COPPER.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.generic, 12, ItemGeneric.EnumGenericType.COPPER_PINS.ordinal()), 20, 0);
        RecipesAnvil.addRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.IRON_BLOOM.ordinal()), (int)(EnumMetal.IRON.getMeltingTemperature() * 0.5F), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.REFINED_IRON_BLOOM.ordinal()), 100, 1);
        RecipesAnvil.addRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.REFINED_IRON_BLOOM.ordinal()), (int)(EnumMetal.IRON.getMeltingTemperature() * 0.5F), new ItemStack(ExPItems.ingot, 1, EnumMetal.IRON.ordinal()), 160, 1);
        RecipesAnvil.addRecipe(new ItemStack(ExPItems.ingot, 1, EnumMetal.IRON.ordinal()), (int)(EnumMetal.IRON.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.LOCK.ordinal()), 120, 2);
        RecipesAnvil.addRecipe(new ItemStack(ExPItems.ingot, 1, EnumMetal.IRON.ordinal()), (int)(EnumMetal.IRON.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.generic, 24, ItemGeneric.EnumGenericType.NAILS.ordinal()), 40, 2);
        RecipesAnvil.addRecipe(new ItemStack(ExPItems.ingot, 1, EnumMetal.IRON.ordinal()), (int)(EnumMetal.IRON.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.generic, 6, ItemGeneric.EnumGenericType.SMALL_GEAR.ordinal()), 80, 2);
        RecipesAnvil.addRecipe(new ItemStack(ExPItems.ingot, 1, EnumMetal.IRON.ordinal()), (int)(EnumMetal.IRON.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.generic, 3, ItemGeneric.EnumGenericType.MEDIUM_GEAR.ordinal()), 120, 2);
        RecipesAnvil.addRecipe(new ItemStack(ExPItems.ingot, 1, EnumMetal.IRON.ordinal()), (int)(EnumMetal.IRON.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.LARGE_GEAR.ordinal()), 180, 2);
        RecipesAnvil.addRecipe(new ItemStack(ExPItems.ingot, 1, EnumMetal.IRON.ordinal()), (int)(EnumMetal.IRON.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.IRON_STICK.ordinal()), 100, 2);
        RecipesAnvil.addRecipe(new ItemStack(ExPItems.metalGeneric, 1, EnumMetal.IRON.ordinal() + ItemMetalGeneric.EnumGenericType.DOUBLE_SHEET.ordinal() * EnumMetal.values().length), (int)(EnumMetal.IRON.getMeltingTemperature() * 0.75F), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.SAW_BLADE.ordinal()), 260, 2);
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

            if (ItemTuyere.items.containsKey(material))
            {
                RecipesAnvil.addRecipe(new ItemStack(ExPItems.metalGeneric, 1, material.getMaterial().ordinal() + EnumMetal.values().length * 2), (int)(material.getMaterial().getMeltingTemperature() * 0.75F), new ItemStack(ItemTuyere.items.get(material), 1, 0), 200, material.getMaterial().getRequiredAnvilTier());
            }
        }

        for (EnumArmorStats stats : EnumArmorStats.values())
        {
            if (stats.associatedMetal == null)
            {
                continue;
            }

            for (EntityEquipmentSlot slot : ItemArmorFramework.SLOTS)
            {
                RecipesAnvil.addRecipe(ItemArmorFramework.createFramework(stats, slot), (int)(stats.associatedMetal.getMeltingTemperature() * 0.75F), new ItemStack(ItemArmor.items.get(Pair.of(slot, stats)), 1, 0), (int) (1000 * ItemArmorFramework.slotModifiers[3 - (slot.ordinal() - 2)]), stats.associatedMetal.getRequiredAnvilTier());
            }
        }

        RecipesBarrel.addRecipe(new RecipesBarrel.RecipeBarrelFluid(new ItemStack(ExPItems.generic, 16, ItemGeneric.EnumGenericType.TWINE.ordinal()), FluidRegistry.WATER, new FluidStack(ExPFluids.tannin, 1), 24000, true));
        RecipesBarrel.addRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.HIDE.ordinal()), new FluidStack(FluidRegistry.WATER, 100), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.SOAKED_HIDE.ordinal()), 6000);
        RecipesBarrel.addRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.PREPARED_HIDE.ordinal()), new FluidStack(ExPFluids.tannin, 50), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.LEATHER.ordinal()), 12000);
        RecipesBarrel.addRecipe(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.WOOL.ordinal()), new FluidStack(FluidRegistry.WATER, 100), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.SOAKED_WOOL.ordinal()), 6000);
        RecipesBarrel.addRecipe(new RecipeBarrelTreatedStick(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.TREATED_STICK.ordinal()), new FluidStack(ExPFluids.oliveOil, 200), 12000));
        RecipesBarrel.addRecipe(new RecipeBarrelTreatedStick(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.TREATED_STICK.ordinal()), new FluidStack(ExPFluids.walnutOil, 500), 12000));
        RecipesBarrel.addRecipe(new RecipeBarrelCheese());
        RecipesBarrel.addRecipe(new RecipesBarrel.RecipeBarrelFluid(new ItemStack(ExPItems.generic, 32, ItemGeneric.EnumGenericType.SALT.ordinal()), FluidRegistry.WATER, new FluidStack(ExPFluids.brine, 10000), 24000, true));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.BEANS));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.BEETROOT));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.CABBAGE));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.CARROT));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.CUCUMBER));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.EGGPLANT));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.GARLIC));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.LEEK));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.LETTUCE));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.ONION));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.PARSNIP));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.PEPPER_GREEN));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.PEPPER_YELLOW));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.PEPPER_RED));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.PUMPKIN));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.RADISH));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.SPINACH));
        RecipesBarrel.addRecipe(new RecipeBarrelPickle(FoodEntry.TOMATO));

        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.oliveOil, FoodEntry.OLIVE));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.walnutOil, FoodEntry.WALNUT));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.APPLE));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.PEACH));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.ORANGE));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.PEAR));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.PLUM));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.BANANA));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.LEMON));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.APRICOT));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.CHERRY));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.POMEGRANATE));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.GRAPEFRUIT));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.AVOCADO));
        RecipesPress.addRecipe(new RecipePressFood(ExPFluids.juice, FoodEntry.CARAMBOLA));
        RecipesPress.addRecipe(new RecipesPress.RecipePress(new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.WOOD_PULP.ordinal()), new FluidStack(FluidRegistry.WATER, 10), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.PRESSED_WOOD_PULP.ordinal())));

        RecipesBlastFurnace.addRecipe(new RecipesBlastFurnace.BlastFurnaceRecipe(new ItemStack[]{ new ItemStack(ExPItems.ore, 5, EnumOre.HEMATITE.ordinal()), new ItemStack(ExPItems.generic, 5, ItemGeneric.EnumGenericType.CHARCOAL.ordinal()), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.BONE_ASH.ordinal()) }, EnumMetal.PIG_IRON, 1700F, 12000, 50));
        RecipesBlastFurnace.addRecipe(new RecipesBlastFurnace.BlastFurnaceRecipe(new ItemStack[]{ new ItemStack(ExPItems.ore, 5, EnumOre.HEMATITE.ordinal()), new ItemStack(ExPItems.generic, 5, ItemGeneric.EnumGenericType.ANTHRACITE.ordinal()), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.BONE_ASH.ordinal()) }, EnumMetal.PIG_IRON, 1700F, 12000, 50));
        RecipesBlastFurnace.addRecipe(new RecipesBlastFurnace.BlastFurnaceRecipe(new ItemStack[]{ new ItemStack(ExPItems.ore, 5, EnumOre.MAGNETITE.ordinal()), new ItemStack(ExPItems.generic, 5, ItemGeneric.EnumGenericType.CHARCOAL.ordinal()), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.BONE_ASH.ordinal()) }, EnumMetal.PIG_IRON, 1700F, 12000, 50));
        RecipesBlastFurnace.addRecipe(new RecipesBlastFurnace.BlastFurnaceRecipe(new ItemStack[]{ new ItemStack(ExPItems.ore, 5, EnumOre.MAGNETITE.ordinal()), new ItemStack(ExPItems.generic, 5, ItemGeneric.EnumGenericType.ANTHRACITE.ordinal()), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.BONE_ASH.ordinal()) }, EnumMetal.PIG_IRON, 1700F, 12000, 50));
        RecipesBlastFurnace.addRecipe(new RecipesBlastFurnace.BlastFurnaceRecipe(new ItemStack[]{ new ItemStack(ExPItems.ore, 5, EnumOre.PENTLANDITE.ordinal()), new ItemStack(ExPItems.generic, 5, ItemGeneric.EnumGenericType.CHARCOAL.ordinal()), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.BONE_ASH.ordinal()) }, EnumMetal.PIG_IRON, 1700F, 12000, 50));
        RecipesBlastFurnace.addRecipe(new RecipesBlastFurnace.BlastFurnaceRecipe(new ItemStack[]{ new ItemStack(ExPItems.ore, 5, EnumOre.PENTLANDITE.ordinal()), new ItemStack(ExPItems.generic, 5, ItemGeneric.EnumGenericType.ANTHRACITE.ordinal()), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.BONE_ASH.ordinal()) }, EnumMetal.PIG_IRON, 1700F, 12000, 50));
        RecipesBlastFurnace.addRecipe(new RecipesBlastFurnace.BlastFurnaceRecipe(new ItemStack[]{ new ItemStack(ExPItems.ingot, 1, EnumMetal.PIG_IRON.ordinal()) }, EnumMetal.STEEL, 1800F, 4000, 100));
    }

    @Override
    public void postInit(FMLPostInitializationEvent evt)
    {
        RecipesSmelting.sort();
    }

    private static class RecipeBarrelCheese implements RecipesBarrel.IRecipeBarrel
    {
        @Override
        public boolean matches(FluidStack fs, ItemStack is)
        {
            return fs != null && fs.getFluid() == ExPFluids.milk && is.isEmpty();
        }

        @Override
        public int getProgressRequired(ItemStack is)
        {
            return 240000;
        }

        @Override
        public ItemStack getResult(ItemStack is, FluidStack fluidStack)
        {
            ItemStack food = new ItemStack(ExPItems.food, 1, FoodEntry.CHEESE.getId());
            ItemFood iFood = (ItemFood)ExPItems.food;
            iFood.setTotalWeight(food, fluidStack == null ? 10000 : fluidStack.amount);
            iFood.setItemRotMultiplier(food, 0.75F);
            iFood.setTotalRot(food, 1F);
            return food;
        }

        @Override
        public ItemStack getInput()
        {
            return ItemStack.EMPTY;
        }

        @Override
        public FluidStack getInputFluid()
        {
            return new FluidStack(ExPFluids.milk, 1000);
        }

        @Override
        public FluidStack getOutputFluid(ItemStack is)
        {
            return null;
        }

        @Override
        public void consumeFluid(IFluidHandler handler, ItemStack is)
        {
            handler.drain(Integer.MAX_VALUE, true);
        }

        @Override
        public String getRecipeName(ItemStack is)
        {
            return ExPItems.food.getUnlocalizedName(new ItemStack(ExPItems.food, 1, FoodEntry.CHEESE.getId())) + ".name";
        }

        @Override
        public void consumeItem(ItemStack is)
        {
        }
    }

    private static class RecipeBarrelTreatedStick implements RecipesBarrel.IRecipeBarrel
    {
        private final ItemStack itemOut;
        private final FluidStack fluidIn;
        private final int timeReq;

        public RecipeBarrelTreatedStick(ItemStack itemOut, FluidStack fluidIn, int timeReq)
        {
            this.itemOut = itemOut;
            this.fluidIn = fluidIn;
            this.timeReq = timeReq;
        }

        @Override
        public boolean matches(FluidStack fs, ItemStack is)
        {
            return fs != null && fs.isFluidEqual(fluidIn) && is.getItem() instanceof ItemStick;
        }

        @Override
        public int getProgressRequired(ItemStack is)
        {
            return this.timeReq;
        }

        @Override
        public ItemStack getResult(ItemStack is, FluidStack fluidStack)
        {
            return this.itemOut.copy();
        }

        @Override
        public ItemStack getInput()
        {
            return new ItemStack(ExPItems.stick, 1, OreDictionary.WILDCARD_VALUE);
        }

        @Override
        public FluidStack getInputFluid()
        {
            FluidStack copy = this.fluidIn.copy();
            copy.amount = 10000;
            return copy;
        }

        @Override
        public FluidStack getOutputFluid(ItemStack is)
        {
            FluidStack copy = this.fluidIn.copy();
            copy.amount = 10000 - this.fluidIn.amount;
            return copy;
        }

        @Override
        public void consumeFluid(IFluidHandler handler, ItemStack is)
        {
            handler.drain(this.fluidIn, true);
        }

        @Override
        public String getRecipeName(ItemStack is)
        {
            return this.itemOut.getUnlocalizedName() + ".name";
        }

        @Override
        public void consumeItem(ItemStack is)
        {
            is.shrink(1);
        }
    }

    private static class RecipeBarrelPickle implements RecipesBarrel.IRecipeBarrel
    {
        public final FoodEntry foodEntryIn;

        private RecipeBarrelPickle(FoodEntry foodEntryIn)
        {
            this.foodEntryIn = foodEntryIn;
        }

        @Override
        public boolean matches(FluidStack fs, ItemStack is)
        {
            return fs != null && fs.getFluid() == ExPFluids.brine && fs.amount > 2000 && is.getItem() instanceof ItemFood && ((ItemFood) is.getItem()).getPreservationType(is) == 0 && is.getMetadata() == this.foodEntryIn.getId();
        }

        @Override
        public int getProgressRequired(ItemStack is)
        {
            return 48000;
        }

        @Override
        public ItemStack getResult(ItemStack is, FluidStack fluidStack)
        {
            ItemStack stack = is.isEmpty() ? new ItemStack(ExPItems.food, 1, this.foodEntryIn.getId()) : is.copy();
            ItemFood food = (ItemFood) stack.getItem();
            food.setTotalWeight(stack, is.isEmpty() ? 10000 : food.getTotalWeight(is));
            food.setPreservationType(stack, (byte)1);
            food.setItemRotMultiplier(stack, 0.5F);
            food.setTotalRot(stack, is.isEmpty() ? 0 : food.getTotalRot(is) * 0.1F);
            return stack;
        }

        @Override
        public ItemStack getInput()
        {
            return new ItemStack(ExPItems.food, 1, this.foodEntryIn.getId());
        }

        @Override
        public FluidStack getInputFluid()
        {
            return new FluidStack(ExPFluids.brine, 2000);
        }

        @Override
        public FluidStack getOutputFluid(ItemStack is)
        {
            return null;
        }

        @Override
        public void consumeFluid(IFluidHandler handler, ItemStack is)
        {
            handler.drain(2000, true);
        }

        @Override
        public String getRecipeName(ItemStack is)
        {
            return I18n.format("exp.txt.pickled") + " " + I18n.format(this.getInput().getItem().getUnlocalizedName(this.getInput()) + ".name");
        }

        @Override
        public void consumeItem(ItemStack is)
        {
            is.shrink(1);
        }
    }

    private static class RecipePressFood implements RecipesPress.IRecipePress
    {
        private final Fluid fluidOut;
        private final FoodEntry foodEntry;

        public RecipePressFood(Fluid fluidOut, FoodEntry foodEntry)
        {
            this.fluidOut = fluidOut;
            this.foodEntry = foodEntry;
        }

        @Override
        public boolean matches(ItemStack is)
        {
            return is.getItem() instanceof ItemFood && is.getMetadata() == this.foodEntry.getId();
        }

        @Override
        public FluidStack getOutput(ItemStack is)
        {
            int amount = is.isEmpty() ? 10000 : (int) ((ItemFood)is.getItem()).getTotalWeight(is);
            return new FluidStack(this.fluidOut, amount);
        }

        @Override
        public ItemStack getInput()
        {
            return new ItemStack(ExPItems.food, 1, this.foodEntry.getId());
        }

        @Override
        public ItemStack getOutput()
        {
            return ItemStack.EMPTY;
        }
    }

    private static class DummyRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
    {
        @Override
        public boolean matches(InventoryCrafting inv, World worldIn)
        {
            return false;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv)
        {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean canFit(int width, int height)
        {
            return false;
        }

        @Override
        public ItemStack getRecipeOutput()
        {
            return ItemStack.EMPTY;
        }
    }

    private static class RecipeSmeltingFood implements RecipesSmelting.IRecipeSmelting
    {
        private final FoodEntry entryIn;
        private final FoodEntry entryOut;
        private final int t;

        public RecipeSmeltingFood(FoodEntry entryIn, FoodEntry entryOut, int i)
        {
            this.entryIn = entryIn;
            this.entryOut = entryOut;
            this.t = i;
        }

        @Override
        public boolean matches(ItemStack is, float temperature)
        {
            return temperature >= this.t && is.getItem() instanceof ItemFood && is.getMetadata() == this.entryIn.getId();
        }

        @Override
        public ItemStack getOutput(ItemStack is)
        {
            ItemStack ret = new ItemStack(ExPItems.food, 1, entryOut.getId());
            ItemFood food = (ItemFood) ExPItems.food;
            food.setLastTickTime(ret, food.getLastTickTime(is));
            food.setTotalRot(ret, food.getTotalRot(is));
            food.setTotalWeight(ret, food.getTotalWeight(is));
            food.setItemRotMultiplier(ret, food.getItemRotMultiplier(is) * 0.5F);
            return ret;
        }

        @Override
        public List<ItemStack> getInput(ItemStack is)
        {
            return Collections.singletonList(new ItemStack(ExPItems.food, 1, this.entryIn.getId()));
        }

        @Override
        public float getTemperature()
        {
            return this.t;
        }
    }

    private static class RecipeSmeltingMeltable implements RecipesSmelting.IRecipeSmelting
    {
        @Override
        public boolean matches(ItemStack is, float temperature)
        {
            return is.getItem() instanceof IMeltableMetal && temperature >= ((IMeltableMetal) is.getItem()).getMeltingTemperature(is);

        }

        @Override
        public ItemStack getOutput(ItemStack is)
        {
            return ItemStack.EMPTY;
        }

        @Override
        public List<ItemStack> getInput(ItemStack is)
        {
            return Collections.EMPTY_LIST;
        }

        @Override
        public float getTemperature()
        {
            return 0;
        }
    }
}
