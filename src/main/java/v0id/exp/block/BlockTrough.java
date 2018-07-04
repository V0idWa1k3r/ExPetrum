package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.item.ItemWoodenBucket;
import v0id.exp.util.temperature.TemperatureUtils;

import static v0id.api.exp.data.ExPBlockProperties.TROUGH_WATER;

public class BlockTrough extends Block implements IInitializableBlock, IItemBlockProvider, IWeightProvider
{
    public BlockTrough()
    {
        super(Material.WOOD);
        this.initBlock();
    }

    @Override
    public void initBlock()
    {
        this.setHardness(1.0F);
        this.setResistance(3.0F);
        this.setRegistryName(ExPRegistryNames.blockTrough);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setDefaultState(this.blockState.getBaseState().withProperty(TROUGH_WATER, 0));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setLightOpacity(0);
    }

    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
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

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack is = playerIn.getHeldItem(hand);
        if (is.getItem() instanceof ItemWoodenBucket && !worldIn.isRemote)
        {
            ItemWoodenBucket wb = (ItemWoodenBucket) is.getItem();
            if (wb.getStoredFluid(is) == ExPFluids.freshWater)
            {
                int current = state.getValue(TROUGH_WATER);
                int has = wb.getWater(is);
                int added = current + has > 10 ? 10 - current : has;
                if (added > 0)
                {
                    worldIn.setBlockState(pos, state.withProperty(TROUGH_WATER, current + added), 2);
                    wb.setWater(is, has - added);
                    if (has == added)
                    {
                        wb.setWaterType(is, null);
                    }

                    worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1F, 1F);
                    return true;
                }
            }
        }

        float currentT = TemperatureUtils.getTemperature(is);
        if (currentT > 0)
        {
            int currentW = state.getValue(TROUGH_WATER);
            if (currentW > 0)
            {
                for (int i = 0; i < 10; ++i)
                {
                    worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.6F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
                    worldIn.spawnParticle(EnumParticleTypes.CLOUD, pos.getX() + 0.2F + worldIn.rand.nextFloat() * 0.6F, pos.getY() + 1, pos.getZ() + 0.2F + worldIn.rand.nextFloat() * 0.6F, 0, 0.05F + worldIn.rand.nextFloat() * 0.05F, 0);
                }

                if (!worldIn.isRemote)
                {
                    TemperatureUtils.setTemperature(is, 0);
                    if (is.hasTagCompound() && is.getTagCompound().hasKey("exp:smithing"))
                    {
                        NBTTagCompound tag = is.getTagCompound().getCompoundTag("exp:smithing");
                        tag.setInteger("integrity", tag.getInteger("integrity") + tag.getInteger("integrityLost"));
                        tag.setInteger("heat", 0);
                    }

                    worldIn.setBlockState(pos, state.withProperty(TROUGH_WATER, currentW - 1), 2);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public float provideWeight(ItemStack item)
    {
        return 1F;
    }

    @Override
    public Pair<Byte, Byte> provideVolume(ItemStack item)
    {
        return Pair.of((byte)2, (byte)2);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TROUGH_WATER);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(TROUGH_WATER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TROUGH_WATER, meta);
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
