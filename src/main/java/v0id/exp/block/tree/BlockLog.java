package v0id.exp.block.tree;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.block.ILog;
import v0id.api.exp.data.*;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.ExPetrum;
import v0id.exp.block.IItemBlockProvider;
import v0id.exp.block.item.ItemBlockLog;
import v0id.exp.entity.EntityFallingTree;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class BlockLog extends BlockRotatedPillar implements IWeightProvider, ILog, IOreDictEntry, IItemBlockProvider
{
	public int logIndex = 0;
	
	public BlockLog(int i)
	{
		super(Material.WOOD);
		this.logIndex = i;
		this.setHardness(6);
		this.setRegistryName(createRegistryLocation());
		this.setResistance(6);
		this.setSoundType(SoundType.WOOD);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, Axis.Y).withProperty(ExPBlockProperties.TREE_TYPE, EnumTreeType.values()[this.logIndex * 5]));
		Blocks.FIRE.setFireInfo(this, 5, 5);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ExPBlocks.logsDeco[this.logIndex]);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 1 + (state.getValue(ExPBlockProperties.TREE_TYPE).ordinal() % 5) * 3;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(this, 1, 1 + (state.getValue(ExPBlockProperties.TREE_TYPE).ordinal() % 5) * 3);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced)
	{
		if (this instanceof Decorative)
		{
			super.addInformation(stack, world, tooltip, advanced);
			return;
		}

		EntityPlayer player = ExPetrum.proxy.getClientPlayer();
		if (player != null)
        {
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
        }
		
		super.addInformation(stack, world, tooltip, advanced);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < 5; ++i)
		{
			IBlockState state = this.getDefaultState().withProperty(ExPBlockProperties.TREE_TYPE, EnumTreeType.values()[this.logIndex * 5 + i]);
			int meta = this.getMetaFromState(state);
			list.add(new ItemStack(this, 1, meta));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AXIS, Axis.values()[meta % 3]).withProperty(ExPBlockProperties.TREE_TYPE, EnumTreeType.values()[meta / 3 + this.logIndex * 5]);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
    	int ordinal = state.getValue(ExPBlockProperties.TREE_TYPE).ordinal();
    	if (ordinal * 3 < this.logIndex * 15 || ordinal * 3 >= (this.logIndex + 1) * 15)
        {
            return 0;
        }

    	return (ordinal - this.logIndex * 5) * 3 + state.getValue(AXIS).ordinal();
    }

    @Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, AXIS, ExPBlockProperties.TREE_TYPE);
    }

	public ResourceLocation createRegistryLocation()
	{
		return ExPRegistryNames.asLocation(ExPRegistryNames.blockLog + this.logIndex);
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
		return 0.5F;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return Pair.of((byte)2, (byte)3);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isSameWoodType(World w, IBlockState state, BlockPos pos, IBlockState of)
	{
		return !(this instanceof Decorative) && (of.getBlock() == this && of.getValue(ExPBlockProperties.TREE_TYPE) == state.getValue(ExPBlockProperties.TREE_TYPE));
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
		EntityItem item = new EntityItem(w, at.x, at.y, at.z, new ItemStack(ExPBlocks.logsDeco[this.logIndex], 1, this.getMetaFromState(state)));
		if (!w.isRemote)
		{
			w.spawnEntity(item);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		return player.getHeldItem(EnumHand.MAIN_HAND).getItem().canHarvestBlock(state, player.getHeldItem(EnumHand.MAIN_HAND)) ? super.getPlayerRelativeBlockHardness(state, player, worldIn, pos) : -1F;
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
			return ExPRegistryNames.asLocation(ExPRegistryNames.blockLogDeco + this.logIndex);
		}
	}
	
	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockLog).forEach(s -> { 
			OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE));
			for (int i = 0; i < 5; ++i)
            {
                EnumTreeType tt = EnumTreeType.values()[i + this.logIndex * 5];
                OreDictionary.registerOre(s + Character.toUpperCase(tt.name().charAt(0)) + tt.name().toLowerCase().substring(1), new ItemStack(this, 1, 1 + i * 3));
            }
		});
	}

	@Override
	public void registerItem(IForgeRegistry<Item> registry)
	{
		registry.register(new ItemBlockLog(this));
	}
}
