package v0id.exp.world.gen.biome;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import v0id.api.exp.block.IWater;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;

public class SeaweedGenerator extends WorldGenerator
{
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		for (int i = 0; i < 16; ++i)
		{
			BlockPos offset = position.add(rand.nextInt(8) - rand.nextInt(8), 4, rand.nextInt(8) - rand.nextInt(8));
			while (!worldIn.getBlockState(offset).getPropertyKeys().contains(ExPBlockProperties.ROCK_CLASS) && offset.getY() > 64)
			{
				offset = offset.down();
			}
			
			IBlockState at = worldIn.getBlockState(offset);
			IBlockState above = worldIn.getBlockState(offset.up());
			if (above.getMaterial() == Material.WATER && at.getBlock().isAssociatedBlock(Blocks.SAND))
			{
				boolean allow = above.getBlock() instanceof IWater ? ((IWater)above.getBlock()).isSalt(worldIn, offset.up()) : true;
				if (allow)
				{
					worldIn.setBlockState(offset, ExPBlocks.seaweed.getDefaultState().withProperty(ExPBlockProperties.ROCK_CLASS, at.getValue(ExPBlockProperties.ROCK_CLASS)), 2);
				}
			}
		}
		
		return true;
	}

}
