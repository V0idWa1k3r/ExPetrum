package v0id.exp.block;

import static v0id.api.exp.block.property.EnumRockClass.ANDESITE;
import static v0id.api.exp.block.property.ExPBlockProperties.ROCK_CLASS;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import v0id.api.core.util.ItemBlockWithMetadata;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.gravity.GravityHelper;
import v0id.api.exp.gravity.IGravitySusceptible;
import v0id.api.exp.inventory.IWeightProvider;

public class BlockSand extends Block implements IWeightProvider, IGravitySusceptible
{
	public BlockSand()
	{
		super(Material.SAND);
		this.setHardness(2);
		this.setRegistryName(ExPRegistryNames.blockSand);
		this.setResistance(5);
		this.setSoundType(SoundType.SAND);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROCK_CLASS, ANDESITE));
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlockWithMetadata(this));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumRockClass.values().length; ++i)
		{
			list.add(new ItemStack(itemIn, 1, i));
		}
	}

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
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return blockState.getValue(ROCK_CLASS).getHardness();
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(state);
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other instanceof net.minecraft.block.BlockSand;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ROCK_CLASS);
	}

	@Override
	public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos,	Predicate<IBlockState> target)
	{
		return target.apply(Blocks.SAND.getDefaultState()) || super.isReplaceableOreGen(state, world, pos, target);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return world.getBlockState(pos).getValue(ROCK_CLASS).getResistance();
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
	public int getFallDamage(Entity collidedWith, EntityFallingBlock self)
	{
		return 3;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.onNeighborChange(worldIn, pos, fromPos);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		super.onNeighborChange(world, pos, neighbor);
		if (world instanceof World)
		{
			World w = (World) world;
			if (this.canFall(w, world.getBlockState(pos), pos, neighbor))
			{
				GravityHelper.doFall(world.getBlockState(pos), w, pos, neighbor);
			}
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		this.onNeighborChange(worldIn, pos, pos);
	}
}
