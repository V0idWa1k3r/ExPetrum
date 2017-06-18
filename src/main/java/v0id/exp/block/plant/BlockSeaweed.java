package v0id.exp.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.IWater;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.IBlockRegistryEntry;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.handler.ExPHandlerRegistry;

import java.util.Random;

import static v0id.api.exp.block.property.EnumRockClass.ANDESITE;
import static v0id.api.exp.data.ExPBlockProperties.ROCK_CLASS;

public class BlockSeaweed extends Block implements IWeightProvider, IInitializableBlock, IBlockRegistryEntry, IItemRegistryEntry
{
	public BlockSeaweed()
	{
		super(Material.GRASS);
		this.initBlock();
	}

	@Override
	public void initBlock()
	{
		this.setHardness(1f);
		this.setRegistryName(ExPRegistryNames.blockSeaweed);
		this.setResistance(0);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROCK_CLASS, ANDESITE));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setTickRandomly(true);
		ExPHandlerRegistry.put(this);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumRockClass.values().length; ++i)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(ROCK_CLASS, EnumRockClass.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ROCK_CLASS).ordinal();
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(state);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ROCK_CLASS);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		super.onNeighborChange(world, pos, neighbor);
		this.doPlacementChecks(world, pos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.onNeighborChange(worldIn, pos, fromPos);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		this.doPlacementChecks(worldIn, pos);
		trySpread(worldIn, pos, rand);
	}

	private void trySpread(World worldIn, BlockPos pos, Random rand)
	{
		if (rand.nextDouble() <= ExPMisc.SEAWEED_GROWTH_RATE)
		{
			BlockPos offset = pos.add(worldIn.rand.nextInt(ExPMisc.SEAWEED_SPREAD_OFFSET) - worldIn.rand.nextInt(ExPMisc.SEAWEED_SPREAD_OFFSET), 1 + worldIn.rand.nextInt(ExPMisc.SEAWEED_SPREAD_OFFSET) - worldIn.rand.nextInt(ExPMisc.SEAWEED_SPREAD_OFFSET), worldIn.rand.nextInt(ExPMisc.SEAWEED_SPREAD_OFFSET) - worldIn.rand.nextInt(ExPMisc.SEAWEED_SPREAD_OFFSET));
			BlockPos belowOffset = offset.down();
			IBlockState atOffset = worldIn.getBlockState(offset);
			IBlockState atBelow = worldIn.getBlockState(belowOffset);
			if (atOffset.getBlock() instanceof IWater && ((IWater)atOffset.getBlock()).isSalt(worldIn, offset) && atBelow.getBlock().isAssociatedBlock(ExPBlocks.sand))
			{
				for (int dx = -1; dx <= 1; ++dx)
				{
					for (int dy = -1; dy <= 1; ++dy)
					{
						for (int dz = -1; dz <= 1; ++dz)
						{
							if (dx != 0 || dz != 0 || dy != 0)
							{
								IBlockState stateCheck = worldIn.getBlockState(offset.add(dx, dy, dz));
								if (stateCheck.getBlock() instanceof BlockSeaweed)
								{
									return;
								}
							}
						}
					}
				}
				
				worldIn.setBlockState(belowOffset, this.getDefaultState().withProperty(ROCK_CLASS, atBelow.getValue(ROCK_CLASS)));
			}
		}
	}
	
	public void doPlacementChecks(IBlockAccess world, BlockPos pos)
	{
		if (world instanceof World)
		{
			World w = (World) world;
			if (w.isAirBlock(pos.down()) || !w.getBlockState(pos.down()).isSideSolid(w, pos.down(), EnumFacing.UP))
			{
				this.breakReplaceSelf(w, pos);
				return;
			}
			
			if (w.getBlockState(pos.up()).getMaterial() != Material.WATER)
			{
				this.breakReplaceSelf(w, pos);
				return;
			}
			
			if (w.getBlockState(pos.up()).getBlock() instanceof IWater && !((IWater)w.getBlockState(pos.up()).getBlock()).isSalt(w, pos.up()))
			{
				this.breakReplaceSelf(w, pos);
            }
		}
	}
	
	public void breakReplaceSelf(World w, BlockPos pos)
	{
		w.setBlockState(pos, ExPBlocks.sand.getDefaultState().withProperty(ROCK_CLASS, w.getBlockState(pos).getValue(ROCK_CLASS)));
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return "spade".equals(type) || "shovel".equals("type");
	}
	
	@Override
	public float provideWeight(ItemStack item)
	{
		return 0;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		// TODO Auto-generated method stub
		return Pair.of((byte)1, (byte)1);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.AIR;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		worldIn.setBlockState(pos, ExPBlocks.sand.getDefaultState().withProperty(ROCK_CLASS, state.getValue(ROCK_CLASS)));
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
