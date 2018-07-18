package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.block.IHasSpecialName;
import v0id.api.exp.block.ISupportBeam;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.gravity.ISupport;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;

import static v0id.api.exp.data.ExPBlockProperties.TREE_TYPE;

public class BlockSupportBeam extends Block implements IWeightProvider, IItemBlockProvider, IHasSpecialName, ISupport, ISupportBeam
{
    public int logIndex;
    public static final AxisAlignedBB SUPPORT_BEAM_AABB = new AxisAlignedBB(0.25F, 0, 0.25F, 0.75F, 1, 0.75F);

    public BlockSupportBeam(int i)
    {
        super(Material.WOOD);
        this.logIndex = i;
        this.setHardness(1);
        this.setRegistryName(createRegistryLocation());
        this.setResistance(3);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TREE_TYPE, EnumTreeType.values()[this.logIndex * 15]));
        Blocks.FIRE.setFireInfo(this, 5, 20);
        this.setLightOpacity(0);
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 0.2F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)2, (byte)1);
    }

    public ResourceLocation createRegistryLocation()
    {
        return ExPRegistryNames.asLocation(ExPRegistryNames.blockSupportBeam + this.logIndex);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TREE_TYPE);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (int i = 0; i < 15; ++i)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TREE_TYPE, EnumTreeType.values()[meta + this.logIndex * 15]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int ordinal = state.getValue(TREE_TYPE).ordinal();
        if (ordinal < this.logIndex * 15 || ordinal >= (this.logIndex + 1) * 15)
        {
            return 0;
        }

        return ordinal % 15;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(TREE_TYPE).ordinal() % 15;
    }

    @Override
    public String getUnlocalizedName(ItemStack is)
    {
        return super.getUnlocalizedName() + "." + EnumTreeType.values()[this.logIndex * 15 + is.getMetadata()].name().toLowerCase();
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SUPPORT_BEAM_AABB;
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
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

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState at = worldIn.getBlockState(pos.down());
        return at.getBlock() instanceof BlockSupportBeam || at.isFullCube();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !(worldIn.getBlockState(pos.down()).getBlock() instanceof ISupportBeam))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
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
}
