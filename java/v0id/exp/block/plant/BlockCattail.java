package v0id.exp.block.plant;

import static v0id.api.exp.block.property.EnumDirtClass.ACRISOL;
import static v0id.api.exp.block.property.ExPBlockProperties.DIRT_CLASS;

import java.util.Random;

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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import v0id.api.core.util.ItemBlockWithMetadata;
import v0id.api.exp.block.IWater;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.util.Helpers;

public class BlockCattail extends Block implements IWeightProvider
{
	public BlockCattail()
	{
		super(Material.GRASS);
		this.initBlock();
	}

	public void initBlock()
	{
		this.setHardness(0.5f);
		this.setRegistryName(ExPRegistryNames.blockCattail);
		this.setResistance(0);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(DIRT_CLASS, ACRISOL));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlockWithMetadata(this));
		this.setTickRandomly(true);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumDirtClass.values().length; ++i)
		{
			list.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(DIRT_CLASS, EnumDirtClass.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(DIRT_CLASS).ordinal();
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(state);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DIRT_CLASS);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		super.onNeighborChange(world, pos, neighbor);
		this.doPlacementChecks(world, pos);
	}
	
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
	}
	
	public void doPlacementChecks(IBlockAccess world, BlockPos pos)
	{
		if (world instanceof World)
		{
			World w = (World) world;
			if (!Helpers.canPlantGrow(pos.up(), w))
			{
				this.breakReplaceSelf(w, pos);
				return;
			}
			
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
			
			if (w.getBlockState(pos.up()).getBlock() instanceof IWater && ((IWater)w.getBlockState(pos.up()).getBlock()).isSalt(w, pos.up()))
			{
				this.breakReplaceSelf(w, pos);
				return;
			}
		}
	}
	
	public void breakReplaceSelf(World w, BlockPos pos)
	{
		w.setBlockState(pos, ExPBlocks.soil.getDefaultState().withProperty(DIRT_CLASS, w.getBlockState(pos).getValue(DIRT_CLASS)));
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
	public float provideVolume(ItemStack item)
	{
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.AIR;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		worldIn.setBlockState(pos, ExPBlocks.soil.getDefaultState().withProperty(DIRT_CLASS, state.getValue(DIRT_CLASS)));
	}
}
