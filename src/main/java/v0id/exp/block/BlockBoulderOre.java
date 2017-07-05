package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.data.*;
import v0id.exp.block.item.ItemBlockBoulderOre;
import v0id.exp.tile.TileOre;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static v0id.api.exp.block.property.EnumRockClass.ANDESITE;
import static v0id.api.exp.data.ExPBlockProperties.ROCK_CLASS;

public class BlockBoulderOre extends Block implements IInitializableBlock, IOreDictEntry, IItemBlockProvider
{
    public static final AxisAlignedBB BOULDER_AABB = new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.6, 0.9);

	public BlockBoulderOre()
	{
		super(Material.ROCK);
		this.initBlock();
	}
	
	@Override
	public void initBlock()
	{
		this.setHardness(4);
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockBoulderOre));
		this.setResistance(2);
		this.setSoundType(SoundType.STONE);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROCK_CLASS, ANDESITE).withProperty(ExPBlockProperties.ORE_TEXTURE_ID, 0));
		this.setCreativeTab(ExPCreativeTabs.tabOres);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ExPItems.rock;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return 3 + random.nextInt(4);
    }

	@Override
	public int damageDropped(IBlockState state)
    {
        return state.getValue(ROCK_CLASS).ordinal();
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumRockClass.values().length * EnumOre.values().length; ++i)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		int oreIndex = (stack.getMetadata() / EnumRockClass.values().length) % EnumOre.values().length;
        tooltip.add(I18n.format("exp.block.ore.desc.type." + EnumOre.values()[oreIndex].getName()));
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
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ROCK_CLASS, ExPBlockProperties.ORE_TEXTURE_ID);
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
	
	@SuppressWarnings("deprecation")
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOULDER_AABB;
	}

	@SuppressWarnings("deprecation")
    @Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return this.getBoundingBox(blockState, worldIn, pos);
	}
	
    @SuppressWarnings("deprecation")
    @Override
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }

	@SuppressWarnings("deprecation")
    @Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@SuppressWarnings("deprecation")
    @Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}
	
	@Override
	public EnumOffsetType getOffsetType()
	{
		return EnumOffsetType.XZ;
	}

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		super.onNeighborChange(world, pos, neighbor);
		if (world instanceof World)
		{
			this.neighborChanged(world.getBlockState(pos), (World) world, pos, this, neighbor);
		}
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockBoulderOre).forEach(s -> OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)));
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockBoulderOre(this));
	}
}
