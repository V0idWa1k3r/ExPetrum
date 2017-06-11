package v0id.exp.world.gen.biome;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import v0id.api.exp.block.EnumBerry;
import v0id.api.exp.block.EnumShrubState;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;

import java.util.Random;

public class BerryBushGenerator extends WorldGenerator
{
	public EnumShrubState stateToPlace;

	public BerryBushGenerator(EnumShrubState generateAs)
	{
		super();
		this.stateToPlace = generateAs;
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		IBlockState toSet = ExPBlocks.berryBushes[this.stateToPlace.ordinal()].getDefaultState().withProperty(ExPBlockProperties.BERRY_BUSH_TYPE, EnumBerry.values()[rand.nextInt(EnumBerry.values().length)]);
        BlockPos offset = position.add(rand.nextInt(4) - rand.nextInt(4), 5, rand.nextInt(4) - rand.nextInt(4));
		while (!worldIn.getBlockState(offset).getBlock().isAssociatedBlock(Blocks.GRASS) && offset.getY() > 96)
		{
			offset = offset.down();
		}

		if (worldIn.getBlockState(offset).getBlock().isAssociatedBlock(Blocks.GRASS))
		{
			BlockPos at = offset.up();
			IBlockState current = worldIn.getBlockState(at);
			if (current.getMaterial() != Material.WATER && (current.getBlock().isAir(current, worldIn, at) || current.getBlock().isReplaceable(worldIn, at)))
			{
				worldIn.setBlockState(at, toSet, 2);
				if (rand.nextBoolean())
				{
					worldIn.setBlockState(at.up(), toSet, 2);
					if (rand.nextDouble() < 0.1)
					{
						worldIn.setBlockState(at.up().up(), toSet, 2);
					}
				}

				return true;
			}
		}
		
		return false;
	}

}
