package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.block.EnumMoltenMetalState;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.gravity.IGravitySusceptible;
import v0id.exp.block.item.ItemBlockWithMetadata;

import javax.annotation.Nullable;

import java.util.Random;

import static v0id.api.exp.data.ExPBlockProperties.MOLTEN_METAL_STATE;

public class BlockMoltenMetal extends Block implements IGravitySusceptible, IItemBlockProvider
{
    public BlockMoltenMetal()
    {
        super(Material.LAVA);
        this.setHardness(-1);
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockMoltenMetal));
        this.setResistance(-1);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setDefaultState(this.blockState.getBaseState().withProperty(MOLTEN_METAL_STATE, EnumMoltenMetalState.NORMAL));
        this.setLightLevel(1F);
        this.setTickRandomly(true);
    }

    @Override
    public int getFallDamage(Entity collidedWith, EntityFallingBlock self)
    {
        return 20;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, MOLTEN_METAL_STATE);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(MOLTEN_METAL_STATE).ordinal();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(MOLTEN_METAL_STATE, EnumMoltenMetalState.values()[meta]);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return blockState.getValue(MOLTEN_METAL_STATE) == EnumMoltenMetalState.SOLID ? Block.FULL_BLOCK_AABB : Block.NULL_AABB;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);
        if (state.getValue(MOLTEN_METAL_STATE) == EnumMoltenMetalState.INVALID)
        {
            worldIn.setBlockState(pos, state.withProperty(MOLTEN_METAL_STATE, EnumMoltenMetalState.SOLID));
        }

        if (state.getValue(MOLTEN_METAL_STATE) == EnumMoltenMetalState.NORMAL)
        {
            worldIn.setBlockState(pos, state.withProperty(MOLTEN_METAL_STATE, EnumMoltenMetalState.INVALID));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return blockState.getValue(MOLTEN_METAL_STATE) == EnumMoltenMetalState.SOLID ? 5F : super.getBlockHardness(blockState, worldIn, pos);
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (int i = 0; i < 3; ++i)
        {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getValue(MOLTEN_METAL_STATE) == EnumMoltenMetalState.SOLID ? 0 : super.getLightValue(state, world, pos);
    }
}
