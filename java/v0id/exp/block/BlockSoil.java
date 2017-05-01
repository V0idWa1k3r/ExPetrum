package v0id.exp.block;

import static v0id.api.exp.block.property.EnumDirtClass.ACRISOL;
import static v0id.api.exp.block.property.ExPBlockProperties.DIRT_CLASS;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import v0id.api.core.util.ItemBlockWithMetadata;
import v0id.api.exp.block.ICanGrowCrop;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.gravity.GravityHelper;
import v0id.api.exp.gravity.IGravitySusceptible;
import v0id.api.exp.inventory.IWeightProvider;

public class BlockSoil extends Block implements IWeightProvider, IGravitySusceptible, ICanGrowCrop, IInitializableBlock
{
	public BlockSoil()
	{
		super(Material.GROUND);
		this.initBlock();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumDirtClass.values().length; ++i)
		{
			list.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(DIRT_CLASS, EnumDirtClass.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(DIRT_CLASS).ordinal();
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(state);
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other instanceof net.minecraft.block.BlockDirt;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DIRT_CLASS);
	}

	@Override
	public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos,	Predicate<IBlockState> target)
	{
		return target.apply(Blocks.DIRT.getDefaultState()) || super.isReplaceableOreGen(state, world, pos, target);
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return "spade".equals(type) || "shovel".equals("type");
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return 0;
	}

	@Override
	public float provideVolume(ItemStack item)
	{
		return 0;
	}

	@Override
	public int getFallDamage(Entity collidedWith, EntityFallingBlock self)
	{
		return 3;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.onNeighborChange(worldIn, pos, fromPos);
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
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		this.onNeighborChange(worldIn, pos, pos);
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,IPlantable plantable)
	{
		EnumDirtClass type = EnumDirtClass.values()[this.getMetaFromState(state)];
		switch (plantable.getPlantType(world, pos))
		{
			case Plains:
			{
				return type.getNutrientMultiplier() > 0.3;
			}
			
			case Desert:
			{
				return type.getNutrientMultiplier() > 0.2 && type.getHumidity() < 0.7;
			}
			
			case Beach:
			{
				return type.getNutrientMultiplier() > 0.2 && type.getHumidity() > 0.5;
			}
			
			case Cave:
			{
				return type.getNutrientMultiplier() > 0.1;
			}
			
			case Water:
			{
				return type.getHumidity() >= 1;
			}
			
			case Nether:
			{
				return type.getHumidity() <= 0.1;
			}
			
			case Crop:
			{
				return type.getGrowthMultiplier() >= 1 && type.getNutrientMultiplier() >= 1;
			}
			
			default:
			{
				return false;
			}
		}
	}

	@Override
	public boolean isFertile(World world, BlockPos pos)
	{
		EnumDirtClass type = EnumDirtClass.values()[this.getMetaFromState(world.getBlockState(pos))];
		return type.getHumidity() >= 1;
	}

	@Override
	public float getNutrientLevel(World w, BlockPos pos)
	{
		EnumDirtClass type = EnumDirtClass.values()[this.getMetaFromState(w.getBlockState(pos))];
		return type.getNutrientMultiplier() / 10;
	}

	@Override
	public float getHumidityLevel(World w, BlockPos pos)
	{
		EnumDirtClass type = EnumDirtClass.values()[this.getMetaFromState(w.getBlockState(pos))];
		return type.getHumidity() / 2;
	}

	@Override
	public float getGrowthMultiplier(World w, BlockPos pos)
	{
		EnumDirtClass type = EnumDirtClass.values()[this.getMetaFromState(w.getBlockState(pos))];
		return type.getGrowthMultiplier() / 50;
	}

	@Override
	public void initBlock()
	{
		this.setHardness(3.5f);
		this.setRegistryName(ExPRegistryNames.blockSoil);
		this.setResistance(3);
		this.setSoundType(SoundType.GROUND);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(DIRT_CLASS, ACRISOL));
		this.setCreativeTab(ExPCreativeTabs.tabCommon);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlockWithMetadata(this));
	}
}
