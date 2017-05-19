package v0id.exp.block.plant;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.exp.block.IBlockRegistryEntry;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.handler.ExPHandlerRegistry;

public class BlockCrop extends BlockBush implements IInitializableBlock, IBlockRegistryEntry, IItemRegistryEntry
{
	public static final AxisAlignedBB SEEDS_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.05, 1);
	
	public BlockCrop()
	{
		super();
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
	protected boolean canSustainBush(IBlockState state)
	{
		return true;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		// TODO Auto-generated method stub
		super.updateTick(worldIn, pos, state, rand);
	}

	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		// TODO Auto-generated method stub
		return super.canBlockStay(worldIn, pos, state);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Crop;
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
		EnumCrop crop = EnumCrop.values()[Math.abs(pos.getX()) % EnumCrop.values().length];
		return this.getDefaultState().withProperty(ExPBlockProperties.CROP_GROWTH_STAGE, Math.min(Math.abs(pos.getZ()) % 16, crop.getData() == null ? 0 : crop.getData().growthStages - 1)).withProperty(ExPBlockProperties.CROP_TYPE, crop);
	}

}
