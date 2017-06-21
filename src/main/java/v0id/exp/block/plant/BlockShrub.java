package v0id.exp.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.block.*;
import v0id.api.exp.data.*;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.IItemBlockProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.util.Helpers;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class BlockShrub extends Block implements IInitializableBlock, IShrub, IPlantable, IOreDictEntry, IItemBlockProvider
{
	public static final AxisAlignedBB BASIC_AABB = new AxisAlignedBB(0.3, 0, 0.3, 0.7, 0.5, 0.7);
	public static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0.3, 0, 0.3, 0.7, 0.5, 0.7);
	public final EnumShrubState shrubState;
	
	public BlockShrub(EnumShrubState s)
	{
		super(Material.PLANTS);
		this.shrubState = s;
		this.initBlock();
	}

	@Override
	public void initBlock()
	{
		this.setHardness(1);
		this.setRegistryName(ExPRegistryNames.asLocation(this.shrubState == EnumShrubState.BLOOMING ? ExPRegistryNames.blockShrubBlooming : this.shrubState == EnumShrubState.AUTUMN ? ExPRegistryNames.blockShrubAutumn : this.shrubState == EnumShrubState.DEAD ? ExPRegistryNames.blockShrubDead : ExPRegistryNames.blockShrubNormal));
		this.setResistance(0);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.SHRUB_TYPE, EnumShrubType.SPOTTED_LAUREL).withProperty(ExPBlockProperties.SHRUB_IS_TALL, false));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setTickRandomly(true);
		Blocks.FIRE.setFireInfo(this, 60, 100);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ExPItems.stick;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(ExPBlockProperties.SHRUB_TYPE).ordinal() + EnumTreeType.values().length;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(ExPBlockProperties.SHRUB_TYPE, EnumShrubType.values()[meta]);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(state.getBlock(), 1, this.getMetaFromState(state));
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ExPBlockProperties.SHRUB_TYPE).ordinal();
	}

    @SuppressWarnings("deprecation")
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return this.getExtendedState(state, worldIn, pos);
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return state.getValue(ExPBlockProperties.SHRUB_IS_TALL) ? FULL_AABB : BASIC_AABB;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		this.performChecks(state, worldIn, pos);
	}

    @SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		performChecks(state, worldIn, pos);
	}

	public void performChecks(IBlockState state, World worldIn, BlockPos pos)
	{
		if (!Helpers.canPlantGrow(pos.up(), worldIn))
		{
			worldIn.setBlockToAir(pos);
			return;
		}
		
		if (!(worldIn.getBlockState(pos.down()).getBlock() instanceof IShrub || worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn.getBlockState(pos.down()), worldIn, pos.down(), EnumFacing.UP, this)))
		{
			worldIn.setBlockToAir(pos);
			return;
		}
		
		if (worldIn.getBlockState(pos.down()).getBlock() instanceof IShrub && ((IShrub)worldIn.getBlockState(pos.down()).getBlock()).getShrubInternalType() == this.getShrubInternalType())
		{
			EnumShrubState sstate = ((IShrub)worldIn.getBlockState(pos.down()).getBlock()).getState();
			if (sstate != this.getState())
			{
				worldIn.setBlockState(pos, ExPBlocks.shrubs[sstate.ordinal()].getDefaultState().withProperty(ExPBlockProperties.SHRUB_TYPE, state.getValue(ExPBlockProperties.SHRUB_TYPE)));
            }
		}
		else
		{
			EnumShrubState sstate = this.getState();
			EnumGrassState grassState = worldIn.getBlockState(pos.down()).getBlock() instanceof IGrass ? ((IGrass)worldIn.getBlockState(pos.down()).getBlock()).getState() : Helpers.getSuggestedGrassState(pos.down(), worldIn);
			switch (grassState)
			{
				case NORMAL:
				{
					if (sstate == EnumShrubState.AUTUMN || sstate == EnumShrubState.DEAD)
					{
						worldIn.setBlockState(pos, ExPBlocks.shrubs[EnumShrubState.NORMAL.ordinal()].getDefaultState().withProperty(ExPBlockProperties.SHRUB_TYPE, state.getValue(ExPBlockProperties.SHRUB_TYPE)));
						return;
					}
					
					if (sstate == EnumShrubState.NORMAL)
					{
						if (worldIn.rand.nextDouble() <= Helpers.getGenericGrowthModifier(pos, worldIn, true) / 100)
						{
							worldIn.setBlockState(pos, ExPBlocks.shrubs[EnumShrubState.BLOOMING.ordinal()].getDefaultState().withProperty(ExPBlockProperties.SHRUB_TYPE, state.getValue(ExPBlockProperties.SHRUB_TYPE)));
							return;
						}
					}
					
					if (sstate == EnumShrubState.BLOOMING)
					{
						if (worldIn.rand.nextDouble() <= 0.0001)
						{
							worldIn.setBlockState(pos, ExPBlocks.shrubs[EnumShrubState.NORMAL.ordinal()].getDefaultState().withProperty(ExPBlockProperties.SHRUB_TYPE, state.getValue(ExPBlockProperties.SHRUB_TYPE)));
							return;
						}
					}
					
					break;
				}
				
				case DRY:
				{
					worldIn.setBlockState(pos, ExPBlocks.shrubs[EnumShrubState.AUTUMN.ordinal()].getDefaultState().withProperty(ExPBlockProperties.SHRUB_TYPE, state.getValue(ExPBlockProperties.SHRUB_TYPE)));
					return;
				}
				
				case DEAD:
				{
					worldIn.setBlockState(pos, ExPBlocks.shrubs[EnumShrubState.DEAD.ordinal()].getDefaultState().withProperty(ExPBlockProperties.SHRUB_TYPE, state.getValue(ExPBlockProperties.SHRUB_TYPE)));
                }
			}
		}
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		entityIn.motionX *= 0.4;
		entityIn.motionZ *= 0.4;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumShrubType.values().length; ++i)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ExPBlockProperties.SHRUB_TYPE, ExPBlockProperties.SHRUB_IS_TALL);
	}

	@SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
    {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return true;
	}

	@Override
	public boolean isFoliage(IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		super.onNeighborChange(world, pos, neighbor);
		if (world instanceof World)
		{
			this.neighborChanged(world.getBlockState(pos), (World) world, pos, world.getBlockState(pos).getBlock(), neighbor);
		}
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.withProperty(ExPBlockProperties.SHRUB_IS_TALL, this.canBeConnectedTo(world, pos.up(), EnumFacing.DOWN));
	}

	@Override
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		IBlockState at = world.getBlockState(pos);
		IBlockState self = world.getBlockState(pos.offset(facing));
		return at.getBlock() instanceof IShrub  && ((IShrub)at.getBlock()).getShrubInternalType() == this.getShrubInternalType() && at.getValue(ExPBlockProperties.SHRUB_TYPE) == self.getValue(ExPBlockProperties.SHRUB_TYPE);
	}

	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return PathNodeType.BLOCKED;
	}

	@Override
	public int getShrubColor(IBlockState state, BlockPos pos, IBlockAccess w)
	{
		return this.getState() == EnumShrubState.NORMAL || this.getState() == EnumShrubState.BLOOMING ? state.getValue(ExPBlockProperties.SHRUB_TYPE) == EnumShrubType.CERCIS_CANADENSIS ? -1 : w.getBlockState(pos.down()).getBlock() instanceof IShrub ? ((IShrub)w.getBlockState(pos.down()).getBlock()).getShrubColor(w.getBlockState(pos.down()), pos.down(), w) : w.getBlockState(pos.down()).getBlock() instanceof IGrass ? ((IGrass)w.getBlockState(pos.down()).getBlock()).getGrassColor(w.getBlockState(pos.down()), pos.down(), w) : w.getBiome(pos).getGrassColorAtPos(pos) : -1;
	}

	@Override
	public EnumShrubState getState()
	{
		return this.shrubState;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos);
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockShrub).forEach(s -> { 
			OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)); 
			AtomicInteger i = new AtomicInteger(0);
			Stream.of(ExPOreDict.bushNames).forEach(ss -> OreDictionary.registerOre(s + Character.toUpperCase(ss.charAt(0)) + ss.substring(1), new ItemStack(this, 1, i.getAndIncrement())));
		});
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWithMetadata(this));
	}

	@Override
	public int getShrubInternalType()
	{
		return 0;
	}
}
