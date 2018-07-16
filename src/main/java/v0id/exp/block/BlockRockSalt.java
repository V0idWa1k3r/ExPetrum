package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.item.ItemGeneric;

import java.util.Random;

import static v0id.api.exp.data.ExPBlockProperties.ROCKSALT_ISHINT;

public class BlockRockSalt extends Block implements IInitializableBlock, IItemBlockProvider
{
    public BlockRockSalt()
    {
        super(Material.ROCK);
        this.initBlock();
    }

    @Override
    public void initBlock()
    {
        this.setHardness(1.0F);
        this.setResistance(3);
        this.setRegistryName(ExPRegistryNames.blockRockSalt);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabUnderground);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ROCKSALT_ISHINT, false));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, ROCKSALT_ISHINT);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ROCKSALT_ISHINT) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ROCKSALT_ISHINT, meta == 1);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, state.getValue(ROCKSALT_ISHINT) ? 1 : 0);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return !state.getValue(ROCKSALT_ISHINT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return !state.getValue(ROCKSALT_ISHINT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return state.getValue(ROCKSALT_ISHINT) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return state.getValue(ROCKSALT_ISHINT) ? BlockBoulder.BOULDER_AABB : Block.FULL_BLOCK_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (state.getValue(ROCKSALT_ISHINT) && !worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ExPItems.generic;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return ItemGeneric.EnumGenericType.ROCK_SALT.ordinal();
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        return state.getValue(ROCKSALT_ISHINT) ? 1 : 1 + random.nextInt(3);
    }
}
