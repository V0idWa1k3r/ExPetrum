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
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.ExPetrum;
import v0id.exp.block.item.ItemBlockCrate;
import v0id.exp.tile.TileCrate;
import v0id.exp.util.Helpers;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockCrate extends Block implements IInitializableBlock, IItemBlockProvider, IWeightProvider
{
    public BlockCrate()
    {
        super(Material.CIRCUITS);
        this.initBlock();
    }

    @Override
    public void initBlock()
    {
        this.setHardness(1.0F);
        this.setResistance(3.0F);
        this.setRegistryName(ExPRegistryNames.blockCrate);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.TREE_TYPE, EnumTreeType.KALOPANAX));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, ExPBlockProperties.TREE_TYPE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileCrate tile = (TileCrate) worldIn.getTileEntity(pos);
        return super.getActualState(state, worldIn, pos).withProperty(ExPBlockProperties.TREE_TYPE, tile.type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockCrate(this));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return face == EnumFacing.UP ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }

        if (!worldIn.getBlockState(pos.up()).isSideSolid(worldIn, pos.up(), EnumFacing.DOWN))
        {
            playerIn.openGui(ExPetrum.instance, 4, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

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
        return new TileCrate();
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 3F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)2, (byte)2);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (EnumTreeType type : EnumTreeType.values())
        {
            items.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, ((TileCrate)world.getTileEntity(pos)).type.ordinal());
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity != null)
        {
            Helpers.dropInventoryItems(worldIn, pos, tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
            worldIn.updateComparatorOutputLevel(pos, this);
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this, 1, ((TileCrate)tileentity).type.ordinal()));
        }

        super.breakBlock(worldIn, pos, state);
    }
}
