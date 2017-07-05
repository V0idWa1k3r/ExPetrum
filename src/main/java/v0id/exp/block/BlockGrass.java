package v0id.exp.block;

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
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.core.util.java.ColorRGB;
import v0id.api.exp.block.EnumGrassAmount;
import v0id.api.exp.block.EnumGrassState;
import v0id.api.exp.block.IGrass;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.data.*;
import v0id.api.exp.gravity.GravityHelper;
import v0id.api.exp.gravity.IGravitySusceptible;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.block.plant.BlockShrub;
import v0id.exp.util.Helpers;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static v0id.api.exp.block.property.EnumDirtClass.ACRISOL;
import static v0id.api.exp.data.ExPBlockProperties.DIRT_CLASS;

public class BlockGrass extends Block implements IWeightProvider, IGravitySusceptible, IGrass, IInitializableBlock, IOreDictEntry, IItemBlockProvider
{
	public BlockGrass()
	{
		super(Material.GRASS);
		initBlock();
		this.setTickRandomly(true);
	}

	@Override
	public void initBlock()
	{
		this.setHardness(3.5f);
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockGrass));
		this.setResistance(3);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(DIRT_CLASS, ACRISOL));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		return plantable instanceof BlockShrub || this.getState() != EnumGrassState.DEAD && plantable.getPlantType(world, pos.up()) == EnumPlantType.Plains;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		float growthRateModifier = Helpers.getGenericGrowthModifier(pos, worldIn, false);
		if (!worldIn.isRemote)
		{
			trySpread(worldIn, pos, rand, growthRateModifier);
			checkState(worldIn, pos, state, rand, growthRateModifier);
			checkBeingDeletedByBlockAbove(worldIn, pos, state);
		}
	}

	public void checkBeingDeletedByBlockAbove(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isAirBlock(pos.up()) && worldIn.getBlockState(pos.up()).isSideSolid(worldIn, pos.up(), EnumFacing.DOWN))
		{
			worldIn.setBlockState(pos, ExPBlocks.soil.getDefaultState().withProperty(DIRT_CLASS, state.getValue(DIRT_CLASS)));
		}
	}

	public void checkState(World worldIn, BlockPos pos, IBlockState state, Random rand, float growthRateModifier)
	{
		if (rand.nextInt(64) != 0)
		{
			return;
		}
		
		EnumGrassState suggested = Helpers.getSuggestedGrassState(pos, worldIn);
		if (suggested != this.getState())
		{
			switch (this.getState())
			{
				case NORMAL:
				{
					if (rand.nextBoolean())
					{
						worldIn.setBlockState(pos, ExPBlocks.grass_dry.getDefaultState().withProperty(DIRT_CLASS, state.getValue(DIRT_CLASS)), 2);
						// TODO grass drying out effects
					}
					
					break;
				}
				case DRY:
				{
					if (suggested == EnumGrassState.NORMAL && rand.nextDouble() <= growthRateModifier)
					{
						worldIn.setBlockState(pos, ExPBlocks.grass.getDefaultState().withProperty(DIRT_CLASS, state.getValue(DIRT_CLASS)), 2);
						// TODO grass growing effects
					}
					
					if (suggested == EnumGrassState.DEAD)
					{
						worldIn.setBlockState(pos, ExPBlocks.grass_dead.getDefaultState().withProperty(DIRT_CLASS, state.getValue(DIRT_CLASS)), 2);
						// TODO grass dying out effects
					}
					
					break;
				}
				case DEAD:
				{
					if(rand.nextDouble() <= growthRateModifier)
					{
						worldIn.setBlockState(pos, ExPBlocks.grass_dry.getDefaultState().withProperty(DIRT_CLASS, state.getValue(DIRT_CLASS)), 2);
						// TODO grass growing effects
					}
					
					break;
				}
				
				default:
				{
					break;
				}
			}
		}
	}

	public void trySpread(World worldIn, BlockPos pos, Random rand, float growthRateModifier)
	{
		if (!Helpers.canPlantGrow(pos, worldIn))
		{
			return;
		}
		
		if (rand.nextInt(128) == 0 && worldIn.isAirBlock(pos.up()) && this.getState() == EnumGrassState.NORMAL)
		{
			worldIn.setBlockState(pos.up(), ExPBlocks.vegetation.getDefaultState(), 2);
		}
		
		BlockPos offset = pos.add(rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET) - rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET), rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET) - rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET), rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET) - rand.nextInt(ExPMisc.GRASS_SPREAD_OFFSET));
		IBlockState spreadToCheck = worldIn.getBlockState(offset);
		if (this.getState() != EnumGrassState.DEAD && spreadToCheck.getBlock() == ExPBlocks.soil && Helpers.canPlantGrow(offset, worldIn) && rand.nextDouble() <= growthRateModifier / (this.getState() == EnumGrassState.DRY ? 2 : 1))
		{
			EnumGrassState suggested = Helpers.getSuggestedGrassState(offset, worldIn);
			if (suggested == EnumGrassState.NORMAL)
			{
				worldIn.setBlockState(offset, ExPBlocks.grass.getDefaultState().withProperty(DIRT_CLASS, spreadToCheck.getValue(DIRT_CLASS)));
				// TODO growth effects!
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumDirtClass.values().length; ++i)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}

	@SuppressWarnings("deprecation")
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
		return super.isAssociatedBlock(other) || other instanceof net.minecraft.block.BlockGrass || other instanceof BlockGrass;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DIRT_CLASS);
	}

	@Override
	public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, @SuppressWarnings("Guava") Predicate<IBlockState> target)
	{
		return target.apply(Blocks.GRASS.getDefaultState()) || super.isReplaceableOreGen(state, world, pos, target);
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
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		// TODO Auto-generated method stub
		return Pair.of((byte)1, (byte)1);
	}

	@Override
	public int getFallDamage(Entity collidedWith, EntityFallingBlock self)
	{
		return 3;
	}

	@SuppressWarnings("deprecation")
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
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ExPBlocks.soil);
	}

	@Override
	public int getGrassColor(IBlockState state, BlockPos pos, IBlockAccess w)
	{
		int baseColor = this.getState() == EnumGrassState.NORMAL ? 0x02661c : this.getState() == EnumGrassState.DRY ? 0x6a7223 : 0x660000;
		int mulColor = w.getBiome(pos).getGrassColorAtPos(pos);
		ColorRGB rgbBase = ColorRGB.FromHEX(baseColor);
		ColorRGB mulBase = ColorRGB.FromHEX(mulColor);
		return ((int)((rgbBase.getR() + mulBase.getR()) * 127.5) << 16) + ((int)((rgbBase.getG() + mulBase.getG()) * 127.5) << 8) + (int)((rgbBase.getB() + mulBase.getB()) * 127.5);
	}

	@Override
	public EnumGrassState getState()
	{
		return EnumGrassState.NORMAL;
	}
	
	public static class Dry extends BlockGrass
	{
		public Dry()
		{
			super();
		}

		@Override
		public void initBlock()
		{
			this.setHardness(3.5f);
			this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockGrass_dry));
			this.setResistance(3);
			this.setSoundType(SoundType.PLANT);
			this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
			this.setDefaultState(this.blockState.getBaseState().withProperty(DIRT_CLASS, ACRISOL));
			this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		}

		@Override
		public EnumGrassState getState()
		{
			return EnumGrassState.DRY;
		}
	}
	
	public static class Dead extends BlockGrass
	{
		public Dead()
		{
			super();
		}

		@Override
		public void initBlock()
		{
			this.setHardness(3.5f);
			this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.blockGrass_dead));
			this.setResistance(3);
			this.setSoundType(SoundType.PLANT);
			this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
			this.setDefaultState(this.blockState.getBaseState().withProperty(DIRT_CLASS, ACRISOL));
			this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		}

		@Override
		public EnumGrassState getState()
		{
			return EnumGrassState.DEAD;
		}
	}

	@Override
	public EnumGrassAmount getAmount(IBlockState state, BlockPos pos, IBlockAccess w)
	{
		return state.getValue(DIRT_CLASS).getAmount();
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockGrass).forEach(s -> { 
			OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)); 
			AtomicInteger i = new AtomicInteger(0);
			Stream.of(ExPOreDict.soilNames).forEach(ss -> OreDictionary.registerOre(s + Character.toUpperCase(ss.charAt(0)) + ss.substring(1), new ItemStack(this, 1, i.getAndIncrement())));
		});
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockWithMetadata(this));
	}
}
