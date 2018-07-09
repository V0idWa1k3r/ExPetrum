
package v0id.exp.block.fluid;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.IItemBlockProvider;
import v0id.exp.block.item.ItemBlockWeighted;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockClay extends BlockFluidFinite implements IInitializableBlock, IItemBlockProvider
{
    public BlockClay()
    {
        super(ExPFluids.clay, Material.CLAY);
        this.initBlock();
    }

    @Override
    public void initBlock()
    {
        this.setHardness(1.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.GROUND);
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockClay));
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
        this.setLightOpacity(0);
        this.setQuantaPerBlock(10);
        this.renderLayer = BlockRenderLayer.SOLID;
        this.setLightOpacity(255);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        return false;
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWeighted(this));
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        int currentVal = state.getValue(BlockFluidFinite.LEVEL);
        if (currentVal > 0)
        {
            worldIn.setBlockState(pos, state.withProperty(BlockFluidFinite.LEVEL, currentVal - 1));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
    {
        return new AxisAlignedBB(0, 0, 0, 1, 0.1F + state.getValue(LEVEL) * 0.1F * 0.9F, 1);
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0, 0, 0, 1, 0.1F + state.getValue(LEVEL) * 0.1F * 0.9F, 1);
    }

    @Override
    public boolean canCollideCheck(@Nonnull IBlockState state, boolean fullHit)
    {
        return this.isCollidable();
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos)
    {
        return this.getBoundingBox(blockState, worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(@Nonnull IBlockAccess worldIn, @Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Nonnull
    @Override
    public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune)
    {
        return ExPItems.generic;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    public int quantityDropped(@Nonnull Random par1Random)
    {
        return 1 + par1Random.nextInt(3);
    }
}
