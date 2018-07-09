package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.ExPetrum;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.tile.TileFruitPress;

import javax.annotation.Nullable;

import java.util.List;

import static v0id.api.exp.data.ExPBlockProperties.PRESS_VALUE;

public class BlockPress extends Block implements IWeightProvider, IInitializableBlock, IItemBlockProvider
{
    public BlockPress()
    {
        super(Material.WOOD);
        this.initBlock();
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
    public void initBlock()
    {
        this.setHardness(1.0F);
        this.setResistance(3);
        this.setRegistryName(ExPRegistryNames.blockFruitPress);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setLightOpacity(0);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PRESS_VALUE, 0));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(PRESS_VALUE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(PRESS_VALUE);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, PRESS_VALUE);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }

        playerIn.openGui(ExPetrum.instance, 12, worldIn, pos.getX(), pos.getY(), pos.getZ());
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
        return new TileFruitPress();
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
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
    {
        return Block.FULL_BLOCK_AABB;
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return new AxisAlignedBB(0, 0, 0, 1, 0.5F + blockState.getValue(PRESS_VALUE) * 0.0625F, 1);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
        AxisAlignedBB aabb1 = new AxisAlignedBB(0, 0, 0, 0.0625, 1, 1).offset(pos);
        if (entityBox.intersects(aabb1))
        {
            collidingBoxes.add(aabb1);
        }

        AxisAlignedBB aabb2 = new AxisAlignedBB(0.9375F, 0, 0, 1, 1, 1).offset(pos);
        if (entityBox.intersects(aabb2))
        {
            collidingBoxes.add(aabb2);
        }

        AxisAlignedBB aabb3 = new AxisAlignedBB(0, 0, 0, 1, 1, 0.0625).offset(pos);
        if (entityBox.intersects(aabb3))
        {
            collidingBoxes.add(aabb3);
        }

        AxisAlignedBB aabb4 = new AxisAlignedBB(0, 0, 0.9375F, 1, 1, 1).offset(pos);
        if (entityBox.intersects(aabb4))
        {
            collidingBoxes.add(aabb4);
        }
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        if (fallDistance >= 1.0F)
        {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileFruitPress)
            {
                ((TileFruitPress) tile).jump();
            }
        }
    }
}
