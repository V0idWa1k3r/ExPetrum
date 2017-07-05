package v0id.exp.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.block.EnumGrassState;
import v0id.api.exp.block.EnumShrubberyType;
import v0id.api.exp.block.IGrass;
import v0id.api.exp.data.*;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.IItemBlockProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by V0idWa1k3r on 17-Jun-17.
 */
public class BlockGenericShrubbery extends BlockBush implements IInitializableBlock, IPlantable, IOreDictEntry, IItemBlockProvider
{
    public enum BloomColor implements IStringSerializable
    {
        WHITE,
        ORANGE,
        MAGENTA,
        LIGHT_BLUE,
        YELLOW,
        LIME,
        PINK,
        GRAY,
        SILVER,
        CYAN,
        PURPLE,
        BLUE,
        BROWN,
        GREEN,
        RED,
        BLACK,
        NONE;

        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
    }

    public static final PropertyInteger TROPIC_PLANT_LEAF = PropertyInteger.create("leaf", 0, 2);
    public static final PropertyEnum<BloomColor> BLOOM_COLOR = PropertyEnum.create("color", BloomColor.class);

    public BlockGenericShrubbery()
    {
        super();
        this.initBlock();
    }

    @Override
    public void registerOreDictNames()
    {

    }

    @Override
    public void initBlock()
    {
        this.setHardness(1);
        this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockGenericShrubbery));
        this.setResistance(1);
        this.setSoundType(SoundType.PLANT);
        this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
        this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
        this.setTickRandomly(true);
        Blocks.FIRE.setFireInfo(this, 60, 100);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.SHRUBBERY_TYPE, EnumShrubberyType.TROPICAL));
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry)
    {
        registry.register(new ItemBlockWithMetadata(this));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BlockVegetation.TALL_GRASS_AABB;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.checkGrowth(worldIn, pos);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);
        this.checkGrowth(worldIn, pos);
    }

    public void checkGrowth(World world, BlockPos pos)
    {
        IBlockState stateBelow = world.getBlockState(pos.down());
        if (stateBelow.getBlock() instanceof IGrass)
        {
            if (((IGrass)stateBelow.getBlock()).getState() != EnumGrassState.NORMAL)
            {
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    {
        return EnumPlantType.Plains;
    }

    @Override
    public EnumOffsetType getOffsetType()
    {
        return EnumOffsetType.XYZ;
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isAssociatedBlock(Block other)
    {
        return super.isAssociatedBlock(other) || other instanceof BlockTallGrass;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ExPBlockProperties.SHRUBBERY_TYPE, EnumShrubberyType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ExPBlockProperties.SHRUBBERY_TYPE).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer.Builder(this).add(ExPBlockProperties.SHRUBBERY_TYPE).add(TROPIC_PLANT_LEAF).add(BLOOM_COLOR).build();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        ExPMisc.modelVariantRandom.setSeed(MathHelper.getPositionRandom(pos));
        switch (state.getValue(ExPBlockProperties.SHRUBBERY_TYPE))
        {
            case TROPICAL:
            {
                return state.withProperty(TROPIC_PLANT_LEAF, ExPMisc.modelVariantRandom.nextInt(3)).withProperty(BLOOM_COLOR, ExPMisc.modelVariantRandom.nextBoolean() ? BloomColor.NONE : BloomColor.values()[ExPMisc.modelVariantRandom.nextInt(16)]);
            }

            case FLOWER:
            {
                return state.withProperty(BLOOM_COLOR, BloomColor.values()[ExPMisc.modelVariantRandom.nextInt(15)]);
            }

            case SMALL_SHRUB:
            {
                return state.withProperty(BLOOM_COLOR, BloomColor.values()[ExPMisc.modelVariantRandom.nextInt(9)]);
            }

            case MUSHROOM:
            {
                return state.withProperty(BLOOM_COLOR, BloomColor.values()[ExPMisc.modelVariantRandom.nextInt(16)]);
            }

            default:
            {
                return state;
            }
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        Arrays.stream(EnumShrubberyType.values()).forEach(e -> items.add(new ItemStack(this, 1, e.ordinal())));
    }
}
