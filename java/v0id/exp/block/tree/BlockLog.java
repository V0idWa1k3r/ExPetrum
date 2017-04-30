package v0id.exp.block.tree;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.api.core.util.ItemBlockWithMetadata;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.block.ILog;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.entity.EntityFallingTree;

public class BlockLog extends BlockRotatedPillar implements IWeightProvider, ILog
{
	public int logIndex = 0;
	
	public BlockLog(int i)
	{
		super(Material.WOOD);
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
		
		this.setHardness(6);
		this.setRegistryName(createRegistryLocation());
		this.setResistance(6);
		this.setSoundType(SoundType.WOOD);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, Axis.Y).withProperty(ExPBlockProperties.TREE_TYPES[this.logIndex], EnumTreeType.values()[this.logIndex * 5]));
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlockWithMetadata(this));
	}
	
	
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(this, 1, 1 + (state.getValue(ExPBlockProperties.TREE_TYPES[this.logIndex]).ordinal() % 5) * 3);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
	{
		if (this instanceof Decorative)
		{
			super.addInformation(stack, player, tooltip, advanced);
			return;
		}
		
		if (player.capabilities.isCreativeMode)
		{
			tooltip.add("This block is NOT FOR BUILDING!!!");
			tooltip.add("This is a block of an alive tree!");
			tooltip.add("Breaking it will trigger the \"fall\" effect of a tree!");
		}
		else
		{
			tooltip.add("You should NEVER get this block");
			tooltip.add("If you have somehow obtained it without using cheats PLEASE report it to V0id!");
		}
		
		super.addInformation(stack, player, tooltip, advanced);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < 5; ++i)
		{
			IBlockState state = this.getDefaultState().withProperty(ExPBlockProperties.TREE_TYPES[this.logIndex], EnumTreeType.values()[this.logIndex * 5 + i]);
			int meta = this.getMetaFromState(state);
			list.add(new ItemStack(itemIn, 1, meta));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AXIS, Axis.values()[meta % 3]).withProperty(ExPBlockProperties.TREE_TYPES[this.logIndex], EnumTreeType.values()[meta / 3 + this.logIndex * 5]);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
    	return (state.getValue(ExPBlockProperties.TREE_TYPES[this.logIndex]).ordinal() - this.logIndex * 5) * 3 + state.getValue(AXIS).ordinal();
    }

    @Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AXIS, ExPBlockProperties.TREE_TYPES[this.logIndex]});
    }

	public ResourceLocation createRegistryLocation()
	{
		return new ResourceLocation(ExPRegistryNames.blockLog.getResourceDomain(), ExPRegistryNames.blockLog.getResourcePath() + this.logIndex);
	}
	
	@Override
	public boolean isWood(IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return true;
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
	public boolean isFullyOpaque(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isSameWoodType(World w, IBlockState state, BlockPos pos, IBlockState of)
	{
		return this instanceof Decorative ? false : of.getBlock() == this && of.getValue(ExPBlockProperties.TREE_TYPES[this.logIndex]) == state.getValue(ExPBlockProperties.TREE_TYPES[this.logIndex]);
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
		if (this instanceof Decorative)
		{
			return super.removedByPlayer(state, worldIn, pos, player, willHarvest);
		}
		
		if (!worldIn.isRemote)
		{
			if (!(worldIn.getBlockState(pos.up()).getBlock() instanceof ILog))
			{
				return super.removedByPlayer(state, worldIn, pos, player, willHarvest);
			}
			
			EntityFallingTree fallingTree = new EntityFallingTree(worldIn, pos, player);
			fallingTree.setPositionAndRotation(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0, 0);
			worldIn.spawnEntity(fallingTree);
		}
		
		return false;
	}

	@Override
	public void dropLogItem(World w, IBlockState state, Vec3d at)
	{
		state = state.withProperty(AXIS, Axis.Y);
		EntityItem item = new EntityItem(w, at.xCoord, at.yCoord, at.zCoord, new ItemStack(ExPBlocks.logsDeco[this.logIndex], 1, this.getMetaFromState(state)));
		if (!w.isRemote)
		{
			w.spawnEntity(item);
		}
	}
	
	public static class Decorative extends BlockLog
	{
		public Decorative(int i)
		{
			super(i);
		}

		@Override
		public ResourceLocation createRegistryLocation()
		{
			return new ResourceLocation(ExPRegistryNames.blockLogDeco.getResourceDomain(), ExPRegistryNames.blockLogDeco.getResourcePath() + this.logIndex);
		}
		
	}
}
