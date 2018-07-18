package v0id.exp.block;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.IChiselable;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.data.*;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static v0id.api.exp.block.property.EnumRockClass.ANDESITE;
import static v0id.api.exp.data.ExPBlockProperties.ROCK_CLASS;

public class BlockDecoratedStone extends Block implements IChiselable, IWeightProvider, IOreDictEntry, IItemBlockProvider
{
	public enum EnumDecorationType
    {
        TILE,
        BRICK,
        SMALL_BRICK
    }

    private EnumDecorationType type;

	public BlockDecoratedStone(EnumDecorationType type)
	{
		super(Material.ROCK);
		this.type = type;
		this.setHardness(3);
		this.setRegistryName(ExPRegistryNames.asLocation(this.type == EnumDecorationType.TILE ? ExPRegistryNames.blockDecoratedStoneTile : this.type == EnumDecorationType.BRICK ? ExPRegistryNames.blockDecoratedStoneBrick : ExPRegistryNames.blockDecoratedStoneBrickSmall));
		this.setResistance(10);
		this.setSoundType(SoundType.STONE);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROCK_CLASS, ANDESITE));
		this.setCreativeTab(ExPCreativeTabs.tabUnderground);
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

	@SuppressWarnings("deprecation")
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
		return super.isAssociatedBlock(other) || other instanceof net.minecraft.block.BlockStone;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ROCK_CLASS);
	}

	@Override
	public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, @SuppressWarnings("Guava") Predicate<IBlockState> target)
	{
		return target.apply(Blocks.STONE.getDefaultState()) || super.isReplaceableOreGen(state, world, pos, target);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return world.getBlockState(pos).getValue(ROCK_CLASS).getResistance();
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return 1;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return Pair.of((byte)2, (byte)2);
	}
	
	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockStone).forEach(s -> { 
			OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)); 
			AtomicInteger i = new AtomicInteger(0);
			Stream.of(ExPOreDict.rockNames).forEach(ss -> OreDictionary.registerOre(s + Character.toUpperCase(ss.charAt(0)) + ss.substring(1), new ItemStack(this, 1, i.getAndIncrement())));
		});
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWithMetadata(this));
	}

    @Override
    public IBlockState chisel(IBlockState original, World world, BlockPos pos)
    {
        return this == ExPBlocks.rockDeco0 ? ExPBlocks.rockDeco1.getDefaultState().withProperty(ROCK_CLASS, original.getValue(ROCK_CLASS)) : this == ExPBlocks.rockDeco1 ? ExPBlocks.rockDeco2.getDefaultState().withProperty(ROCK_CLASS, original.getValue(ROCK_CLASS)) : original;
    }
}
