package v0id.exp.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.ItemBlockSplitterGearbox;
import v0id.exp.tile.TileSplitterGearbox;

import javax.annotation.Nullable;

import static v0id.api.exp.data.ExPBlockProperties.GEARBOX_OUT0;
import static v0id.api.exp.data.ExPBlockProperties.GEARBOX_OUT1;

public class BlockSplitterGearbox extends BlockDirectional implements IInitializableBlock, IItemBlockProvider, IWeightProvider
{
    public BlockSplitterGearbox()
    {
        super(Material.WOOD);
        this.initBlock();
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

    @Override
    public void initBlock()
    {
        this.setHardness(1.0F);
        this.setResistance(3);
        this.setRegistryName(ExPRegistryNames.blockSplitterGearbox);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(GEARBOX_OUT0, EnumFacing.WEST).withProperty(GEARBOX_OUT1, EnumFacing.EAST));
        this.setLightOpacity(0);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, GEARBOX_OUT0, GEARBOX_OUT1);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).ordinal();
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockSplitterGearbox(this));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        Vec3d vec = placer.getLookVec();
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFacingFromVector((float)vec.x, (float)vec.y, (float)vec.z));
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
        return new TileSplitterGearbox();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileSplitterGearbox tile = (TileSplitterGearbox) worldIn.getTileEntity(pos);
        if (state.getValue(FACING) != facing)
        {
            if (playerIn.isSneaking())
            {
                tile.output1 = facing;
            }
            else
            {
                tile.output0 = facing;
            }

            tile.sendUpdatePacket();
            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return super.getActualState(state, worldIn, pos).withProperty(GEARBOX_OUT0, ((TileSplitterGearbox)worldIn.getTileEntity(pos)).output0).withProperty(GEARBOX_OUT1, ((TileSplitterGearbox)worldIn.getTileEntity(pos)).output1);
    }
}
