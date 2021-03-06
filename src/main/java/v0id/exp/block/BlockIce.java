package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.data.*;
import v0id.api.exp.gravity.GravityHelper;
import v0id.api.exp.gravity.IGravitySusceptible;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.util.Helpers;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class BlockIce extends Block implements IGravitySusceptible, IOreDictEntry, IItemBlockProvider
{
	public BlockIce()
	{
		super(Material.ICE);
		this.setHardness(8);
		this.setResistance(0);
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockIce));
		this.setSoundType(SoundType.GLASS);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.ICE_IS_SALT, false));
		this.setCreativeTab(ExPCreativeTabs.tabCommon);
		this.setTickRandomly(true);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.AIR;
	}

	@SuppressWarnings("deprecation")
	@Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        return block != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

	@Override
	public int getFallDamage(Entity collidedWith, EntityFallingBlock self)
	{
		return 5;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.NORMAL;
    }

	@Override
	public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity entity)
	{
		return 0.98F;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(ExPBlockProperties.ICE_IS_SALT, meta == 1);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ExPBlockProperties.ICE_IS_SALT) ? 1 : 0;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		if (this.canFall(worldIn, worldIn.getBlockState(pos), pos, pos) && worldIn.getBlockState(pos.down()).getMaterial() != Material.WATER)
		{
			GravityHelper.doFall(worldIn.getBlockState(pos), worldIn, pos, pos);
			return;
		}
		
		this.tryMelt(worldIn, pos, state, rand);
	}
	
	public void tryMelt(World world, BlockPos pos, IBlockState state, Random rand)
	{
		float temp = Helpers.getTemperatureAt(world, pos);
		if (temp > 0 && rand.nextFloat() < temp / 10 && world.isAirBlock(pos.up()))
		{
			boolean salt = state.getValue(ExPBlockProperties.ICE_IS_SALT);
			world.setBlockState(pos, salt ? ExPBlocks.saltWater.getDefaultState().withProperty(BlockFluidBase.LEVEL, 9) : ExPBlocks.freshWater.getDefaultState().withProperty(BlockFluidBase.LEVEL, 9));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.onNeighborChange(worldIn, pos, fromPos);
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ExPBlockProperties.ICE_IS_SALT);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		list.add(new ItemStack(this, 1, 0));
		list.add(new ItemStack(this, 1, 1));
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		super.onNeighborChange(world, pos, neighbor);
		if (world instanceof World)
		{
			if (this.canFall((World) world, world.getBlockState(pos), pos, neighbor) && world.getBlockState(pos.down()).getMaterial() != Material.WATER)
			{
				GravityHelper.doFall(world.getBlockState(pos), (World) world, pos, neighbor);
			}
		}
	}
	
	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockIce).forEach(s -> OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)));
		Stream.of(ExPOreDict.blockIceFresh).forEach(s -> OreDictionary.registerOre(s, new ItemStack(this, 1, 0)));
		Stream.of(ExPOreDict.blockIceSalt).forEach(s -> OreDictionary.registerOre(s, new ItemStack(this, 1, 1)));
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWithMetadata(this));
	}
}
