package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.gravity.IGravitySusceptible;
import v0id.api.exp.gravity.ISupport;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.item.ItemGeneric;

import java.util.Random;

import static v0id.api.exp.data.ExPBlockProperties.CHARCOAL_COUNT;

public class BlockCharcoal extends Block implements IGravitySusceptible, IItemBlockProvider, ISupport
{
    public BlockCharcoal()
    {
        super(Material.GROUND);
        this.setHardness(1.5F);
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockCharcoal));
        this.setResistance(5);
        this.setSoundType(SoundType.STONE);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setDefaultState(this.blockState.getBaseState().withProperty(CHARCOAL_COUNT, 1));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
    }

    @Override
    public int getFallDamage(Entity collidedWith, EntityFallingBlock self)
    {
        return 5;
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, CHARCOAL_COUNT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(CHARCOAL_COUNT, meta + 1);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(CHARCOAL_COUNT) - 1;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        BlockPos original = pos;
        while (worldIn.getBlockState(pos.up()).getBlock() instanceof BlockCharcoal)
        {
            pos = pos.up();
        }

        int level = pos == original ? state.getValue(CHARCOAL_COUNT) : worldIn.getBlockState(pos).getValue(CHARCOAL_COUNT);
        if (level == 1)
        {
            worldIn.setBlockToAir(pos);
        }
        else
        {
            worldIn.setBlockState(pos, state.withProperty(CHARCOAL_COUNT, level - 1), 3);
        }

        if (pos != original)
        {
            worldIn.setBlockState(original, state, 3);
        }
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

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0, 0, 0, 1, state.getValue(CHARCOAL_COUNT) * 0.0625F, 1);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack is = playerIn.getHeldItem(hand);
        if (is.getItem() instanceof ItemBlockWithMetadata)
        {
            if (((ItemBlockWithMetadata) is.getItem()).getBlock() instanceof BlockCharcoal)
            {
                if (state.getValue(CHARCOAL_COUNT) < 16)
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.setBlockState(pos, state.withProperty(CHARCOAL_COUNT, state.getValue(CHARCOAL_COUNT) + 1), 3);
                    }

                    return true;
                }
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ExPItems.generic;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return ItemGeneric.EnumGenericType.CHARCOAL.ordinal();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, 0);
    }
}
