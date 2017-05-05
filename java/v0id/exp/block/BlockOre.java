package v0id.exp.block;

import static v0id.api.exp.block.property.EnumRockClass.ANDESITE;
import static v0id.api.exp.block.property.ExPBlockProperties.ROCK_CLASS;

import java.util.List;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.exp.block.item.ItemBlockOre;
import v0id.exp.tile.TileOre;

public class BlockOre extends Block implements ITileEntityProvider, IInitializableBlock, IOreDictEntry
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
		this.setRegistryName(ExPRegistryNames.blockOre);
		this.setResistance(10);
		this.setSoundType(SoundType.STONE);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROCK_CLASS, ANDESITE).withProperty(ExPBlockProperties.ORE_TEXTURE_ID, 0));
		this.setCreativeTab(ExPCreativeTabs.tabOres);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlockOre(this));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumRockClass.values().length * EnumOre.values().length * 3; ++i)
		{
			list.add(new ItemStack(itemIn, 1, i));
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
	{
		int oreIndex = (stack.getMetadata() / EnumRockClass.values().length) % EnumOre.values().length;
		int oreRichnessIndex = stack.getMetadata() / (EnumRockClass.values().length * EnumOre.values().length);
		tooltip.add(I18n.format("exp.block.ore.desc.type." + EnumOre.values()[oreIndex].getName()));
		tooltip.add(I18n.format("exp.block.ore.desc.richness." + oreRichnessIndex));
		super.addInformation(stack, player, tooltip, advanced);
	}

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

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return this.getExtendedState(state, worldIn, pos);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileOre();
	}
	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
    }
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }
	
	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockOre).forEach(s -> OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)));
	}
}
