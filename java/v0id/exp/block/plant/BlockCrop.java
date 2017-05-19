package v0id.exp.block.plant;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.api.exp.tile.crop.ExPCropCapability;
import v0id.api.exp.tile.crop.ExPFarmlandCapability;
import v0id.api.exp.tile.crop.IExPCrop;
import v0id.exp.block.IBlockRegistryEntry;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.handler.ExPHandlerRegistry;
import v0id.exp.tile.TileCrop;

public class BlockCrop extends BlockContainer implements IInitializableBlock, IBlockRegistryEntry, IItemRegistryEntry, IPlantable
{
	public static final AxisAlignedBB SEEDS_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.05, 1);
	
	public BlockCrop()
	{
		super(Material.PLANTS);
		this.initBlock();
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerBlock(IForgeRegistry<Block> registry)
	{
		registry.register(this);
	}

	@Override
	public void initBlock()
	{
		this.setHardness(4F);
		this.setResistance(0);
		this.setRegistryName(ExPRegistryNames.blockCrop);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.CROP_GROWTH_STAGE, 0).withProperty(ExPBlockProperties.CROP_TYPE, EnumCrop.DEAD));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setTickRandomly(true);
		ExPHandlerRegistry.blockEntries.add(this);
		ExPHandlerRegistry.itemEntries.add(this);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		return state.getValue(ExPBlockProperties.CROP_TYPE) == EnumCrop.DEAD || state.getValue(ExPBlockProperties.CROP_TYPE) == EnumCrop.BEANS || state.getValue(ExPBlockProperties.CROP_TYPE) == EnumCrop.TOMATO ? FULL_BLOCK_AABB : state.getValue(ExPBlockProperties.CROP_GROWTH_STAGE) == 0 ? SEEDS_AABB : FULL_BLOCK_AABB;
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
				return;
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

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState();
	}

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
		// TODO Auto-generated method stub
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
		IExPCrop data = IExPCrop.of(world.getTileEntity(pos));
		return this.getDefaultState().withProperty(ExPBlockProperties.CROP_GROWTH_STAGE, data.getGrowthIndex()).withProperty(ExPBlockProperties.CROP_TYPE, data.getType());
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileCrop();
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return this.getExtendedState(this.getDefaultState(), world, pos);
	}
}
