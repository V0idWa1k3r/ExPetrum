package v0id.exp.world.gen.biome;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import v0id.api.exp.block.EnumShrubState;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;

public class ShrubGenerator extends WorldGenerator
{
	public EnumShrubType toGenerate;
	public EnumShrubState stateToPlace;
	
	public ShrubGenerator(EnumShrubType toGenerate, EnumShrubState generateAs)
	{
		super();
		this.toGenerate = toGenerate;
		this.stateToPlace = generateAs;
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		IBlockState toSet = ExPBlocks.shrubs[this.stateToPlace.ordinal()].getDefaultState().withProperty(ExPBlockProperties.SHRUB_TYPE, this.toGenerate);
		for (int i = 0; i < 4; ++i)
		{
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
		}
		
		return false;
	}

}
