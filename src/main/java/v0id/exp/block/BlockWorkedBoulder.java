package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.core.network.PacketType;
import v0id.api.core.network.VoidNetwork;
import v0id.api.core.util.DimBlockPos;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.*;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.handler.ExPHandlerRegistry;
import v0id.exp.item.ItemRock;
import v0id.exp.tile.TileWorkedBoulder;

import java.util.Random;
import java.util.stream.Stream;

import static v0id.api.exp.block.property.EnumRockClass.ANDESITE;
import static v0id.api.exp.data.ExPBlockProperties.ROCK_CLASS;

public class BlockWorkedBoulder extends Block implements ITileEntityProvider, IInitializableBlock, IOreDictEntry, IBlockRegistryEntry, IItemRegistryEntry
{
	public static final AxisAlignedBB BOULDER_AABB = new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.6, 0.9);

	public BlockWorkedBoulder()
	{
		super(Material.ROCK);
		this.initBlock();
	}
	
	@Override
	public void initBlock()
	{
		this.setHardness(4);
		this.setRegistryName(ExPRegistryNames.blockBoulderWorked);
		this.setResistance(2);
		this.setSoundType(SoundType.STONE);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROCK_CLASS, ANDESITE).withProperty(ExPBlockProperties.WORKED_BOULDER_INDEX, 0));
		this.setCreativeTab(ExPCreativeTabs.tabUnderground);
		ExPHandlerRegistry.put(this);
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
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileWorkedBoulder)
		{
			state = state.withProperty(ExPBlockProperties.WORKED_BOULDER_INDEX, (int)((TileWorkedBoulder)tile).workedIndex);
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
		return new TileWorkedBoulder();
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return this.BOULDER_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return this.getBoundingBox(blockState, worldIn, pos);
	}
	
    @Override
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
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

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,	EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (playerIn != null)
		{
			ItemStack held = playerIn.getHeldItem(hand);
			if (!held.isEmpty() && held.getItem() instanceof ItemRock)
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
							NBTTagCompound sent = new NBTTagCompound();
							sent.setTag("tileData", twb.serializeNBT());
							sent.setTag("blockPosData", new DimBlockPos(pos, worldIn.provider.getDimension()).serializeNBT());
							VoidNetwork.sendDataToClient(PacketType.TileData, sent, (EntityPlayerMP) playerIn);
							worldIn.notifyBlockUpdate(pos, state, state.withProperty(ExPBlockProperties.WORKED_BOULDER_INDEX, (int)twb.workedIndex), 3);
						}
						
						held.shrink(1);
					}
					else
					{
						worldIn.playSound(null, pos, SoundEvents.BLOCK_ANVIL_STEP, SoundCategory.BLOCKS, 1, 2f);
						if (worldIn.rand.nextBoolean() && worldIn.rand.nextBoolean())
						{
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
						ret = new ItemStack(ExPItems.toolHead, twb.workedIndex == 7 ? 3 : 1, twb.workedIndex - 1);
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
	public void registerBlock(IForgeRegistry<Block> registry)
	{
		registry.register(this);
	}
}
