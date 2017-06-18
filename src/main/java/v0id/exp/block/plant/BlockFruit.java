package v0id.exp.block.plant;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import v0id.api.core.VoidApi;
import v0id.api.exp.block.EnumFruit;
import v0id.api.exp.block.EnumLeafState;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.block.IBlockRegistryEntry;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.block.tree.BlockLeaf;
import v0id.exp.handler.ExPHandlerRegistry;
import v0id.exp.item.ItemFood;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by V0idWa1k3r on 16-Jun-17.
 */
public class BlockFruit extends Block implements IInitializableBlock, IBlockRegistryEntry, IItemRegistryEntry
{
    public BlockFruit()
    {
        super(Material.PLANTS);
        this.initBlock();
    }

    @Override
    public void initBlock()
    {
        this.setHardness(1F);
        this.setResistance(0);
        this.setRegistryName(ExPRegistryNames.blockFruit);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.FRUIT_TYPE, EnumFruit.APPLE));
        this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
        this.setTickRandomly(true);
        this.setHarvestLevel("knife", 0);
        this.setLightOpacity(0);
        ExPHandlerRegistry.put(this);
    }

    @Override
    public void registerBlock(IForgeRegistry<Block> registry)
    {
        registry.register(this);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
    }


    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ExPBlockProperties.FRUIT_TYPE, EnumFruit.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ExPBlockProperties.FRUIT_TYPE).ordinal();
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return Block.FULL_BLOCK_AABB;
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
    {
        super.randomTick(worldIn, pos, state, random);
        this.checkGrowthConditions(worldIn, pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.checkGrowthConditions(worldIn, pos, state);
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public void checkGrowthConditions(World w, BlockPos pos, IBlockState state)
    {
        IBlockState above = w.getBlockState(pos.up());
        if (above.getBlock() instanceof BlockLeaf)
        {
            if (above.getValue(ExPBlockProperties.TREE_TYPE) == state.getValue(ExPBlockProperties.FRUIT_TYPE).getAssociatedTreeType())
            {
                if (((BlockLeaf)above.getBlock()).getLeavesState(w, above, pos.up()) != EnumLeafState.DEAD)
                {
                    return;
                }
            }
        }

        this.dropBlockAsItem(w, pos, state, 0);
        w.setBlockToAir(pos);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ExPItems.food;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(ExPBlockProperties.FRUIT_TYPE).getAssociatedEntry().getId();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        Arrays.stream(EnumFruit.values()).forEach(e -> items.add(new ItemStack(this, 1, e.ordinal())));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer.Builder(this).add(ExPBlockProperties.FRUIT_TYPE).build();
    }

    @Override
    public EnumOffsetType getOffsetType()
    {
        return EnumOffsetType.XZ;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return PathNodeType.OPEN;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        World w = world instanceof World ? (World) world : FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER ? FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld() : VoidApi.proxy.getClientWorld();
        Random rand = w.rand;
        ItemStack item = new ItemStack(this.getItemDropped(state, rand, fortune), this.quantityDropped(state, fortune, rand), this.damageDropped(state));
        ItemFood foodItemRef = (ItemFood) item.getItem();
        foodItemRef.setLastTickTime(item, IExPWorld.of(w).today());
        EnumFruit fruit = state.getValue(ExPBlockProperties.FRUIT_TYPE);
        foodItemRef.setTotalWeight(item, fruit.getWeightMin() + rand.nextInt(fruit.getWeightMax() - fruit.getWeightMin()));
        foodItemRef.setTotalRot(item, rand.nextFloat() * 10);
        return Lists.newArrayList(item);
    }
}
