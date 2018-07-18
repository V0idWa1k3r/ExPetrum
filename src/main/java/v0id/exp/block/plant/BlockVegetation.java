package v0id.exp.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.block.EnumGrassAmount;
import v0id.api.exp.block.EnumGrassState;
import v0id.api.exp.block.EnumShrubberyType;
import v0id.api.exp.block.IGrass;
import v0id.api.exp.data.*;
import v0id.api.exp.item.IScythe;
import v0id.exp.block.BlockFarmland;
import v0id.exp.block.IItemBlockProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.item.ItemGeneric;
import v0id.exp.util.Helpers;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class BlockVegetation extends BlockBush implements IOreDictEntry, IItemBlockProvider
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
		this.setHardness(0);
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockVegetation));
		this.setResistance(0);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(RANDOM_MODEL, 0).withProperty(ExPBlockProperties.VEGETATION_GROWTH, 0));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setTickRandomly(true);
		Blocks.FIRE.setFireInfo(this, 60, 100);
	}
	
	@SuppressWarnings("deprecation")
    @Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos.down()) instanceof BlockFarmland ? 10 : super.getBlockHardness(blockState, worldIn, pos);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return rand.nextFloat() < 0.25F ? ExPItems.generic : Items.AIR;
	}

    @Override
    public int damageDropped(IBlockState state)
    {
        return 1;
    }

	@SuppressWarnings("deprecation")
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return TALL_GRASS_AABB;
    }
	
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return !(worldIn.getBlockState(pos).getBlock() instanceof BlockFarmland);
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < 4; ++i)
		{
			list.add(new ItemStack(this, 1, i));
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

	@SuppressWarnings("deprecation")
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

	@SuppressWarnings("deprecation")
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

						if (world.rand.nextFloat() < 0.001F)
                        {
                            int foundSelf = 0;
                            for (int dp = 0; dp < 25; ++dp)
                            {
                                BlockPos checkAt = pos.add((dp % 5) - 2, 0, (dp / 5) - 2);
                                if (world.getBlockState(checkAt).getBlock() instanceof BlockGenericShrubbery)
                                {
                                    return;
                                }

                                if (world.getBlockState(checkAt).getBlock() instanceof BlockVegetation)
                                {
                                    ++foundSelf;
                                }
                            }

                            if (foundSelf > 9)
                            {
                                world.setBlockState(pos, ExPBlocks.genericShrubbery.getDefaultState().withProperty(ExPBlockProperties.SHRUBBERY_TYPE, EnumShrubberyType.chooseType(world.getBiome(pos), world.rand)));
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
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IScythe)
        {
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ExPItems.generic, 1, ItemGeneric.EnumGenericType.HAY.ordinal()));
        }
	}
}
