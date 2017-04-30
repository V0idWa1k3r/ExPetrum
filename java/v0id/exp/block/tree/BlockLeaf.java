package v0id.exp.block.tree;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.api.core.util.ItemBlockWithMetadata;
import v0id.api.exp.block.EnumLeafState;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.block.ILeaves;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.util.Helpers;

public class BlockLeaf extends Block implements ILeaves, IWeightProvider
{
	// log and not leaf because this is the index of the log this leaf is a leaf of
	// Sound confusing enough? (>w<)
	public int logIndex = 0;
	
	public BlockLeaf(int i)
	{
		super(Material.LEAVES);
		this.logIndex = i;
		this.initBlock();
	}
	
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
		
		this.setHardness(0.5f);
		this.setRegistryName(this.createRegistryLocation());
		this.setResistance(0.5f);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.LEAF_STATE, EnumLeafState.NORMAL).withProperty(ExPBlockProperties.TREE_TYPES[this.logIndex], EnumTreeType.values()[this.logIndex * 5]));
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlockWithMetadata(this));
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
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {ExPBlockProperties.LEAF_STATE, ExPBlockProperties.TREE_TYPES[this.logIndex]});
    }

	public ResourceLocation createRegistryLocation()
	{
		return new ResourceLocation(ExPRegistryNames.blockLeaves.getResourceDomain(), ExPRegistryNames.blockLeaves.getResourcePath() + this.logIndex);
	}

	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return false;
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
	public float provideVolume(ItemStack item)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLeavesColorForMeta(int meta)
	{
		EnumLeafState state = EnumLeafState.values()[meta % 3];
		
		return state == EnumLeafState.NORMAL ? 0x166612 : -1;
	}

}
