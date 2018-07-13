package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.gravity.ISupport;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.block.item.ItemBlockLog;
import v0id.exp.tile.TileLogPile;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockLogPile extends Block implements IInitializableBlock, ISupport
{
    public BlockLogPile()
    {
        super(Material.WOOD);
        this.initBlock();
    }

    @Override
    public void initBlock()
    {
        this.setHardness(6);
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockLogPile));
        this.setResistance(6);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.LOGPILE_IS_ROTATED, false).withProperty(ExPBlockProperties.LOGPILE_COUNT, 1).withProperty(ExPBlockProperties.TREE_TYPE, EnumTreeType.KALOPANAX));
        Blocks.FIRE.setFireInfo(this, 5, 5);
        this.setTickRandomly(true);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileLogPile tile = (TileLogPile) worldIn.getTileEntity(pos);
        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Item.getItemFromBlock(ExPBlocks.logsDeco[tile.type.ordinal() / 5]), tile.count, 1 + (tile.type.ordinal() % 5) * 3));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return ItemStack.EMPTY;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, ExPBlockProperties.LOGPILE_COUNT, ExPBlockProperties.LOGPILE_IS_ROTATED, ExPBlockProperties.TREE_TYPE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileLogPile tile = (TileLogPile) worldIn.getTileEntity(pos);
        return super.getActualState(state, worldIn, pos).withProperty(ExPBlockProperties.TREE_TYPE, tile.type).withProperty(ExPBlockProperties.LOGPILE_COUNT, tile.count).withProperty(ExPBlockProperties.LOGPILE_IS_ROTATED, tile.rotated);
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

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileLogPile();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack item = playerIn.getHeldItem(hand);
        if (item.isEmpty())
        {
            if (!worldIn.isRemote && hand == EnumHand.MAIN_HAND)
            {
                TileLogPile tlp = (TileLogPile) worldIn.getTileEntity(pos);
                BlockPos offset = pos.offset(facing);
                InventoryHelper.spawnItemStack(worldIn, offset.getX(), offset.getY(), offset.getZ(), new ItemStack(Item.getItemFromBlock(ExPBlocks.logsDeco[tlp.type.ordinal() / 5]), 1, 1 + (tlp.type.ordinal() % 5) * 3));
                if (--tlp.count <= 0)
                {
                    worldIn.setBlockToAir(pos);
                }
                else
                {
                    tlp.sendUpdatePacket();
                }
            }

            return true;
        }
        else
        {
            if (item.getItem() instanceof ItemBlockLog)
            {
                TileLogPile tlp = (TileLogPile) worldIn.getTileEntity(pos);
                if (tlp != null && tlp.count < 8)
                {
                    IBlockState istate = ((ItemBlockLog) item.getItem()).getBlock().getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, item.getMetadata(), playerIn, hand);
                    EnumTreeType type = istate.getValue(ExPBlockProperties.TREE_TYPE);
                    if (type == tlp.type)
                    {
                        if (!worldIn.isRemote)
                        {
                            ++tlp.count;
                            worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.6F + (float) Math.random() * 0.4F);
                            if (!playerIn.capabilities.isCreativeMode)
                            {
                                item.shrink(1);
                            }

                            tlp.sendUpdatePacket();
                        }

                        return true;
                    }
                }
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        if (!worldIn.isRemote)
        {
            TileLogPile tlp = (TileLogPile) worldIn.getTileEntity(pos);
            if (tlp.litAt == null && worldIn.getBlockState(pos.up()).getBlock() instanceof BlockFire)
            {
                worldIn.setBlockToAir(pos.up());
                tlp.litAt = new Calendar(IExPWorld.of(worldIn).today().getTime());
                tlp.initStructure();
            }
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);
        if (!worldIn.isRemote)
        {
            ((TileLogPile) worldIn.getTileEntity(pos)).onBlockTick();
        }
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        TileLogPile tlp = (TileLogPile) worldIn.getTileEntity(pos);
        if (tlp.litAt != null)
        {
            if (worldIn.getBlockState(pos.up()).getBlock() instanceof BlockLogPile)
            {
                return;
            }

            if (!worldIn.isAirBlock(pos.up()))
            {
                pos = pos.up();
            }

            for (int i = 0; i < 10; ++i)
            {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), 0, 0.1F, 0);
            }
        }

        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }
}
