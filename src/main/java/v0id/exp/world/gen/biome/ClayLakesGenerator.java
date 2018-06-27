package v0id.exp.world.gen.biome;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fluids.BlockFluidFinite;
import v0id.api.exp.data.ExPBlocks;

import java.util.Random;

public class ClayLakesGenerator extends WorldGenerator
{
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int range = 3 + rand.nextInt(3);
        while (worldIn.isAirBlock(position))
        {
            position = position.down();
        }

        Material material = worldIn.getBlockState(position).getMaterial();
        if (material != Material.GROUND && material != Material.GRASS)
        {
            return false;
        }

        BlockPos center = position.up(range - 2);
        for (int dx = -range; dx < range; ++dx)
        {
            for (int dz = -range; dz < range; ++dz)
            {
                for (int dy = 0; dy < 2; ++dy)
                {
                    BlockPos at = position.add(dx, -dy, dz);
                    double distance = at.distanceSq(center);
                    if (distance <= range * range && !worldIn.isAirBlock(at))
                    {
                        worldIn.setBlockState(at, ExPBlocks.clay.getDefaultState().withProperty(BlockFluidFinite.LEVEL, 10), 2);
                    }
                }
            }
        }

        return true;
    }
}
