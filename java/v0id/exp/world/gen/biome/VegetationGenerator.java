package v0id.exp.world.gen.biome;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import v0id.api.exp.block.EnumGrassAmount;
import v0id.api.exp.block.EnumGrassState;
import v0id.api.exp.block.IGrass;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;

public class VegetationGenerator extends WorldGenerator
{
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		IBlockState tallGrassState = ExPBlocks.vegetation.getDefaultState().withProperty(ExPBlockProperties.VEGETATION_GROWTH, 3);
		BlockPos off = position;
		while (worldIn.isAirBlock(off) || worldIn.getBlockState(off).getBlock().isLeaves(worldIn.getBlockState(off), worldIn, off) && off.getY() > 0)
		{
			off = off.down();
		}
		
		IBlockState chosen = worldIn.getBlockState(off);
		if (chosen.getBlock().isAssociatedBlock(Blocks.GRASS))
		{
			for (int i = 0; i < 64; ++i)
			{
				BlockPos at = off.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
				if (worldIn.isAirBlock(at))
				{
					BlockPos below = at.down();
					IBlockState stateBelow = worldIn.getBlockState(below);
					if (!(stateBelow.getBlock().isAssociatedBlock(Blocks.GRASS)))
					{
						continue;
					}
					
					float grassChance = 1;
					if (stateBelow.getBlock() instanceof IGrass)
					{
						if (((IGrass)stateBelow.getBlock()).getState() != EnumGrassState.NORMAL)
						{
							continue;
						}
						
						EnumGrassAmount ega = ((IGrass)stateBelow.getBlock()).getAmount(stateBelow, below, worldIn);
						grassChance = ega == EnumGrassAmount.GREATER ? 1 : ega == EnumGrassAmount.MORE ? 0.9F : ega == EnumGrassAmount.NORMAL ? 0.8F : ega == EnumGrassAmount.LESS ? 0.4F : 0.2F;
					}
					
					if (rand.nextFloat() <= grassChance)
					{
						worldIn.setBlockState(at, tallGrassState, 2);
					}
				}
			}
			
			return true;
		}
		
		return false;
	}
	
}
