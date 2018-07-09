package v0id.exp.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.property.EnumWaterLilyType;
import v0id.api.exp.data.*;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.IItemBlockProvider;
import v0id.exp.block.item.ItemBlockWaterLily;
import v0id.exp.util.Helpers;

import java.util.Random;
import java.util.stream.Stream;

import static v0id.api.exp.block.property.EnumWaterLilyType.LEOPARDESS;
import static v0id.api.exp.data.ExPBlockProperties.LILY_TYPE;
import static v0id.api.exp.data.ExPBlockProperties.PLANT_BLOOMING;

public class BlockWaterLily extends BlockBush implements IWeightProvider, IInitializableBlock, IOreDictEntry, IItemBlockProvider
{
	protected static final AxisAlignedBB LILY_PAD_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.09375D, 0.9375D);
	
	public BlockWaterLily()
	{
		super();
		this.initBlock();
	}

	@Override
	public void initBlock()
	{
		this.setHardness(0.5f);
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockWaterLily));
		this.setResistance(0);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(PLANT_BLOOMING, false).withProperty(LILY_TYPE, LEOPARDESS));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
	}

	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return LILY_PAD_AABB;
    }

	@Override
	protected boolean canSustainBush(IBlockState state)
	{
		return state.getMaterial() == Material.WATER;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		if (worldIn.isRemote)
		{
			return;
		}
		
		if (!Helpers.canPlantGrow(pos, worldIn))
		{
			worldIn.setBlockToAir(pos);
			// TODO death effects!
		}
		else
		{
			checkBlooming(worldIn, pos, state, rand);
			trySpread(worldIn, pos, state, rand);
		}
	}

	private void trySpread(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (rand.nextDouble() <= Helpers.getGenericGrowthModifier(pos, worldIn, false) / 5)
		{
			BlockPos offset = pos.add(rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET) - rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET), rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET) - rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET), rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET) - rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET));
			BlockPos belowOffset = offset.down();
			IBlockState atBelowOffset = worldIn.getBlockState(belowOffset);
			if (worldIn.isAirBlock(offset) && atBelowOffset.getBlock().canSustainPlant(atBelowOffset, worldIn, belowOffset, EnumFacing.UP, this))
			{
				worldIn.setBlockState(offset, state.withProperty(PLANT_BLOOMING, false));
			}
				
		}
	}

	public void checkBlooming(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		// FIXME Check blooming corresponding to current season, or temp | humidity!
		EnumWaterLilyType type = state.getValue(LILY_TYPE);
		boolean isNight = worldIn.getWorldTime() % 24000 >= 12000;
		boolean blooming = state.getValue(PLANT_BLOOMING);
		float growthChanceModifier = (float) Math.sin(Math.toRadians((worldIn.getWorldTime() % 12000) * 0.015));
		if (isNight)
		{
			if (type.isNightBloomer())
			{
				if (rand.nextDouble() <= growthChanceModifier && !blooming)
				{
					worldIn.setBlockState(pos, this.getDefaultState().withProperty(LILY_TYPE, type).withProperty(PLANT_BLOOMING, true), 2);
				}
			}
			else
			{
				if (rand.nextDouble() <= growthChanceModifier / 3 && blooming)
				{
					worldIn.setBlockState(pos, this.getDefaultState().withProperty(LILY_TYPE, type).withProperty(PLANT_BLOOMING, false), 2);
				}
			}
		}
		else
		{
			if (!type.isNightBloomer())
			{
				if (rand.nextDouble() <= growthChanceModifier && !blooming)
				{
					worldIn.setBlockState(pos, this.getDefaultState().withProperty(LILY_TYPE, type).withProperty(PLANT_BLOOMING, true), 2);
				}
			}
			else
			{
				if (rand.nextDouble() <= growthChanceModifier / 3 && blooming)
				{
					worldIn.setBlockState(pos, this.getDefaultState().withProperty(LILY_TYPE, type).withProperty(PLANT_BLOOMING, false), 2);
				}
			}
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Water;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumWaterLilyType.values().length * 2; ++i)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(PLANT_BLOOMING, meta >= 5).withProperty(LILY_TYPE, EnumWaterLilyType.values()[meta % 5]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(PLANT_BLOOMING) ? 5 : 0) + state.getValue(LILY_TYPE).ordinal();
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(state) % 5;
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other instanceof net.minecraft.block.BlockLilyPad;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PLANT_BLOOMING, LILY_TYPE);
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return 0.05F;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return IWeightProvider.DEFAULT_VOLUME;
	}

	@Override
	public EnumOffsetType getOffsetType()
	{
		return EnumOffsetType.XYZ;
	}
	
	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockWaterLily).forEach(s -> OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)));
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWaterLily(this));
	}
}
