package v0id.exp.block.plant;

import java.util.Random;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.core.util.ItemBlockWithMetadata;
import v0id.api.exp.block.EnumGrassAmount;
import v0id.api.exp.block.EnumGrassState;
import v0id.api.exp.block.IGrass;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.exp.block.IBlockRegistryEntry;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.handler.ExPHandlerRegistry;
import v0id.exp.util.Helpers;

public class BlockVegetation extends BlockBush implements IInitializableBlock, IOreDictEntry, IBlockRegistryEntry, IItemRegistryEntry
{
	// Not exposed, for internal use only(rendering the model)
	// You do NOT need these! They are purely for rendering! They are not even saved with the meta!
	// Stop looking at them and go inspect something more useful to you :P
	private static final PropertyInteger RANDOM_MODEL = PropertyInteger.create("amdl", 0, 2);
	private static final PropertyInteger TEXTURE_INDEX = PropertyInteger.create("tindex", 0, 19);
	protected static final AxisAlignedBB TALL_GRASS_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
	
	public BlockVegetation()
	{
		super();
		this.initBlock();
	}
	
	@Override
	public void initBlock()
	{
		this.setHardness(0);
		this.setRegistryName(ExPRegistryNames.blockVegetation);
		this.setResistance(0);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(RANDOM_MODEL, 0).withProperty(ExPBlockProperties.VEGETATION_GROWTH, 0));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setTickRandomly(true);
		Blocks.FIRE.setFireInfo(this, 60, 100);
		ExPHandlerRegistry.blockEntries.add(this);
		ExPHandlerRegistry.itemEntries.add(this);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.AIR;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return TALL_GRASS_AABB;
    }
	
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < 4; ++i)
		{
			list.add(new ItemStack(itemIn, 1, i));
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ExPBlockProperties.VEGETATION_GROWTH, RANDOM_MODEL, TEXTURE_INDEX);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(ExPBlockProperties.VEGETATION_GROWTH, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ExPBlockProperties.VEGETATION_GROWTH);
	}

	@Override
	public EnumOffsetType getOffsetType()
	{
		return EnumOffsetType.NONE;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		IBlockState growsOn = world.getBlockState(pos.down());
		EnumGrassAmount level = growsOn.getBlock() instanceof IGrass ? ((IGrass)growsOn.getBlock()).getAmount(growsOn, pos.down(), world) : EnumGrassAmount.NORMAL;
		int texIndex = level.ordinal() + state.getValue(ExPBlockProperties.VEGETATION_GROWTH) * 5;
		ExPMisc.modelVariantRandom.setSeed(MathHelper.getPositionRandom(pos));
		return state.withProperty(RANDOM_MODEL, ExPMisc.modelVariantRandom.nextInt(3)).withProperty(TEXTURE_INDEX, texIndex);
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return this.getExtendedState(state, worldIn, pos);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.growthChecks(worldIn, pos, state, false);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		this.growthChecks(worldIn, pos, state, true);
	}
	
	public void growthChecks(World world, BlockPos pos, IBlockState state, boolean ticked)
	{
		// Our tallgrass could be removed by super invocations!
		if (world.getBlockState(pos).getBlock() == this)
		{
			if (!world.isRemote)
			{
				if (Helpers.canPlantGrow(pos, world))
				{
					if (!ticked)
					{
						return;
					}
					
					EnumGrassState grassState = EnumGrassState.NORMAL;
					IBlockState growsOn = world.getBlockState(pos.down());
					int growth = state.getValue(ExPBlockProperties.VEGETATION_GROWTH);
					if (growsOn.getBlock() instanceof IGrass)
					{
						grassState = ((IGrass)growsOn.getBlock()).getState();
					}
					
					if (grassState == EnumGrassState.DEAD)
					{
						world.setBlockToAir(pos);
						return;
					}
					
					if (grassState == EnumGrassState.DRY)
					{
						if (world.rand.nextInt(4) >= growth)
						{
							world.setBlockToAir(pos);
						}
						
						return;
					}
					
					float growthRateMultiplierBase = Helpers.getGenericGrowthModifier(pos, world, true);
					if (growth < 3)
					{
						if (world.rand.nextFloat() < growthRateMultiplierBase)
						{
							world.setBlockState(pos, state.cycleProperty(ExPBlockProperties.VEGETATION_GROWTH));
						}
					}
					else
					{
						if (world.rand.nextFloat() < growthRateMultiplierBase)
						{
							BlockPos offset = pos.add(world.rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET) - world.rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET), world.rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET) - world.rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET),world.rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET) - world.rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET));
							BlockPos belowOffset = offset.down();
							//TODO Handle farmland & crops!
							if (world.isAirBlock(offset) && world.getBlockState(belowOffset).getBlock().canSustainPlant(world.getBlockState(belowOffset), world, belowOffset, EnumFacing.UP, this))
							{
								world.setBlockState(offset, this.getDefaultState());
							}
						}
					}
				}
				else
				{
					world.setBlockToAir(pos);
				}
			}
		}
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other instanceof BlockTallGrass;
	}
	
	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockVegetation).forEach(s -> OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)));
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWithMetadata(this));
	}

	@Override
	public void registerBlock(IForgeRegistry<Block> registry)
	{
		registry.register(this);
	}
}
