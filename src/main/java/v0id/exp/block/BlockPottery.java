package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.ExPetrum;
import v0id.exp.tile.TilePot;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPottery extends Block implements IInitializableBlock
{
    public enum EnumPotteryType implements IStringSerializable
    {
        POT,
        JUG,
        BOWL;

        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
    }

    public BlockPottery()
    {
        super(Material.ROCK);
        this.initBlock();
    }

    @Override
    public void initBlock()
    {
        this.setHardness(1.0F);
        this.setResistance(3.0F);
        this.setRegistryName(ExPRegistryNames.blockPottery);
        this.setSoundType(SoundType.STONE);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.POTTERY_TYPE, EnumPotteryType.POT));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, ExPBlockProperties.POTTERY_TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ExPBlockProperties.POTTERY_TYPE).ordinal();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ExPBlockProperties.POTTERY_TYPE, EnumPotteryType.values()[meta]);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        super.onNeighborChange(world, pos, neighbor);
        if (world instanceof World)
        {
            this.neighborChanged(world.getBlockState(pos), (World) world, pos, this, neighbor);
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return state.getValue(ExPBlockProperties.POTTERY_TYPE) == EnumPotteryType.POT;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return state.getValue(ExPBlockProperties.POTTERY_TYPE) == EnumPotteryType.POT ? new TilePot() : super.createTileEntity(world, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (playerIn.isSneaking())
        {
            worldIn.setBlockToAir(pos);
            return true;
        }
        else
        {
            if (state.getValue(ExPBlockProperties.POTTERY_TYPE) == EnumPotteryType.POT)
            {
                playerIn.openGui(ExPetrum.instance, 3, worldIn, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        ItemStack is = new ItemStack(ExPItems.pottery, 1, 3 + state.getValue(ExPBlockProperties.POTTERY_TYPE).ordinal());
        if (is.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
        {
            IItemHandler itemCap = is.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            TilePot pot = (TilePot)worldIn.getTileEntity(pos);
            if (pot != null)
            {
                IItemHandler potCap = pot.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                for (int i = 0; i < itemCap.getSlots() && i < potCap.getSlots(); ++i)
                {
                    itemCap.insertItem(i, potCap.getStackInSlot(i), false);
                }
            }
        }

        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), is);
    }
}
