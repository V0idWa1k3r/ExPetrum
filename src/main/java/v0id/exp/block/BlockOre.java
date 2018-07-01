package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.data.*;
import v0id.exp.block.item.ItemBlockOre;
import v0id.exp.tile.TileOre;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static v0id.api.exp.block.property.EnumRockClass.ANDESITE;
import static v0id.api.exp.data.ExPBlockProperties.ROCK_CLASS;

public class BlockOre extends Block implements IInitializableBlock, IOreDictEntry, IItemBlockProvider
{
	public static final float ORE_HARDNESS_MODIFIER = 2F;

	public BlockOre()
	{
		super(Material.ROCK);
		this.initBlock();
	}
	
	@Override
	public void initBlock()
	{
		this.setHardness(3);
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockOre));
		this.setResistance(10);
		this.setSoundType(SoundType.STONE);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROCK_CLASS, ANDESITE).withProperty(ExPBlockProperties.ORE_TEXTURE_ID, 0));
		this.setCreativeTab(ExPCreativeTabs.tabOres);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumRockClass.values().length * EnumOre.values().length * 3; ++i)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		int oreIndex = (stack.getMetadata() / EnumRockClass.values().length) % EnumOre.values().length;
		int oreRichnessIndex = stack.getMetadata() / (EnumRockClass.values().length * EnumOre.values().length);
		tooltip.add(I18n.format("exp.block.ore.desc.type." + EnumOre.values()[oreIndex].getName()));
		tooltip.add(I18n.format("exp.block.ore.desc.richness." + oreRichnessIndex));
		super.addInformation(stack, player, tooltip, advanced);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(ROCK_CLASS, EnumRockClass.values()[meta % EnumRockClass.values().length]);
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
		return blockState.getValue(ROCK_CLASS).getHardness() + ORE_HARDNESS_MODIFIER;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ROCK_CLASS, ExPBlockProperties.ORE_TEXTURE_ID);
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return world.getBlockState(pos).getValue(ROCK_CLASS).getResistance();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileOre)
		{
			state = state.withProperty(ExPBlockProperties.ORE_TEXTURE_ID, ((TileOre)tile).type.getTextureIndex());
		}
		
		return state;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return this.getExtendedState(state, worldIn, pos);
	}

	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state)
	{
		return new TileOre();
	}

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
	
	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockOre).forEach(s -> OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)));
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockOre(this));
	}

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    @Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		super.getDrops(drops, world, pos, state, fortune);
		if (world.getTileEntity(pos) instanceof TileOre)
		{
			drops.add(((TileOre) world.getTileEntity(pos)).createDrops());
		}
	}
}
