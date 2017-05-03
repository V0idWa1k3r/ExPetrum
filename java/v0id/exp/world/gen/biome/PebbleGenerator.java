package v0id.exp.world.gen.biome;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.exp.world.gen.GenerationHelper;

public class PebbleGenerator extends WorldGenerator
{
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		for (int i = 0; i < 16; ++i)
		{
			BlockPos offset = position.add(rand.nextInt(6) - rand.nextInt(6), 6, rand.nextInt(6) - rand.nextInt(6));
			while (!worldIn.isSideSolid(offset.down(), EnumFacing.UP, false) && offset.getY() > 0)
			{
				offset = offset.down();
			}
			
			if (worldIn.isAirBlock(offset) && !worldIn.getBlockState(offset.down()).getBlock().isAssociatedBlock(Blocks.SAND))
			{
				EnumRockClass erc = GenerationHelper.getStoneTypeAt(worldIn, offset);
				worldIn.setBlockState(offset, ExPBlocks.pebble.getDefaultState().withProperty(ExPBlockProperties.ROCK_CLASS, erc), 2);
			}
		}
		
		return true;
	}

}
