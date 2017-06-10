package v0id.exp.block.tree;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.block.EnumLeafState;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.block.ILeaves;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPItems;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.block.IBlockRegistryEntry;
import v0id.exp.block.IInitializableBlock;
import v0id.exp.block.item.IItemRegistryEntry;
import v0id.exp.block.item.ItemBlockWithMetadata;
import v0id.exp.handler.ExPHandlerRegistry;
import v0id.exp.util.Helpers;

public class BlockLeaf extends Block implements ILeaves, IWeightProvider, IInitializableBlock, IOreDictEntry, IBlockRegistryEntry, IItemRegistryEntry
{
	// Log and not leaf because this is the index of the log this leaf is a leaf of
	// Sounds confusing enough? (>w<)
	public int logIndex = 0;
	
	public BlockLeaf(int i)
	{
		super(Material.LEAVES);
		this.logIndex = i;
		this.initBlock();
	}
	
	@Override
	public void initBlock()
	{
		try
		{
			Field blockStateFld = null;
			for (Field f : Block.class.getDeclaredFields())
			{
				if (f.getType().equals(BlockStateContainer.class))
				{
					blockStateFld = f;
					break;
				}
			}
			
			blockStateFld.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.set(blockStateFld, modifiersField.getModifiers() & ~Modifier.FINAL);
			blockStateFld.set(this, this.createBlockState());
		}
		catch (Exception ex)
		{
			FMLCommonHandler.instance().raiseException(ex, "ExPetrum was umable to reflect BlockStateContainer field!", true);
		}
		
		this.setHardness(1.5f);
		this.setRegistryName(this.createRegistryLocation());
		this.setResistance(0.5f);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.LEAF_STATE, EnumLeafState.NORMAL).withProperty(ExPBlockProperties.TREE_TYPES[this.logIndex], EnumTreeType.values()[this.logIndex * 5]));
		this.setTickRandomly(true);
		Blocks.FIRE.setFireInfo(this, 30, 60);
		ExPHandlerRegistry.put(this);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ExPItems.stick;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(ExPBlockProperties.TREE_TYPES[this.logIndex]).ordinal();
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random)
	{
		return 1 + random.nextInt(3);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		super.updateTick(worldIn, pos, state, rand);
		boolean foundWood = false;
		for (int dx = -LEAVES_CHECK_RADIUS; dx <= LEAVES_CHECK_RADIUS; ++dx)
		{
			for (int dy = -LEAVES_CHECK_RADIUS; dy <= LEAVES_CHECK_RADIUS; ++dy)
			{
				for (int dz = -LEAVES_CHECK_RADIUS; dz <= LEAVES_CHECK_RADIUS; ++dz)
				{
					BlockPos offset = pos.add(dx, dy, dz);
					if (this.isSameWoodType(worldIn, state, pos, worldIn.getBlockState(offset)))
					{
						foundWood = true;
						break;
					}
				}
			}
		}
		
		if (!foundWood)
		{
			worldIn.setBlockToAir(pos);
		}
		else
		{
			if (worldIn.rand.nextInt(64) == 0)
			{
				EnumLeafState suggestion = EnumLeafState.values()[Helpers.getSuggestedGrassState(pos, worldIn).ordinal()];
				if (state.getValue(ExPBlockProperties.LEAF_STATE) != suggestion && !state.getValue(ExPBlockProperties.TREE_TYPES[this.logIndex]).isEvergreen())
				{
					worldIn.setBlockState(pos, state.withProperty(ExPBlockProperties.LEAF_STATE, suggestion), 2);
				}
			}
		}
    }

	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return PathNodeType.OPEN;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ExPBlockProperties.LEAF_STATE, EnumLeafState.values()[meta % 3]).withProperty(ExPBlockProperties.TREE_TYPES[this.logIndex], EnumTreeType.values()[meta / 3 + this.logIndex * 5]);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
    	return (state.getValue(ExPBlockProperties.TREE_TYPES[this.logIndex]).ordinal() - this.logIndex * 5) * 3 + state.getValue(ExPBlockProperties.LEAF_STATE).ordinal();
    }

    @Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(state.getBlock(), 1, this.getMetaFromState(state));
	}

	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {ExPBlockProperties.LEAF_STATE, ExPBlockProperties.TREE_TYPES[this.logIndex]});
    }

	public ResourceLocation createRegistryLocation()
	{
		return new ResourceLocation(ExPRegistryNames.blockLeaves.getResourceDomain(), ExPRegistryNames.blockLeaves.getResourcePath() + this.logIndex);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other instanceof BlockLeaves;
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}
	
	@Override
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        entityIn.motionX *= 0.4D;
        entityIn.motionZ *= 0.4D;
    }

    @Override
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < 15; ++i)
		{
			list.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	public boolean isSameWoodType(World w, IBlockState state, BlockPos pos, IBlockState of)
	{
		if (of.getBlock() instanceof BlockLog)
		{
			int logIndex = ((BlockLog)of.getBlock()).logIndex;
			if (logIndex == this.logIndex)
			{
				return state.getValue(ExPBlockProperties.TREE_TYPES[this.logIndex]) == of.getValue(ExPBlockProperties.TREE_TYPES[this.logIndex]);
			}
		}
		
		return false;
	}

	@Override
	public int getLeavesColor(IBlockAccess access, IBlockState state, BlockPos pos)
	{
		return this.getLeavesState(access, state, pos) == EnumLeafState.NORMAL ? Helpers.getGrassColor(state, access, pos, -1) : -1;
	}

	@Override
	public EnumLeafState getLeavesState(IBlockAccess access, IBlockState state, BlockPos pos)
	{
		return state.getValue(ExPBlockProperties.LEAF_STATE);
	}

	@Override
	public boolean isEvergreen(IBlockAccess access, IBlockState state, BlockPos pos)
	{
		return state.getValue(ExPBlockProperties.TREE_TYPES[this.logIndex]).isEvergreen();
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		// TODO Auto-generated method stub
		return Pair.of((byte)1, (byte)1);
	}

	@Override
	public int getLeavesColorForMeta(int meta)
	{
		EnumLeafState state = EnumLeafState.values()[meta % 3];
		
		return state == EnumLeafState.NORMAL ? 0x166612 : -1;
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockLeaf).forEach(s -> { 
			OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)); 
			AtomicInteger i = new AtomicInteger(0);
			ExPBlockProperties.TREE_TYPES[this.logIndex].getAllowedValues().stream().map(EnumTreeType::getName).forEach(ss -> OreDictionary.registerOre(s + Character.toUpperCase(ss.charAt(0)) + ss.substring(1), new ItemStack(this, 1, i.getAndAdd(3))));
		});
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
