package v0id.exp.block.fluid;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.api.exp.block.IWater;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.block.IInitializableBlock;

public class BlockFreshWater extends BlockFluidFinite implements IWater, IInitializableBlock
{
	public BlockFreshWater()
	{
		super(ExPFluids.freshWater, Material.WATER);
		this.initBlock();
	}
	
	@Override
	public void initBlock()
	{
		this.setBlockUnbreakable();
		this.setRegistryName(ExPRegistryNames.blockFreshWater);
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabMiscBlocks);
		this.setLightOpacity(Blocks.WATER.getLightOpacity(Blocks.WATER.getDefaultState()));
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		this.setQuantaPerBlock(10);
	}
	
    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand)
    {
        boolean changed = false;
        int quantaRemaining = state.getValue(LEVEL) + 1;

        // Flow vertically if possible
        int prevRemaining = quantaRemaining;
        quantaRemaining = tryToFlowVerticallyInto(world, pos, quantaRemaining);

        if (quantaRemaining < 1)
        {
            return;
        }
        else if (quantaRemaining != prevRemaining)
        {
            changed = true;
            if (quantaRemaining == 1)
            {
                world.setBlockState(pos, state.withProperty(LEVEL, quantaRemaining - 1), 3);
                return;
            }
        }
        else if (quantaRemaining == 1)
        {
            return;
        }

        // Flow out if possible
        int lowerThan = quantaRemaining - 1;
        int total = quantaRemaining;
        int count = 1;

        for (EnumFacing side : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos off = pos.offset(side);
            if (displaceIfPossible(world, off))
                world.setBlockToAir(off);

            int quanta = getQuantaValueBelow(world, off, lowerThan);
            if (quanta >= 0)
            {
                count++;
                total += quanta;
            }
        }

        if (count == 1)
        {
            if (changed)
            {
                world.setBlockState(pos, state.withProperty(LEVEL, quantaRemaining - 1), 3);
            }
            return;
        }

        int each = total / count;
        int rem = total % count;

        for (EnumFacing side : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos off = pos.offset(side);
            int quanta = getQuantaValueBelow(world, off, lowerThan);
            if (quanta >= 0)
            {
                int newQuanta = each;
                if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0)
                {
                    ++newQuanta;
                    --rem;
                }

                if (newQuanta != quanta)
                {
                    if (newQuanta == 0)
                    {
                        world.setBlockState(off, Blocks.AIR.getDefaultState(), 2);
                    }
                    else
                    {
                        world.setBlockState(off, getDefaultState().withProperty(LEVEL, newQuanta - 1), 3);
                    }
                    world.scheduleUpdate(off, this, this.tickRate);
                }
                --count;
            }
        }

        if (rem > 0)
        {
            ++each;
        }
        world.setBlockState(pos, state.withProperty(LEVEL, each - 1), 2);
    }
    
    @Override
	public int tryToFlowVerticallyInto(World world, BlockPos pos, int amtToInput)
    {
        IBlockState myState = world.getBlockState(pos);
        BlockPos other = pos.add(0, this.densityDir, 0);
        if (other.getY() < 0 || other.getY() >= world.getHeight())
        {
        	world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
            return 0;
        }

        int amt = getQuantaValueBelow(world, other, this.quantaPerBlock);
        if (amt >= 0)
        {
            amt += amtToInput;
            if (amt > this.quantaPerBlock)
            {
                world.setBlockState(other, myState.withProperty(LEVEL, this.quantaPerBlock - 1), 3);
                world.scheduleUpdate(other, this, this.tickRate);
                return amt - this.quantaPerBlock;
            }
            else if (amt > 0)
            {
                world.setBlockState(other, myState.withProperty(LEVEL, amt - 1), 3);
                world.scheduleUpdate(other, this, this.tickRate);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                return 0;
            }
            return amtToInput;
        }
        else
        {
            int density_other = getDensity(world, other);
            if (density_other == Integer.MAX_VALUE)
            {
                if (displaceIfPossible(world, other))
                {
                    world.setBlockState(other, myState.withProperty(LEVEL, amtToInput - 1), 3);
                    world.scheduleUpdate(other, this, this.tickRate);
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                    return 0;
                }
                else
                {
                    return amtToInput;
                }
            }

            if (this.densityDir < 0)
            {
                if (density_other < this.density) // then swap
                {
                    IBlockState state = world.getBlockState(other);
                    world.setBlockState(other, myState.withProperty(LEVEL, amtToInput - 1), 3);
                    world.setBlockState(pos,   state, 3);
                    world.scheduleUpdate(other, this, this.tickRate);
                    world.scheduleUpdate(pos,   state.getBlock(), state.getBlock().tickRate(world));
                    return 0;
                }
            }
            else
            {
                if (density_other > this.density)
                {
                    IBlockState state = world.getBlockState(other);
                    world.setBlockState(other, myState.withProperty(LEVEL, amtToInput - 1), 2);
                    world.setBlockState(other, state, 2);
                    world.scheduleUpdate(other, this,  this.tickRate);
                    world.scheduleUpdate(other, state.getBlock(), state.getBlock().tickRate(world));
                    return 0;
                }
            }
            return amtToInput;
        }
    }

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return super.isAssociatedBlock(other) || other == Blocks.WATER || other == Blocks.FLOWING_WATER;
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		return plantable.getPlantType(world, pos) == EnumPlantType.Water && this.getQuantaPercentage(world, pos) >= 1;
	}

	@Override
	public boolean isSalt(World w, BlockPos pos)
	{
		return false;
	}
}
