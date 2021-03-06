package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.block.IChiselable;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.data.*;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.metal.EnumToolStats;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.item.ItemRock;
import v0id.exp.item.ItemToolHead;
import v0id.exp.net.ExPNetwork;
import v0id.exp.tile.TileWorkedBoulder;

import java.util.Random;
import java.util.stream.Stream;

import static v0id.api.exp.block.property.EnumRockClass.ANDESITE;
import static v0id.api.exp.data.ExPBlockProperties.ROCK_CLASS;

public class BlockWorkedBoulder extends Block implements IOreDictEntry, IItemBlockProvider, IChiselable
{
	public static final AxisAlignedBB BOULDER_AABB = new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.6, 0.9);

	public BlockWorkedBoulder()
	{
		super(Material.ROCK);
		this.setHardness(4);
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockBoulderWorked));
		this.setResistance(2);
		this.setSoundType(SoundType.STONE);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROCK_CLASS, ANDESITE).withProperty(ExPBlockProperties.WORKED_BOULDER_INDEX, 0));
		this.setCreativeTab(ExPCreativeTabs.tabUnderground);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.AIR;
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
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ROCK_CLASS, ExPBlockProperties.WORKED_BOULDER_INDEX);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile != null && tile instanceof TileWorkedBoulder)
		{
			return state.withProperty(ExPBlockProperties.WORKED_BOULDER_INDEX, (int)((TileWorkedBoulder)tile).workedIndex);
		}

		return super.getActualState(state, worldIn, pos);
	}

	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state)
	{
		return new TileWorkedBoulder();
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (playerIn != null && hand == EnumHand.MAIN_HAND)
		{
			ItemStack held = playerIn.getHeldItem(hand);
			if (!held.isEmpty() && held.getItem() instanceof ItemRock || held.getItem() == Items.FLINT)
			{
				for (int i = 0; i < 16; ++i)
				{
					worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX() + worldIn.rand.nextDouble(), pos.getY() + worldIn.rand.nextDouble(), pos.getZ() + worldIn.rand.nextDouble(), 0, 0, 0, Block.getStateId(state));
				}
				
				if (!worldIn.isRemote)
				{
					if (worldIn.rand.nextInt(8) == 0)
					{
						TileWorkedBoulder twb = (TileWorkedBoulder) worldIn.getTileEntity(pos);
						worldIn.playSound(null, pos, SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.BLOCKS, 1, 0.1f);
						if (twb.workedIndex == 7)
						{
							worldIn.setBlockToAir(pos);
						}
						else
						{
							++twb.workedIndex;
							ExPNetwork.sendTileData(twb, true);
							worldIn.notifyBlockUpdate(pos, state, state.withProperty(ExPBlockProperties.WORKED_BOULDER_INDEX, (int)twb.workedIndex), 3);
						}
						
						held.shrink(1);
					}
					else
					{
						worldIn.playSound(null, pos, SoundEvents.BLOCK_ANVIL_STEP, SoundCategory.BLOCKS, 1, 2f);
						if (worldIn.rand.nextBoolean() && worldIn.rand.nextBoolean())
						{
							if (held.getItem() == Items.FLINT)
                            {
                                return true;
                            }

							held.shrink(1);
						}
					}
				}
				
				return true;
			}
			else
			{
				if (held.isEmpty())
				{
					TileWorkedBoulder twb = (TileWorkedBoulder) worldIn.getTileEntity(pos);
					ItemStack ret = ItemStack.EMPTY;
					if (twb.workedIndex > 0)
					{
					    EnumToolClass[] possibleClasses = new EnumToolClass[]{ EnumToolClass.HAMMER, EnumToolClass.AXE, EnumToolClass.SHOVEL, EnumToolClass.SPEAR, EnumToolClass.KNIFE, EnumToolClass.CHISEL, null };
						if (possibleClasses[twb.workedIndex - 1] != null)
                        {
                            ret = ItemToolHead.createToolHead(possibleClasses[twb.workedIndex - 1], EnumToolStats.STONE);
                        }
					}
					
					if (!ret.isEmpty())
					{
						worldIn.setBlockToAir(pos);
						playerIn.dropItem(ret, false);
						return true;
					}
				}
			}
		}
		
		return false;
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockBoulder).forEach(s -> OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)));
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWithMetadata(this));
	}

	@Override
	public IBlockState chisel(IBlockState original, World world, BlockPos pos)
	{
		TileWorkedBoulder twb = (TileWorkedBoulder) world.getTileEntity(pos);
		if (twb.workedIndex == 7)
		{
			return Blocks.AIR.getDefaultState();
		}
		else
		{
			++twb.workedIndex;
			ExPNetwork.sendTileData(twb, true);
			return original;
		}
	}
}
