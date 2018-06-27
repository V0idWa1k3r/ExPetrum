package v0id.exp.world.gen.biome;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import v0id.api.exp.data.ExPBlocks;

import java.util.Random;

public class FlintGenerator extends WorldGenerator
{
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		for (int i = 0; i < 2 + rand.nextInt(2); ++i)
		{
			BlockPos offset = position.add(rand.nextInt(6) - rand.nextInt(6), 6, rand.nextInt(6) - rand.nextInt(6));
			while (!worldIn.isSideSolid(offset.down(), EnumFacing.UP, false) && offset.getY() > 0)
			{
				offset = offset.down();
			}
			
			if (worldIn.isAirBlock(offset) && !worldIn.getBlockState(offset.down()).getBlock().isAssociatedBlock(Blocks.SAND))
			{
				worldIn.setBlockState(offset, ExPBlocks.flint.getDefaultState(), 2);
			}
		}
		
		return true;
	}

}
