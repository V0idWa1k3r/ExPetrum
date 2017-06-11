package v0id.exp.block.plant;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import v0id.api.exp.block.*;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.*;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.handler.ExPHandlerRegistry;
import v0id.exp.item.ItemFood;
import v0id.exp.util.Helpers;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
			if (sstate != this.getState() && sstate != EnumShrubState.BLOOMING)
			{
				worldIn.setBlockState(pos, ExPBlocks.shrubs[sstate.ordinal()].getDefaultState().withProperty(ExPBlockProperties.SHRUB_TYPE, state.getValue(ExPBlockProperties.SHRUB_TYPE)));
            }
		}
		else
		{
			EnumShrubState sstate = this.getState();
			EnumGrassState grassState = worldIn.getBlockState(pos.down()).getBlock() instanceof IGrass ? ((IGrass)worldIn.getBlockState(pos.down()).getBlock()).getState() : Helpers.getSuggestedGrassState(pos.down(), worldIn);
			switch (grassState)
            {
                case DRY:
                {
                    worldIn.setBlockState(pos, ExPBlocks.berryBushes[EnumShrubState.AUTUMN.ordinal()].getDefaultState().withProperty(ExPBlockProperties.BERRY_BUSH_TYPE, state.getValue(ExPBlockProperties.BERRY_BUSH_TYPE)));
                    break;
                }

                case DEAD:
                {
                    worldIn.setBlockState(pos, ExPBlocks.berryBushes[EnumShrubState.DEAD.ordinal()].getDefaultState().withProperty(ExPBlockProperties.BERRY_BUSH_TYPE, state.getValue(ExPBlockProperties.BERRY_BUSH_TYPE)));
                    break;
                }

                case NORMAL:
                default:
                {
                    if (this.getState() == EnumShrubState.NORMAL)
                    {
                        if (worldIn.rand.nextFloat() <= Helpers.getGenericGrowthModifier(pos, worldIn, true) / 10000)
                        {
                            worldIn.setBlockState(pos, ExPBlocks.berryBushes[EnumShrubState.BLOOMING.ordinal()].getDefaultState().withProperty(ExPBlockProperties.BERRY_BUSH_TYPE, state.getValue(ExPBlockProperties.BERRY_BUSH_TYPE)));
                        }
                    }
                    else if (this.getState() != EnumShrubState.BLOOMING)
                    {
                        worldIn.setBlockState(pos, ExPBlocks.berryBushes[EnumShrubState.NORMAL.ordinal()].getDefaultState().withProperty(ExPBlockProperties.BERRY_BUSH_TYPE, state.getValue(ExPBlockProperties.BERRY_BUSH_TYPE)));
                    }
                }
            }
		}
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for (int i = 0; i < EnumBerry.values().length; ++i)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ExPBlockProperties.BERRY_BUSH_TYPE, ExPBlockProperties.SHRUB_IS_TALL);
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

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (this.getState() == EnumShrubState.BLOOMING)
        {
            if (!worldIn.isRemote)
            {
                ItemStack held = playerIn.getHeldItem(hand);
                int allowHarvest = 0;
                if (!held.isEmpty() && held.getItem() == ExPItems.basket)
                {
                    held.damageItem(1, playerIn);
                    allowHarvest = 2;
                }
                else
                {
                    allowHarvest = worldIn.rand.nextFloat() < 0.1F ? 1 : 0;
                    if (allowHarvest == 0)
                    {
                        worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1, 1);
                    }
                }

                if (allowHarvest > 0)
                {
                    worldIn.setBlockState(pos, ExPBlocks.berryBushes[EnumShrubState.NORMAL.ordinal()].getDefaultState().withProperty(ExPBlockProperties.BERRY_BUSH_TYPE, state.getValue(ExPBlockProperties.BERRY_BUSH_TYPE)));
                    if (allowHarvest == 2 || worldIn.rand.nextFloat() <= 0.75F)
                    {
                        float amt = 50 + worldIn.rand.nextFloat() * 250;
                        ItemStack food = new ItemStack(ExPItems.food, 1, EnumCrop.values().length + state.getValue(ExPBlockProperties.BERRY_BUSH_TYPE).ordinal() + 1);
                        ItemFood item = (ItemFood) food.getItem();
                        item.setLastTickTime(food, IExPWorld.of(worldIn).today());
                        item.setTotalWeight(food, amt);
                        EntityItem drop = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, food.copy());
                        worldIn.spawnEntity(drop);
                    }
                }
            }

            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
