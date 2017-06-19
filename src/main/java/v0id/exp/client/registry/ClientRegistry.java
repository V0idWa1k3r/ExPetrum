package v0id.exp.client.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.lang3.ArrayUtils;
import v0id.api.core.util.IFunctionalRenderFactory;
import v0id.api.exp.block.*;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.block.property.EnumWaterLilyType;
import v0id.api.exp.combat.condition.ExecuteConditionKeyBindings;
import v0id.api.exp.data.*;
import v0id.api.exp.item.EnumToolhead;
import v0id.api.exp.item.food.FoodEntry;
import v0id.api.exp.metal.EnumMetal;
import v0id.api.exp.metal.EnumToolStats;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.api.exp.tile.crop.ExPFarmlandCapability;
import v0id.api.exp.tile.crop.IFarmland;
import v0id.exp.block.tree.BlockLeaf;
import v0id.exp.block.tree.BlockLog;
import v0id.exp.client.model.ModelLoaderExP;
import v0id.exp.client.render.entity.RenderFallingTree;
import v0id.exp.client.render.entity.RenderThrownWeapon;
import v0id.exp.crop.ExPFarmland;
import v0id.exp.entity.EntityFallingTree;
import v0id.exp.entity.EntityGravFallingBlock;
import v0id.exp.entity.EntityThrownWeapon;
import v0id.exp.registry.ILifecycleListener;
import v0id.exp.tile.TileFarmland;
import v0id.exp.tile.TileOre;
import v0id.exp.util.Helpers;

