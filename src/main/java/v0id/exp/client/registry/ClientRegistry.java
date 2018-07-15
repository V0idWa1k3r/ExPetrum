package v0id.exp.client.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.*;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.block.property.EnumKaolinType;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.block.property.EnumWaterLilyType;
import v0id.api.exp.client.model.WavefrontObject;
import v0id.api.exp.combat.condition.ExecuteConditionKeyBindings;
import v0id.api.exp.data.*;
import v0id.api.exp.item.EnumArmorStats;
import v0id.api.exp.item.food.FoodEntry;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.api.exp.tile.crop.ExPFarmlandCapability;
import v0id.api.exp.tile.crop.IFarmland;
import v0id.exp.block.BlockCraftingTable;
import v0id.exp.block.BlockPlanks;
import v0id.exp.block.tree.BlockLeaf;
import v0id.exp.block.tree.BlockLog;
import v0id.exp.client.model.ModelLoaderExP;
import v0id.exp.client.model.entity.*;
import v0id.exp.client.render.entity.RenderAnimal;
import v0id.exp.client.render.entity.RenderFallingTree;
import v0id.exp.client.render.entity.RenderThrownWeapon;
import v0id.exp.client.render.entity.RenderWolf;
import v0id.exp.client.render.tile.*;
import v0id.exp.crop.ExPFarmland;
import v0id.exp.entity.EntityFallingTree;
import v0id.exp.entity.EntityGravFallingBlock;
import v0id.exp.entity.EntityThrownWeapon;
import v0id.exp.entity.impl.*;
import v0id.exp.item.*;
import v0id.exp.item.tool.IExPTool;
import v0id.exp.registry.ILifecycleListener;
import v0id.exp.tile.*;
import v0id.exp.util.Helpers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ClientRegistry implements ILifecycleListener
{
    public static ClientRegistry instance;

    public ClientRegistry()
    {

    }

    @Override
    public void preInit(FMLPreInitializationEvent evt)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityGravFallingBlock.class, RenderFallingBlock::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFallingTree.class, RenderFallingTree::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityThrownWeapon.class, RenderThrownWeapon::new);
        RenderingRegistry.registerEntityRenderingHandler(Chicken.class, r -> new RenderAnimal(r, ModelChicken.instance = new ModelChicken(), 1F));
        RenderingRegistry.registerEntityRenderingHandler(Cow.class, r -> new RenderAnimal(r, ModelCow.instance = new ModelCow(), 1F));
        RenderingRegistry.registerEntityRenderingHandler(Sheep.class, r -> new RenderAnimal(r, ModelSheep.instance = new ModelSheep(), 1F));
        RenderingRegistry.registerEntityRenderingHandler(Pig.class, r -> new RenderAnimal(r, ModelPig.instance = new ModelPig(), 1F));
        RenderingRegistry.registerEntityRenderingHandler(Wolf.class, r -> new RenderWolf(r, new ModelWolf(), 1F));
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileCrate.class, new TESRCrate());
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileQuern.class, new TESRQuern());
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileScrapingRack.class, new TESRScrapingRack());
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileBellows.class, new TESRBellows());
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileSpinningWheel.class, new TESRSpinningWheel());
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileChest.class, new TESRChest());
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileShaft.class, new TESRShaft());
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileWaterWheel.class, new TESRWaterWheel());
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileMechanicalQuern.class, new TESRMechanicalQuern());
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileWindmill.class, new TESRWindmill());
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileMechanicalBellows.class, new TESRMechanicalBellows());
        net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(TileSaw.class, new TESRSaw());
        this.loadAdditionalData();
        this.initAttacksConditions();
    }

    @Override
    public void init(FMLInitializationEvent evt)
    {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getGrassColor, ExPBlocks.grass, ExPBlocks.grass_dry, ExPBlocks.grass_dead);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getGrassColor, ExPBlocks.waterLily);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> 0x02661c, ExPBlocks.grass);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> 0x6a7223, ExPBlocks.grass_dry);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> 0x660000, ExPBlocks.grass_dead);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> ColorizerGrass.getGrassColor(1, 0.5), ExPBlocks.waterLily, ExPBlocks.genericShrubbery);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) ->
                worldIn != null && pos != null && worldIn.getBlockState(pos.down()).getBlock() instanceof IGrass ? ((IGrass) worldIn.getBlockState(pos.down()).getBlock()).getGrassColor(worldIn.getBlockState(pos.down()), pos.down(), worldIn) : Helpers.getGrassColor(state, worldIn, pos, tintIndex), ExPBlocks.vegetation, ExPBlocks.genericShrubbery);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) ->
                worldIn != null && pos != null && worldIn.getBlockState(pos).getBlock() instanceof IShrub ? ((IShrub) worldIn.getBlockState(pos).getBlock()).getShrubColor(state, pos, worldIn) : Helpers.getGrassColor(state, worldIn, pos, tintIndex), ArrayUtils.addAll(ExPBlocks.shrubs, ExPBlocks.berryBushes));
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getCoralColor, ExPBlocks.coralRock);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getCoralColor, ExPBlocks.coralPlant);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getLeafColor, ExPBlocks.leaves);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) ->
                {
                    if (worldIn != null && pos != null)
                    {
                        TileEntity tile = worldIn.getTileEntity(pos);
                        return tile instanceof TileOre ? ((TileOre)tile).type.getColor() : -1;
                    }

                    return -1;
                }, ExPBlocks.ore, ExPBlocks.boulderOre);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) -> state == null ? -1 : state.getValue(ExPBlockProperties.ANVIL_MATERIAL).getColor(), ExPBlocks.anvil);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> ColorizerGrass.getGrassColor(1, 0.5), ExPBlocks.vegetation);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> ColorizerGrass.getGrassColor(1, 0.5), ArrayUtils.addAll(ExPBlocks.shrubs, ExPBlocks.berryBushes));
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> ((ILeaves) Block.getBlockFromItem(stack.getItem())).getLeavesColorForMeta(stack.getMetadata()), ExPBlocks.leaves);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) ->
                EnumOre.values()[(stack.getMetadata() / EnumRockClass.values().length) % EnumOre.values().length].getColor(), ExPBlocks.ore, ExPBlocks.boulderOre);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) ->
                worldIn != null && worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileFarmland && worldIn.getTileEntity(pos).hasCapability(ExPFarmlandCapability.farmlandCap, EnumFacing.UP) ? ExPFarmland.getColor(IFarmland.of(worldIn.getTileEntity(pos), EnumFacing.UP)) : -1, ExPBlocks.farmland);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) ->
                EnumMetal.values()[stack.getMetadata()].getColor(), ExPItems.ingot);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) ->
                tintIndex == 1 ? EnumOre.values()[stack.getMetadata()].getColor() : -1, ExPItems.ore);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) ->
                tintIndex == 1 ? EnumMetal.values()[stack.getMetadata() - 2].getColor() : -1, ExPItems.moldIngot);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) ->
                EnumAnvilMaterial.values()[stack.getMetadata()].getColor(), Item.getItemFromBlock(ExPBlocks.anvil));
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> EnumMetal.values()[stack.getMetadata() % EnumMetal.values().length].getColor(), ExPItems.metalGeneric);
    }

    @Override
    public void postInit(FMLPostInitializationEvent evt)
    {

    }

    public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
        event.getRegistry().registerAll(
                new SoundEvent(ExPRegistryNames.asLocation(ExPRegistryNames.soundNewAge)).setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.soundNewAge))
        );
    }

    public static void registerModels()
    {
        // Model loaders
        OBJLoader.INSTANCE.addDomain("exp");
        ModelLoaderRegistry.registerLoader(new ModelLoaderExP());

        // Statically mapped block models
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.coralRock), 0, new ModelResourceLocation(ExPBlocks.coralRock.getRegistryName(), "rtindex=0"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.coralPlant), 0, new ModelResourceLocation(ExPBlocks.coralPlant.getRegistryName(), "ptindex=0,rtindex=0"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.snow), 0, new ModelResourceLocation(ExPBlocks.snow.getRegistryName(), "layers=1"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.crop), 0, new ModelResourceLocation("exp:crops/dead", "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.flint), 0, new ModelResourceLocation(ExPBlocks.flint.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.campfire), 0, new ModelResourceLocation(ExPBlocks.campfire.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.potteryStation), 0, new ModelResourceLocation(ExPBlocks.potteryStation.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.charcoal), 0, new ModelResourceLocation(ExPBlocks.charcoal.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.forge), 0, new ModelResourceLocation(ExPBlocks.forge.getRegistryName(), "active=false"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.quern), 0, new ModelResourceLocation(ExPBlocks.quern.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.crucible), 0, new ModelResourceLocation(ExPBlocks.crucible.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.hay), 0, new ModelResourceLocation(ExPBlocks.hay.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.nestingBox), 0, new ModelResourceLocation(ExPBlocks.nestingBox.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.barrel), 0, new ModelResourceLocation(ExPBlocks.barrel.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.scrapingRack), 0, new ModelResourceLocation(ExPBlocks.scrapingRack.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.bellows), 0, new ModelResourceLocation(ExPBlocks.bellows.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.spinningWheel), 0, new ModelResourceLocation(ExPBlocks.spinningWheel.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.bloomery), 0, new ModelResourceLocation(ExPBlocks.bloomery.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.fruitPress), 0, new ModelResourceLocation(ExPBlocks.fruitPress.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.gearbox), 0, new ModelResourceLocation(ExPBlocks.gearbox.getRegistryName(), "facing=north,input=south"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.waterWheel), 0, new ModelResourceLocation(ExPBlocks.waterWheel.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.splitterGearbox), 0, new ModelResourceLocation(ExPBlocks.splitterGearbox.getRegistryName(), "facing=north,out_0=east,out_1=west"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.mechanicalQuern), 0, new ModelResourceLocation(ExPBlocks.mechanicalQuern.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.windmill), 0, new ModelResourceLocation(ExPBlocks.windmill.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.mechanicalBellows), 0, new ModelResourceLocation(ExPBlocks.mechanicalBellows.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.saw), 0, new ModelResourceLocation(ExPBlocks.saw.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.mechanicalPotteryStation), 0, new ModelResourceLocation(ExPBlocks.mechanicalPotteryStation.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.blastFurnaceMetal), 0, new ModelResourceLocation(ExPBlocks.blastFurnaceMetal.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.blastFurnace), 0, new ModelResourceLocation(ExPBlocks.blastFurnace.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(ExPItems.lightBackpack, 0, new ModelResourceLocation(ExPItems.lightBackpack.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(ExPItems.travelersBackpack, 0, new ModelResourceLocation(ExPItems.travelersBackpack.getRegistryName(), "inventory"));
        // Iteration-dependent models
        mkCustomModelResourceLocations(ExPItems.stick, EnumTreeType.values().length + EnumShrubType.values().length + EnumBerry.values().length, i -> "type=" + ExPOreDict.stickNames[i]);
        mkCustomModelResourceLocations(ExPItems.toolHead, EnumToolClass.values().length * EnumToolStats.values().length, i -> "material=" + EnumToolStats.values()[i % EnumToolStats.values().length].name().toLowerCase() + ",type=" + EnumToolClass.values()[i / EnumToolStats.values().length].name().toLowerCase());
        mkCustomModelResourceLocations(ExPItems.seeds, EnumCrop.values().length - 1, i -> "crop=" + EnumCrop.values()[i + 1].getName());
        mkCustomModelResourceLocations(ExPItems.food, FoodEntry.allEntries.size(), i -> "type=" + FoodEntry.allEntries.get(i).getUnlocalizedName());
        mkCustomModelResourceLocations(ExPItems.ingot, EnumMetal.values().length, null);
        mkCustomModelResourceLocations(ExPBlocks.pebble, 16, i -> "amdl=0,class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.boulder, 16, i -> "amdl=0,class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.workedBoulder, 16, i -> "class=" + EnumRockClass.values()[i].getName() + ",workindex=0");
        mkCustomModelResourceLocations(ExPItems.rock, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.ice, 2, i -> "salt=" + (i == 1));
        mkCustomModelResourceLocations(ExPBlocks.vegetation, 4, i -> "inventory-" + i);
        mkCustomModelResourceLocations(ExPBlocks.waterLily, 10, i -> "blooming=" + (i >= 5) + ",type=" + EnumWaterLilyType.values()[i % 5].getName());
        mkCustomModelResourceLocations(ExPBlocks.fruit, 15, i -> "type=" + EnumFruit.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.rock, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.rockDeco0, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.rockDeco1, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.rockDeco2, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.cobblestone, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.soil, 16, i -> "class=" + EnumDirtClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.grass, 16, i -> "class=" + EnumDirtClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.grass_dry, 16, i -> "class=" + EnumDirtClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.grass_dead, 16, i -> "class=" + EnumDirtClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.cattail, 16, i -> "class=" + EnumDirtClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.sand, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.seaweed, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.farmland, 16, i -> "class=" + EnumDirtClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPItems.generic, ItemGeneric.EnumGenericType.values().length, i -> "type=" + ItemGeneric.EnumGenericType.values()[i].getName());
        mkCustomModelResourceLocations(ExPItems.ore, EnumOre.values().length, i -> "tindex=" + Integer.toString(EnumOre.values()[i].getTextureIndex()));
        mkCustomModelResourceLocations(ExPItems.moldTool, EnumToolClass.values().length * ItemMold.EnumMoldType.values().length, i -> "state=" + ItemMold.EnumMoldType.values()[i / EnumToolClass.values().length].name().toLowerCase() + ",tool=" + EnumToolClass.values()[i % EnumToolClass.values().length].name().toLowerCase());
        mkCustomModelResourceLocations(ExPItems.moldIngot, 2 + EnumMetal.values().length, i -> "ingot=" + (i == 0 ? "clay" : i == 1 ? "ceramic" : EnumMetal.values()[i - 2].name().toLowerCase()));
        mkCustomModelResourceLocations(ExPItems.pottery, ItemPottery.EnumPotteryType.values().length, i -> "type=" + ItemPottery.EnumPotteryType.values()[i].name().toLowerCase());
        mkCustomModelResourceLocations(ExPBlocks.kaolin, EnumKaolinType.values().length, i -> "type=" + EnumKaolinType.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.crate, EnumTreeType.values().length, i -> "ttype=" + EnumTreeType.values()[i].getName());
        mkCustomModelResourceLocations(ExPItems.woodenBucket, 4, i -> "fluid=" + (i == 0 ? "none" : i == 1 ? "water" : i == 2 ? "salt" : "milk"));
        mkCustomModelResourceLocations(ExPBlocks.trough, 11, i -> "water=" + i);
        mkCustomModelResourceLocations(ExPBlocks.anvil, EnumAnvilMaterial.values().length, i -> "inventory");
        mkCustomModelResourceLocations(ExPItems.metalGeneric, EnumMetal.values().length * ItemMetalGeneric.EnumGenericType.values().length, i -> "type=" + ItemMetalGeneric.EnumGenericType.values()[i / EnumMetal.values().length].name().toLowerCase());
        mkCustomModelResourceLocations(ExPBlocks.moltenMetal, EnumMoltenMetalState.values().length, i -> "state=" + EnumMoltenMetalState.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.chest, EnumTreeType.values().length, i -> "facing=north,ttype=" + EnumTreeType.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.shaft, EnumShaftMaterial.values().length, i -> "axis=x,type=" + EnumShaftMaterial.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.sapling, EnumTreeType.values().length, i -> "ttype=" + EnumTreeType.values()[i].getName());

        // Statically mapped item models
        registerStaticModel(ExPItems.basket, new ModelResourceLocation(ExPItems.basket.getRegistryName(), "inventory"));
        registerStaticModel(ExPItems.fireStarter, new ModelResourceLocation(ExPItems.fireStarter.getRegistryName(), "inventory"));
        registerStaticModel(ExPItems.grindstone, new ModelResourceLocation(ExPItems.grindstone.getRegistryName(), "inventory"));
        registerStaticModel(ExPItems.woolCard, new ModelResourceLocation(ExPItems.woolCard.getRegistryName(), "inventory"));
        registerStaticModel(ExPItems.flintAndIron, new ModelResourceLocation(ExPItems.flintAndIron.getRegistryName(), "inventory"));

        // Other models
        registerToolModels();
        registerFluidModels();
        registerTreeModels();
        registerOreModels();
        registerBerryBushModels();
        registerShrubModels();
        registerShrubberyModels();
        registerArmorModels();

        // State mappers
        registerCustomStateMappers();
    }

    public void loadAdditionalData()
    {
        TESRQuern.quernTopModel = new WavefrontObject();
        TESRSpinningWheel.model = new WavefrontObject();
        TESRWaterWheel.model = new WavefrontObject();
        TESRWindmill.model = new WavefrontObject();
        try
        {
            TESRQuern.quernTopModel.load(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("exp", "models/block/quern_top.obj")).getInputStream());
            TESRSpinningWheel.model.load(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("exp", "models/block/spinning_wheel.obj")).getInputStream());
            TESRWaterWheel.model.load(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("exp", "models/block/water_wheel.obj")).getInputStream());
            TESRWindmill.model.load(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("exp", "models/block/windmill.obj")).getInputStream());
        }
        catch (IOException e)
        {
            ExPMisc.modLogger.error("ExPetrum was unable to load it's wavefront models!", e);
        }
    }

    public static void mkCustomModelResourceLocations(Block block, int amount, Function<Integer, String> ptrFunc)
    {
        mkCustomModelResourceLocations(Item.getItemFromBlock(block), amount, ptrFunc);
    }

    public static void mkCustomModelResourceLocations(Item item, int amount, Function<Integer, String> ptrFunc)
    {
        for (int i = 0; i < amount; i++)
        {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryName(), ptrFunc == null ? "inventory" : ptrFunc.apply(i)));
        }
    }

    public static void registerStaticModel(Item item, ModelResourceLocation staticLocation)
    {
        ModelLoader.setCustomMeshDefinition(item, i -> staticLocation);
        ModelBakery.registerItemVariants(item, staticLocation);
    }

    public void initAttacksConditions()
    {
        ExPWeaponAttacks.piercingDash.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindForward));
        ExPWeaponAttacks.slash.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindBack));
        ExPWeaponAttacks.downStrike.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindBack, Minecraft.getMinecraft().gameSettings.keyBindForward));
        ExPWeaponAttacks.spin.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindLeft, Minecraft.getMinecraft().gameSettings.keyBindRight));
        ExPWeaponAttacks.shieldSlam.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindForward));
        ExPWeaponAttacks.behead.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindRight));
        ExPWeaponAttacks.stab.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindBack));
        ExPWeaponAttacks.itemThrow.executeConditions.add(new ExecuteConditionKeyBindings(Minecraft.getMinecraft().gameSettings.keyBindBack, Minecraft.getMinecraft().gameSettings.keyBindForward));
    }

    public static void registerArmorModels()
    {
        for (Map.Entry<Pair<EntityEquipmentSlot, EnumArmorStats>, ItemArmor> data : ItemArmor.items.entrySet())
        {
            registerStaticModel(data.getValue(), new ModelResourceLocation(ExPRegistryNames.asLocation(ExPRegistryNames.itemArmor), "type=" + data.getKey().getLeft().getName() + "_" + data.getKey().getRight().name));
        }

        mkCustomModelResourceLocations(ExPItems.armorFramework, 4 * EnumArmorStats.values().length, i -> "type=" + ItemArmorFramework.SLOTS[i % 4].getName() + "_" + EnumArmorStats.values()[i / 4].name);
    }

    public static void registerBerryBushModels()
    {
        Arrays.stream(ExPBlocks.berryBushes).forEach(bush -> mkCustomModelResourceLocations(bush, EnumBerry.values().length, i -> "istall=false,type=" + EnumBerry.values()[i].getName()));
    }

    public static void registerShrubModels()
    {
        Arrays.stream(ExPBlocks.shrubs).forEach(shrub -> mkCustomModelResourceLocations(shrub, EnumShrubType.values().length, i -> "istall=false,type=" + EnumShrubType.values()[i].getName()));
    }

    public static void registerShrubberyModels()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.genericShrubbery), 0, new ModelResourceLocation(new ResourceLocation("exp", "tropical_shrubbery"), "color=none,leaf=0"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.genericShrubbery), 1, new ModelResourceLocation(new ResourceLocation("exp", "flower_shrubbery"), "color=white"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.genericShrubbery), 2, new ModelResourceLocation(new ResourceLocation("exp", "small_shrubbery"), "color=white"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.genericShrubbery), 3, new ModelResourceLocation(new ResourceLocation("exp", "mushroom_shrubbery"), "color=red"));
    }

    public static void registerOreModels()
    {
        for (int i = 0; i < EnumRockClass.values().length * EnumOre.values().length * 3; ++i)
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.ore), i, new ModelResourceLocation(ExPBlocks.ore.getRegistryName(), "class=" + EnumRockClass.values()[i % EnumRockClass.values().length].getName() + ",oretexture=" + EnumOre.values()[(i / EnumRockClass.values().length) % EnumOre.values().length].getTextureIndex()));
            if (i < EnumRockClass.values().length * EnumOre.values().length)
            {
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.boulderOre), i, new ModelResourceLocation(ExPBlocks.boulderOre.getRegistryName(), "class=" + EnumRockClass.values()[i % EnumRockClass.values().length].getName() + ",oretexture=" + EnumOre.values()[(i / EnumRockClass.values().length) % EnumOre.values().length].getTextureIndex()));
            }
        }
    }

    public static void registerTreeModels()
    {
        Arrays.asList(ExPBlocks.logs).forEach(ClientRegistry::registerLogItemModel);
        Arrays.asList(ExPBlocks.logsDeco).forEach(ClientRegistry::registerLogItemModel);
        Arrays.asList(ExPBlocks.leaves).forEach(ClientRegistry::registerLeafItemModel);
        Arrays.asList(ExPBlocks.planks).forEach(ClientRegistry::registerPlankItemModel);
        Arrays.asList(ExPBlocks.craftingTables).forEach(ClientRegistry::registerCraftingTableItemModel);
    }

    public static void registerFluidModels()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.saltWater), 0, new ModelResourceLocation(ExPBlocks.saltWater.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.freshWater), 0, new ModelResourceLocation(ExPBlocks.freshWater.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.lava), 0, new ModelResourceLocation(ExPBlocks.lava.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.oil), 0, new ModelResourceLocation(ExPBlocks.oil.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.clay), 0, new ModelResourceLocation(ExPBlocks.clay.getRegistryName(), "inventory"));
    }

    public static void registerToolModels()
    {
        Collection<Item> toolsList = IExPTool.allTools.values();
        toolsList.forEach(tool -> registerStaticModel(tool, new ModelResourceLocation(new ResourceLocation(tool.getRegistryName().getResourceDomain(), "tools/" + tool.getRegistryName().getResourcePath().substring(0, tool.getRegistryName().getResourcePath().lastIndexOf('.'))), "material=" + ((IExPTool)tool).getStats(new ItemStack(tool, 1, 0)).getName())));
        for (int i = 0; i < EnumToolStats.values().length; ++i)
        {
            if (ItemTuyere.items.containsKey(EnumToolStats.values()[i]))
            {
                ModelResourceLocation mloc = new ModelResourceLocation(new ResourceLocation(ExPRegistryNames.modid, "tools/" + ExPRegistryNames.itemTuyere), "material=" + EnumToolStats.values()[i].getName());
                ModelLoader.setCustomMeshDefinition(ItemTuyere.items.get(EnumToolStats.values()[i]), is -> mloc);
                ModelLoader.registerItemVariants(ItemTuyere.items.get(EnumToolStats.values()[i]), mloc);
            }
        }
    }

    public static void registerLogItemModel(Block b)
    {
        for (int i = 0; i < 5; ++i)
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), i * 3 + 1, new ModelResourceLocation(ExPRegistryNames.asLocation(ExPRegistryNames.blockLog), "axis=y,ttype=" + EnumTreeType.values()[i + ((BlockLog) b).logIndex * 5].getName()));
        }
    }

    public static void registerPlankItemModel(Block b)
    {
        for (int i = 0; i < 15; ++i)
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), i, new ModelResourceLocation(ExPRegistryNames.asLocation(ExPRegistryNames.blockPlanks), "ttype=" + EnumTreeType.values()[i + ((BlockPlanks) b).logIndex * 15].getName()));
        }
    }

    public static void registerCraftingTableItemModel(Block b)
    {
        for (int i = 0; i < 15; ++i)
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), i, new ModelResourceLocation(ExPRegistryNames.asLocation(ExPRegistryNames.blockCraftingTable), "ttype=" + EnumTreeType.values()[i + ((BlockCraftingTable) b).logIndex * 15].getName()));
        }
    }

    public static void registerLeafItemModel(Block b)
    {
        for (int i = 0; i < 15; ++i)
        {
            EnumLeafState els = EnumLeafState.values()[i % 3];
            ResourceLocation rl =ExPRegistryNames.asLocation(ExPRegistryNames.blockLeaves + "_" + els.getName());
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), i, new ModelResourceLocation(rl, "ttype=" + EnumTreeType.values()[i / 3 + ((BlockLeaf) b).logIndex * 5].getName()));
        }
    }

    @SuppressWarnings("deprecation")
    public static void registerCustomStateMappers()
    {
        List<Block> lst = Lists.newArrayList();
        lst.addAll(Arrays.asList(ExPBlocks.logs));
        lst.addAll(Arrays.asList(ExPBlocks.logsDeco));
        for (Block b : lst)
        {
            ModelLoader.setCustomStateMapper(b, blockIn ->
            {
                Map<IBlockState, ModelResourceLocation> ret = Maps.newLinkedHashMap();
                for (int i = 0; i < 15; ++i)
                {
                    // Need the meta here as log blocks are fairly complex with states.
                    IBlockState state = b.getStateFromMeta(i);
                    String enumName = state.getValue(ExPBlockProperties.TREE_TYPE).getName();
                    String modelLocation = "axis=" + Axis.values()[i % 3].getName() + ",ttype=" + enumName;
                    ModelResourceLocation mrl = new ModelResourceLocation(ExPRegistryNames.asLocation(ExPRegistryNames.blockLog), modelLocation);
                    ret.put(state, mrl);
                }

                return ret;
            });
        }

        lst.clear();
        lst.addAll(Arrays.asList(ExPBlocks.leaves));
        for (Block b : lst)
        {
            ModelLoader.setCustomStateMapper(b, blockIn ->
            {
                Map<IBlockState, ModelResourceLocation> ret = Maps.newLinkedHashMap();
                for (int i = 0; i < 15; ++i)
                {
                    // Need the meta here as log/leaves blocks are fairly complex with states.
                    IBlockState state = b.getStateFromMeta(i);
                    EnumLeafState els = EnumLeafState.values()[i % 3];
                    String enumName = state.getValue(ExPBlockProperties.TREE_TYPE).getName();
                    String modelLocation = "ttype=" + enumName;
                    ResourceLocation rl = ExPRegistryNames.asLocation(ExPRegistryNames.blockLeaves + "_" + els.getName());
                    ModelResourceLocation mrl = new ModelResourceLocation(rl, modelLocation);
                    ret.put(state, mrl);
                }

                return ret;
            });
        }

        lst.clear();
        lst.addAll(Arrays.asList(ExPBlocks.planks));
        for (Block b : lst)
        {
            ModelLoader.setCustomStateMapper(b, blockIn ->
            {
                Map<IBlockState, ModelResourceLocation> ret = Maps.newLinkedHashMap();
                for (int i = 0; i < 15; ++i)
                {
                    IBlockState state = b.getStateFromMeta(i);
                    String modelLocation = "ttype=" + state.getValue(ExPBlockProperties.TREE_TYPE).getName();
                    ResourceLocation rl = ExPRegistryNames.asLocation(ExPRegistryNames.blockPlanks);
                    ModelResourceLocation mrl = new ModelResourceLocation(rl, modelLocation);
                    ret.put(state, mrl);
                }

                return ret;
            });
        }

        lst.clear();
        lst.addAll(Arrays.asList(ExPBlocks.craftingTables));
        for (Block b : lst)
        {
            ModelLoader.setCustomStateMapper(b, blockIn ->
            {
                Map<IBlockState, ModelResourceLocation> ret = Maps.newLinkedHashMap();
                for (int i = 0; i < 15; ++i)
                {
                    IBlockState state = b.getStateFromMeta(i);
                    String modelLocation = "ttype=" + state.getValue(ExPBlockProperties.TREE_TYPE).getName();
                    ResourceLocation rl = ExPRegistryNames.asLocation(ExPRegistryNames.blockCraftingTable);
                    ModelResourceLocation mrl = new ModelResourceLocation(rl, modelLocation);
                    ret.put(state, mrl);
                }

                return ret;
            });
        }

        ModelLoader.setCustomStateMapper(ExPBlocks.saltWater, new ExPStateMappers.StateMapperFluid(ExPBlocks.saltWater));
        ModelLoader.setCustomStateMapper(ExPBlocks.freshWater, new ExPStateMappers.StateMapperFluid(ExPBlocks.freshWater));
        ModelLoader.setCustomStateMapper(ExPBlocks.lava, new ExPStateMappers.StateMapperFluid(ExPBlocks.lava));
        ModelLoader.setCustomStateMapper(ExPBlocks.oil, new ExPStateMappers.StateMapperFluid(ExPBlocks.oil));
        ModelLoader.setCustomStateMapper(ExPBlocks.clay, new ExPStateMappers.StateMapperFluid(ExPBlocks.clay));
        ModelLoader.setCustomStateMapper(ExPBlocks.cattail, new ExPStateMappers.StateMapperCattail());
        ModelLoader.setCustomStateMapper(ExPBlocks.crop, new ExPStateMappers.StateMapperCrop(ExPBlocks.crop));
        ModelLoader.setCustomStateMapper(ExPBlocks.genericShrubbery, new ExPStateMappers.StateMapperGenericShrubbery(ExPBlocks.genericShrubbery));
    }
}
