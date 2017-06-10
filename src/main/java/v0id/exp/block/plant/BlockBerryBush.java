package v0id.exp.block.plant;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.block.EnumBerry;
import v0id.api.exp.block.EnumGrassState;
import v0id.api.exp.block.EnumShrubState;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.block.IGrass;
import v0id.api.exp.block.IShrub;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.handler.ExPHandlerRegistry;
import v0id.exp.util.Helpers;

public class BlockBerryBush extends BlockShrub
{
	public BlockBerryBush(EnumShrubState s)
	{
		super(s);
	}

	@Override
	public void initBlock()
	{
		this.setHardness(1);
		this.setRegistryName(this.shrubState == EnumShrubState.BLOOMING ? ExPRegistryNames.blockBerryBushBerries : this.shrubState == EnumShrubState.AUTUMN ? ExPRegistryNames.blockBerryBushAutumn : this.shrubState == EnumShrubState.DEAD ? ExPRegistryNames.blockBerryBushDead : ExPRegistryNames.blockBerryBushNormal);
		this.setResistance(0);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ExPBlockProperties.BERRY_BUSH_TYPE, EnumBerry.BLACKBERRY).withProperty(ExPBlockProperties.SHRUB_IS_TALL, false));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setTickRandomly(true);
		Blocks.FIRE.setFireInfo(this, 60, 100);
		ExPHandlerRegistry.put(this);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(ExPBlockProperties.BERRY_BUSH_TYPE).ordinal() + EnumTreeType.values().length + EnumShrubType.values().length;
	}

    @Override
    public int getShrubColor(IBlockState state, BlockPos pos, IBlockAccess w)
    {
        return this.getState() == EnumShrubState.NORMAL || this.getState() == EnumShrubState.BLOOMING ? w.getBlockState(pos.down()).getBlock() instanceof IShrub ? ((IShrub)w.getBlockState(pos.down()).getBlock()).getShrubColor(w.getBlockState(pos.down()), pos.down(), w) : w.getBlockState(pos.down()).getBlock() instanceof IGrass ? ((IGrass)w.getBlockState(pos.down()).getBlock()).getGrassColor(w.getBlockState(pos.down()), pos.down(), w) : w.getBiome(pos).getGrassColorAtPos(pos) : -1;
    }

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(ExPBlockProperties.BERRY_BUSH_TYPE, EnumBerry.values()[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ExPBlockProperties.BERRY_BUSH_TYPE).ordinal();
	}

	@Override
	public void performChecks(IBlockState state, World worldIn, BlockPos pos)
	{
		if (!Helpers.canPlantGrow(pos.up(), worldIn))
		{
			worldIn.setBlockToAir(pos);
			return;
		}
		
		if (!(worldIn.getBlockState(pos.down()).getBlock() instanceof IShrub || worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn.getBlockState(pos.down()), worldIn, pos.down(), EnumFacing.UP, this)))
		{
			worldIn.setBlockToAir(pos);
			return;
		}
		
		if (worldIn.getBlockState(pos.down()).getBlock() instanceof IShrub && ((IShrub)worldIn.getBlockState(pos.down()).getBlock()).getShrubInternalType() == this.getShrubInternalType())
		{
			EnumShrubState sstate = ((IShrub)worldIn.getBlockState(pos.down()).getBlock()).getState();
			if (sstate != this.getState())
			{
				worldIn.setBlockState(pos, ExPBlocks.shrubs[sstate.ordinal()].getDefaultState().withProperty(ExPBlockProperties.SHRUB_TYPE, state.getValue(ExPBlockProperties.SHRUB_TYPE)));
				return;
			}
		}
		else
		{
			EnumShrubState sstate = this.getState();
			EnumGrassState grassState = worldIn.getBlockState(pos.down()).getBlock() instanceof IGrass ? ((IGrass)worldIn.getBlockState(pos.down()).getBlock()).getState() : Helpers.getSuggestedGrassState(pos.down(), worldIn);
			// TODO growth!
		}
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumBerry.values().length; ++i)
		{
			list.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{ ExPBlockProperties.BERRY_BUSH_TYPE, ExPBlockProperties.SHRUB_IS_TALL });
	}

	@Override
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		IBlockState at = world.getBlockState(pos);
		IBlockState self = world.getBlockState(pos.offset(facing));
		return at.getBlock() instanceof IShrub  && ((IShrub)at.getBlock()).getShrubInternalType() == this.getShrubInternalType() && at.getValue(ExPBlockProperties.BERRY_BUSH_TYPE) == self.getValue(ExPBlockProperties.BERRY_BUSH_TYPE);
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.blockShrub).forEach(s -> { 
			OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)); 
			AtomicInteger i = new AtomicInteger(0);
			Stream.of(ExPOreDict.berryNames).forEach(ss -> OreDictionary.registerOre(s + Character.toUpperCase(ss.charAt(0)) + ss.substring(1), new ItemStack(this, 1, i.getAndIncrement())));
		});
	}
	
	@Override
	public int getShrubInternalType()
	{
		return 1;
	}
}
