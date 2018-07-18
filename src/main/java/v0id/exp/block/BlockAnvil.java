package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumAnvilMaterial;
import v0id.api.exp.block.IHasSpecialName;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.ExPetrum;
import v0id.exp.block.item.ItemBlockAnvil;
import v0id.exp.tile.TileAnvil;
import v0id.exp.util.Helpers;

import javax.annotation.Nullable;

import static v0id.api.exp.data.ExPBlockProperties.ANVIL_MATERIAL;

public class BlockAnvil extends Block implements IWeightProvider, IItemBlockProvider, IHasSpecialName
{
    public BlockAnvil()
    {
        super(Material.IRON);
        this.setHardness(3.0F);
        this.setResistance(10.0F);
        this.setRegistryName(ExPRegistryNames.blockAnvil);
        this.setSoundType(SoundType.ANVIL);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ANVIL_MATERIAL, EnumAnvilMaterial.STONE));
        this.setLightOpacity(0);
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 60F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)4, (byte)3);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (EnumAnvilMaterial material : EnumAnvilMaterial.values())
        {
            items.add(new ItemStack(this, 1, material.ordinal()));
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, ANVIL_MATERIAL);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ANVIL_MATERIAL).ordinal();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ANVIL_MATERIAL, EnumAnvilMaterial.values()[meta]);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockAnvil(this));
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }

        playerIn.openGui(ExPetrum.instance, 7, worldIn, pos.getX(), pos.getY(), pos.getZ());
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
        return new TileAnvil();
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
    public String getUnlocalizedName(ItemStack is)
    {
        return this.getUnlocalizedName() + "." + EnumAnvilMaterial.values()[is.getMetadata()].getName();
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity)
    {
        return state.getValue(ANVIL_MATERIAL) == EnumAnvilMaterial.STONE ? SoundType.STONE : super.getSoundType(state, world, pos, entity);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(ANVIL_MATERIAL).ordinal();
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity != null)
        {
            Helpers.dropInventoryItems(worldIn, pos, tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }
}
