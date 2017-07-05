package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.*;
import v0id.api.exp.gravity.GravityHelper;
import v0id.api.exp.gravity.IGravitySusceptible;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;

import java.util.stream.Stream;

public class BlockCoralRock extends Block implements IWeightProvider, IGravitySusceptible, IInitializableBlock, IOreDictEntry, IItemBlockProvider
{
	public static final PropertyInteger TEXTURE_INDEX_ROCK = PropertyInteger.create("rtindex", 0, 5);
	
	public BlockCoralRock()
	{
		super(Material.ROCK);
		this.initBlock();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, TEXTURE_INDEX_ROCK);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return 0;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return Pair.of((byte)2, (byte)2);
	}

	@Override
	public int getFallDamage(Entity collidedWith, EntityFallingBlock self)
	{
		return 20;
	}

	@SuppressWarnings("deprecation")
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
			if (this.canFall(w, world.getBlockState(pos), pos, neighbor) && w.rand.nextBoolean())
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
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		ExPMisc.modelVariantRandom.setSeed(MathHelper.getPositionRandom(pos));
		return state.withProperty(TEXTURE_INDEX_ROCK, ExPMisc.modelVariantRandom.nextInt(6));
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return this.getExtendedState(state, worldIn, pos);
	}

	@Override
	public void initBlock()
	{
		this.setHardness(8);
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockCoralRock));
		this.setResistance(10);
		this.setSoundType(SoundType.STONE);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(TEXTURE_INDEX_ROCK, 0));
		this.setCreativeTab(ExPCreativeTabs.tabCommon);
	}
	
	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockCoralRock).forEach(s -> OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)));
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWithMetadata(this));
	}
}
