package v0id.exp.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.gravity.GravityHelper;
import v0id.api.exp.gravity.IGravitySusceptible;
import v0id.exp.block.item.ItemBlockWeighted;
import v0id.exp.util.Helpers;

import java.util.Random;
import java.util.stream.Stream;

import static net.minecraft.block.BlockSnow.LAYERS;

public class BlockSnow extends Block implements IGravitySusceptible, IInitializableBlock, IOreDictEntry, IItemBlockProvider
{
	protected static final AxisAlignedBB[] SNOW_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

	public BlockSnow()
	{
		super(Material.SNOW);
		this.initBlock();
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.AIR;
	}

	@Override
	public void initBlock()
	{
		this.setHardness(0.1F);
		this.setResistance(0);
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockSnow));
		this.setSoundType(SoundType.SNOW);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(LAYERS, 1));
		this.setCreativeTab(ExPCreativeTabs.tabCommon);
		this.setTickRandomly(true);
	}

	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos) * (9 - state.getValue(LAYERS));
	}

	@Override
	public int getFallDamage(Entity collidedWith, EntityFallingBlock self)
	{
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(LAYERS, meta + 1);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(LAYERS) - 1;
	}

	@SuppressWarnings("deprecation")
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return 1 + blockState.getValue(LAYERS);
	}


	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return SNOW_AABB[state.getValue(LAYERS)];
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);
		if (this.canFall(world, world.getBlockState(pos), pos, pos))
		{
			GravityHelper.doFall(world.getBlockState(pos), world, pos, pos);
			return;
		}
		
		tryMelt(world, pos, state, rand);
	}

	public void tryMelt(World world, BlockPos pos, IBlockState state, Random rand)
	{
		float temp = Helpers.getTemperatureAt(world, pos);
		if (temp > 0 && rand.nextFloat() < temp / 10)
		{
			world.setBlockToAir(pos);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.onNeighborChange(worldIn, pos, fromPos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isTopSolid(IBlockState state)
    {
        return state.getValue(LAYERS) == 8;
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,	EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itm = playerIn.getHeldItemMainhand();
		if (!itm.isEmpty() && itm.getItem() instanceof ItemBlock && Block.getBlockFromItem(itm.getItem()) == this && state.getValue(LAYERS) < 8)
		{
			if (!playerIn.capabilities.isCreativeMode)
			{
				itm.shrink(1);
			}
			
			worldIn.setBlockState(pos, state.cycleProperty(LAYERS));
			return true;
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other == Blocks.SNOW_LAYER;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, LAYERS);
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return (int)(15F * (float)state.getValue(LAYERS) / 8F);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		super.onNeighborChange(world, pos, neighbor);
		if (world instanceof World)
		{
			World w = (World) world;
			if (this.canFall(w, world.getBlockState(pos), pos, neighbor))
			{
				GravityHelper.doFall(world.getBlockState(pos), w, pos, neighbor);
			}
		}
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockSnow).forEach(s -> OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)));
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWeighted(this));
	}
}
