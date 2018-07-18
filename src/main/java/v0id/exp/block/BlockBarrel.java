package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.ExPetrum;
import v0id.exp.block.item.ItemBlockBarrel;
import v0id.exp.item.ItemFluidBottle;
import v0id.exp.item.ItemPottery;
import v0id.exp.tile.TileBarrel;
import v0id.exp.util.Helpers;

import javax.annotation.Nullable;
import java.util.Random;

import static v0id.api.exp.data.ExPBlockProperties.TREE_TYPE;

public class BlockBarrel extends Block implements IWeightProvider, IItemBlockProvider
{
    public BlockBarrel()
    {
        super(Material.WOOD);
        this.setHardness(1.0F);
        this.setResistance(3);
        this.setRegistryName(ExPRegistryNames.blockBarrel);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TREE_TYPE, EnumTreeType.KALOPANAX));
        this.setLightOpacity(0);
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 5F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)2, (byte)2);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        return super.getActualState(state, worldIn, pos).withProperty(TREE_TYPE, tile instanceof TileBarrel ? ((TileBarrel) tile).treeType : EnumTreeType.KALOPANAX);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TREE_TYPE);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockBarrel(this));
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }

        TileBarrel barrel = (TileBarrel) worldIn.getTileEntity(pos);
        ItemStack is = playerIn.getHeldItem(hand);
        if (is.getItem() instanceof ItemPottery && is.getMetadata() == ItemPottery.EnumPotteryType.CERAMIC_JUG.ordinal())
        {
            if (barrel.fluidInventory.getFluid().getFluid() == FluidRegistry.WATER && barrel.fluidInventory.getFluid().amount >= 100)
            {
                barrel.fluidInventory.drain(100, true);
                playerIn.setHeldItem(hand, new ItemStack(ExPItems.pottery, 1, ItemPottery.EnumPotteryType.CERAMIC_JUG_FULL.ordinal()));
                worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 2.0F);
                return true;
            }
        }
        else
        {
            if (is.getItem() == Items.GLASS_BOTTLE)
            {
                FluidStack fs = barrel.fluidInventory.getFluid();
                if (ItemFluidBottle.allowedFluids.contains(fs.getFluid()) && fs.amount >= 250)
                {
                    ItemStack bottle = ItemFluidBottle.createFluidBottle(fs.getFluid());
                    barrel.fluidInventory.drain(250, true);
                    playerIn.dropItem(bottle, false);
                    worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1.0F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
                    return true;
                }
            }
        }

        barrel.sendUpdatePacket();
        playerIn.openGui(ExPetrum.instance, 10, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
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
        return new TileBarrel();
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
        return new AxisAlignedBB(0.125F, 0, 0.125F, 0.875F, 1, 0.875F);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity != null)
        {
            Helpers.dropInventoryItems(worldIn, pos, tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        if (tileentity instanceof TileBarrel)
        {
            ItemStack barrel = new ItemStack(this, 1, ((TileBarrel) tileentity).treeType.ordinal());
            barrel.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(((TileBarrel) tileentity).fluidInventory.drain(Integer.MAX_VALUE, true), true);
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), barrel);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (EnumTreeType type : EnumTreeType.values())
        {
            items.add(new ItemStack(this, 1, type.ordinal()));
        }
    }
}
