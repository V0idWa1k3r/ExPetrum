package v0id.exp.world.gen.biome;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fluids.BlockFluidBase;
import v0id.api.exp.data.ExPBlocks;

import java.util.Random;

/**
 * Created by V0idWa1k3r on 10-Jun-17.
 */
public class OilGenerator extends WorldGenerator
{
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        IBlockState oilBlock = ExPBlocks.oil.getDefaultState().withProperty(BlockFluidBase.LEVEL, 10);
        int randomY = 32 + rand.nextInt(64);
        int size = 4 + rand.nextInt(8);
        BlockPos at = new BlockPos(position.getX(), randomY, position.getZ());
        for (int dx = -size; dx <= size; ++dx)
        {
            for (int dy = -size; dy <= size; ++dy)
            {
                for (int dz = -size; dz <= size; ++dz)
                {
                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (dist > size)
                    {
                        continue;
                    }

                    worldIn.setBlockState(at.add(dx, dy, dz), oilBlock, 2);
                }
            }
        }

        int randomYEx = 8 + rand.nextInt(size * 2);
        while (at.getY() < randomYEx + 128)
        {
            if (worldIn.getBlockState(at.up(3)).getMaterial() == Material.WATER && worldIn.getBlockState(at.up(3)) != oilBlock)
            {
                break;
            }

            worldIn.setBlockState(at, oilBlock, 2);
            at = at.up();
        }

        return true;
    }
}
