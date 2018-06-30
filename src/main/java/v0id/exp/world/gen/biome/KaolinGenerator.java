package v0id.exp.world.gen.biome;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import v0id.api.exp.data.ExPBlocks;

import java.util.Random;

public class KaolinGenerator extends WorldGenerator
{
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
        int range = 3 + rand.nextInt(3);
        while (worldIn.isAirBlock(position))
        {
            position = position.down();
        }

        Material mat = worldIn.getBlockState(position).getMaterial();
        if (mat != Material.GRASS)
        {
            return false;
        }

        IBlockState place = ExPBlocks.kaolin.getDefaultState();
        for (int dx = -range; dx <= range; ++dx)
        {
            for (int dz = -range; dz <= range; ++dz)
            {
                BlockPos at = position.add(dx, 0, dz);
                if (at.distanceSq(position) <= range * range)
                {
                    worldIn.setBlockState(at, place, 2);
                    if (rand.nextBoolean() && rand.nextBoolean())
                    {
                        int val = rand.nextInt(range);
                        while (at.getY() >= position.getY() - val)
                        {
                            at = at.down();
                            worldIn.setBlockState(at, place, 2);
                        }
                    }
                }
            }
        }

		return true;
	}

}