import java.util.Arrays;
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
        IFunctionalRenderFactory.registerEntityRenderingHandler(EntityGravFallingBlock.class, RenderFallingBlock::new);
        IFunctionalRenderFactory.registerEntityRenderingHandler(EntityFallingTree.class, RenderFallingTree::new);
        IFunctionalRenderFactory.registerEntityRenderingHandler(EntityThrownWeapon.class, RenderThrownWeapon::new);
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
                worldIn.getBlockState(pos.down()).getBlock() instanceof IGrass ? ((IGrass) worldIn.getBlockState(pos.down()).getBlock()).getGrassColor(worldIn.getBlockState(pos.down()), pos.down(), worldIn) : Helpers.getGrassColor(state, worldIn, pos, tintIndex), ExPBlocks.vegetation, ExPBlocks.genericShrubbery);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) ->
                worldIn.getBlockState(pos).getBlock() instanceof IShrub ? ((IShrub) worldIn.getBlockState(pos).getBlock()).getShrubColor(state, pos, worldIn) : Helpers.getGrassColor(state, worldIn, pos, tintIndex), ArrayUtils.addAll(ExPBlocks.shrubs, ExPBlocks.berryBushes));
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getCoralColor, ExPBlocks.coralRock);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getCoralColor, ExPBlocks.coralPlant);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(Helpers::getLeafColor, ExPBlocks.leaves);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) ->
                worldIn.getTileEntity(pos) instanceof TileOre ? ((TileOre) worldIn.getTileEntity(pos)).type.getColor() : -1, ExPBlocks.ore, ExPBlocks.boulderOre);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> ColorizerGrass.getGrassColor(1, 0.5), ExPBlocks.vegetation);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> ColorizerGrass.getGrassColor(1, 0.5), ArrayUtils.addAll(ExPBlocks.shrubs, ExPBlocks.berryBushes));
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) -> ((ILeaves) Block.getBlockFromItem(stack.getItem())).getLeavesColorForMeta(stack.getMetadata()), ExPBlocks.leaves);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) ->
                EnumOre.values()[(stack.getMetadata() / EnumRockClass.values().length) % EnumOre.values().length].getColor(), ExPBlocks.ore, ExPBlocks.boulderOre);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) ->
                worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileFarmland && worldIn.getTileEntity(pos).hasCapability(ExPFarmlandCapability.farmlandCap, EnumFacing.UP) ? ExPFarmland.getColor(IFarmland.of(worldIn.getTileEntity(pos), EnumFacing.UP)) : -1, ExPBlocks.farmland);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((ItemStack stack, int tintIndex) ->
                EnumMetal.values()[stack.getMetadata()].getColor(), ExPItems.ingot);
    }

    @Override
    public void postInit(FMLPostInitializationEvent evt)
    {

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

        // Iteration-dependent models
        mkCustomModelResourceLocations(ExPItems.stick, EnumTreeType.values().length + EnumShrubType.values().length + EnumBerry.values().length, i -> "type=" + ExPOreDict.stickNames[i]);
        mkCustomModelResourceLocations(ExPItems.toolHead, EnumToolhead.values().length, i -> "type=" + EnumToolhead.values()[i].getName());
        mkCustomModelResourceLocations(ExPItems.seeds, EnumCrop.values().length - 1, i -> "crop=" + EnumCrop.values()[i + 1].getName());
        mkCustomModelResourceLocations(ExPItems.food, FoodEntry.allEntries.size(), i -> "type=" + FoodEntry.allEntries.get(i).getUnlocalizedName());
        mkCustomModelResourceLocations(ExPItems.ingot, EnumMetal.values().length, null);
        mkCustomModelResourceLocations(ExPBlocks.pebble, 16, i -> "amdl=0,class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.boulder, 16, i -> "amdl=0,class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.workedBoulder, 16, i -> "class=" + EnumRockClass.values()[i].getName() + ",workindex=0");
        mkCustomModelResourceLocations(ExPItems.rock, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPItems.rock, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.ice, 2, i -> "salt=" + (i == 1));
        mkCustomModelResourceLocations(ExPBlocks.vegetation, 4, i -> "inventory-" + i);
        mkCustomModelResourceLocations(ExPBlocks.waterLily, 10, i -> "blooming=" + (i >= 5) + ",type=" + EnumWaterLilyType.values()[i % 5].getName());
        mkCustomModelResourceLocations(ExPBlocks.fruit, 15, i -> "type=" + EnumFruit.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.rock, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.soil, 16, i -> "class=" + EnumDirtClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.grass, 16, i -> "class=" + EnumDirtClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.grass_dry, 16, i -> "class=" + EnumDirtClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.grass_dead, 16, i -> "class=" + EnumDirtClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.cattail, 16, i -> "class=" + EnumDirtClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.sand, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.seaweed, 16, i -> "class=" + EnumRockClass.values()[i].getName());
        mkCustomModelResourceLocations(ExPBlocks.farmland, 16, i -> "class=" + EnumDirtClass.values()[i].getName());

        // Statically mapped item models
        registerStaticModel(ExPItems.basket, new ModelResourceLocation(ExPItems.basket.getRegistryName(), "inventory"));

        // Other models
        registerToolModels();
        registerFluidModels();
        registerTreeModels();
        registerOreModels();
        registerBerryBushModels();
        registerShrubModels();
        registerShrubberyModels();

        // State mappers
        registerCustomStateMappers();
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
    }

    public static void registerFluidModels()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.saltWater), 0, new ModelResourceLocation(ExPBlocks.saltWater.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.freshWater), 0, new ModelResourceLocation(ExPBlocks.freshWater.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.lava), 0, new ModelResourceLocation(ExPBlocks.lava.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ExPBlocks.oil), 0, new ModelResourceLocation(ExPBlocks.oil.getRegistryName(), "inventory"));
    }

    public static void registerToolModels()
    {
        List<Item> toolsList = Arrays.asList(ExPItems.knife, ExPItems.pickaxe, ExPItems.axe, ExPItems.shovel, ExPItems.hoe, ExPItems.sword, ExPItems.scythe, ExPItems.battleaxe, ExPItems.hammer, ExPItems.spear, ExPItems.watering_can, ExPItems.gardening_spade);
        toolsList.forEach(tool -> ModelLoader.setCustomMeshDefinition(tool, stack -> new ModelResourceLocation(new ResourceLocation(tool.getRegistryName().getResourceDomain(), "tools/" + tool.getRegistryName().getResourcePath()), "material=" + EnumToolStats.values()[stack.getMetadata()].getName())));
        for (int i = 0; i < EnumToolStats.values().length; ++i)
        {
            Integer lambdaCaptureInt = i;
            toolsList.forEach(tool -> ModelLoader.registerItemVariants(tool, new ModelResourceLocation(new ResourceLocation(tool.getRegistryName().getResourceDomain(), "tools/" + tool.getRegistryName().getResourcePath()), "material=" + EnumToolStats.values()[lambdaCaptureInt].getName())));
        }
    }

    public static void registerLogItemModel(Block b)
    {
        for (int i = 0; i < 5; ++i)
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), i * 3 + 1, new ModelResourceLocation(ExPRegistryNames.blockLog, "axis=y,ttype=" + EnumTreeType.values()[i + ((BlockLog) b).logIndex * 5].getName()));
        }
    }

    public static void registerLeafItemModel(Block b)
    {
        for (int i = 0; i < 15; ++i)
        {
            EnumLeafState els = EnumLeafState.values()[i % 3];
            ResourceLocation rl = new ResourceLocation(ExPRegistryNames.blockLeaves.getResourceDomain(), ExPRegistryNames.blockLeaves.getResourcePath() + "_" + els.getName());
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
                    ModelResourceLocation mrl = new ModelResourceLocation(ExPRegistryNames.blockLog, modelLocation);
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
                    ResourceLocation rl = new ResourceLocation(ExPRegistryNames.blockLeaves.getResourceDomain(), ExPRegistryNames.blockLeaves.getResourcePath() + "_" + els.getName());
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
        ModelLoader.setCustomStateMapper(ExPBlocks.cattail, new ExPStateMappers.StateMapperCattail());
        ModelLoader.setCustomStateMapper(ExPBlocks.crop, new ExPStateMappers.StateMapperCrop(ExPBlocks.crop));
        ModelLoader.setCustomStateMapper(ExPBlocks.genericShrubbery, new ExPStateMappers.StateMapperGenericShrubbery(ExPBlocks.genericShrubbery));
    }
}
