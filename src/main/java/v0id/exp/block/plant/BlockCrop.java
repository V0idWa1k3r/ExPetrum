package v0id.exp.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.api.exp.tile.crop.ExPCropCapability;
import v0id.api.exp.tile.crop.ExPFarmlandCapability;
import v0id.api.exp.tile.crop.IExPCrop;
import v0id.exp.block.IItemBlockProvider;
import v0id.exp.block.item.ItemBlockWeighted;
import v0id.exp.crop.ExPCrop;
import v0id.exp.item.ItemFood;
import v0id.exp.tile.TileCrop;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockCrop extends Block implements IItemBlockProvider, IPlantable
{
	public static final AxisAlignedBB SEEDS_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.05, 1);
	
	public BlockCrop()
	{
		super(Material.PLANTS);
		this.setHardness(4F);
		this.setResistance(0);
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockCrop));
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.CROP_GROWTH_STAGE, 0).withProperty(ExPBlockProperties.CROP_TYPE, EnumCrop.DEAD));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setTickRandomly(true);
		this.setHarvestLevel("scythe", 0);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
    @SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWeighted(this));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		return state.getValue(ExPBlockProperties.CROP_TYPE) == EnumCrop.DEAD || state.getValue(ExPBlockProperties.CROP_TYPE) == EnumCrop.BEANS || state.getValue(ExPBlockProperties.CROP_TYPE) == EnumCrop.TOMATO ? FULL_BLOCK_AABB : state.getValue(ExPBlockProperties.CROP_GROWTH_STAGE) == 0 ? SEEDS_AABB : FULL_BLOCK_AABB;
    }

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileCrop)
		{
			ExPCrop crop = ((TileCrop) tile).cropState;
			if (crop.getType() != null && crop.getType() != EnumCrop.DEAD)
			{
				return new ItemStack(ExPItems.food, 1, ItemFood.getMetadataFromCrop(crop.getType()));
			}
		}

		return super.getPickBlock(state, target, world, pos, player);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		this.growthChecks(worldIn, pos, state);
		if (worldIn.getBlockState(pos) == state)
		{
			IExPCrop data = IExPCrop.of(worldIn.getTileEntity(pos));
			data.onWorldTick();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.growthChecks(worldIn, pos, state);
	}
	
	public void growthChecks(World world, BlockPos pos, IBlockState state)
	{
		if (world.getTileEntity(pos) != null)
		{
			IExPCrop data = IExPCrop.of(world.getTileEntity(pos));
			if (data.getType() != EnumCrop.DEAD && !data.isWild())
			{
				if (world.getTileEntity(pos.down()) == null || !world.getTileEntity(pos.down()).hasCapability(ExPFarmlandCapability.farmlandCap, EnumFacing.UP))
				{
					world.setBlockToAir(pos);
					return;
				}
			}
			
			if (world.isAirBlock(pos.down()) || !world.getBlockState(pos.down()).getBlock().canSustainPlant(world.getBlockState(pos.down()), world, pos.down(), EnumFacing.UP, this))
			{
				world.setBlockToAir(pos);
			}
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return world.getTileEntity(pos).hasCapability(ExPCropCapability.cropCap, null) && IExPCrop.of(world.getTileEntity(pos)).isWild() ? EnumPlantType.Plains : EnumPlantType.Crop;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState();
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return this.getExtendedState(state, worldIn, pos);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.AIR;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
		entityIn.motionX *= 0.4;
		entityIn.motionZ *= 0.4;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos).hasCapability(ExPCropCapability.cropCap, null))
		{
			IExPCrop crop = IExPCrop.of(worldIn.getTileEntity(pos));
			Pair<EnumActionResult, NonNullList<ItemStack>> ret = crop.onHarvest(player, worldIn, pos, state, EnumHand.MAIN_HAND, player.getHeldItemMainhand(), false);
			if (ret.getLeft() == EnumActionResult.SUCCESS)
			{
				for (ItemStack is : ret.getRight())
				{
					EntityItem drop = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, is.copy());
					worldIn.spawnEntity(drop);
				}
			}
		}
		
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other == Blocks.WHEAT || other == Blocks.CARROTS || other == Blocks.POTATOES || other == Blocks.BEETROOTS || other instanceof net.minecraft.block.BlockCrops;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ExPBlockProperties.CROP_GROWTH_STAGE, ExPBlockProperties.CROP_TYPE);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if (world.getTileEntity(pos) == null)
		{
			return state;
		}
		
		IExPCrop data = IExPCrop.of(world.getTileEntity(pos));
		return this.getDefaultState().withProperty(ExPBlockProperties.CROP_GROWTH_STAGE, data.getGrowthIndex()).withProperty(ExPBlockProperties.CROP_TYPE, data.getType());
	}

	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state)
	{
		return new TileCrop();
	}

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return this.getExtendedState(this.getDefaultState(), world, pos);
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
    {
        return "scythe";
    }

	@SuppressWarnings("deprecation")
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		// Axes are hardcoded to break blocks with a material of PLANT at their efficiency speed, need to fix that.
		ItemStack stack = player.getHeldItemMainhand();
		return !stack.isEmpty() && stack.getItem() instanceof ItemAxe ? super.getPlayerRelativeBlockHardness(state, player, worldIn, pos) / stack.getItem().getDestroySpeed(stack, state) : super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		
		if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos).hasCapability(ExPCropCapability.cropCap, null))
		{
			IExPCrop crop = IExPCrop.of(worldIn.getTileEntity(pos));
			Pair<EnumActionResult, NonNullList<ItemStack>> ret = crop.onHarvest(playerIn, worldIn, pos, state, hand, playerIn.getHeldItem(hand), true);
			if (ret.getLeft() == EnumActionResult.SUCCESS)
			{
				for (ItemStack is : ret.getRight())
				{
					EntityItem drop = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, is.copy());
					worldIn.spawnEntity(drop);
				}
			}
		}
		
		return false;
	}
}
