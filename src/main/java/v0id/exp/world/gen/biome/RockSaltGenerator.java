package v0id.exp.world.gen.biome;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import v0id.api.exp.block.IOreHintReplaceable;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class RockSaltGenerator extends WorldGenerator
{
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        BlockPos pos = new BlockPos(position.getX(), rand.nextInt(worldIn.getHeight(position).getY() - 4), position.getZ());
        List<BlockPos> hints = Lists.newArrayList();
        generateTendril(worldIn, rand, pos, null, 32 + rand.nextInt(96), bpos -> hints.add(bpos));
        if (!hints.isEmpty())
        {
            for (int i = 0; i < 8 + rand.nextInt(12); ++i)
            {
                BlockPos hPos = hints.get(rand.nextInt(hints.size()));
                hPos = worldIn.getHeight(hPos).up(6);
                while ((worldIn.isAirBlock(hPos) || !worldIn.getBlockState(hPos).isOpaqueCube()) && hPos.getY() > 0)
                {
                    hPos = hPos.down();
                }

                IBlockState preventing = worldIn.getBlockState(hPos.up());
                if (preventing.getBlock().isReplaceable(worldIn, hPos) || preventing.getBlock() instanceof IOreHintReplaceable)
                {
                    hPos = hPos.up();
                    worldIn.setBlockState(hPos, ExPBlocks.rockSalt.getDefaultState().withProperty(ExPBlockProperties.ROCKSALT_ISHINT, true), 2);
                }
            }
        }

        return false;
    }

    public void generateTendril(World world, Random rand, BlockPos at, EnumFacing from, int placesLeft, Consumer<BlockPos> hintPositionSaver)
    {
        if (placesLeft <= 0)
        {
            return;
        }

        if (world.getBlockState(at).getBlock().isReplaceableOreGen(world.getBlockState(at), world, at, state -> state.getBlock().isAssociatedBlock(Blocks.STONE)))
        {
            world.setBlockState(at, ExPBlocks.rockSalt.getDefaultState(), 2);
            hintPositionSaver.accept(new BlockPos(at.getX(), 0, at.getZ()));
        }

        EnumFacing to = EnumFacing.values()[rand.nextInt(6)];
        while (to == from || !world.isBlockLoaded(at.offset(to)))
        {
            to = EnumFacing.values()[rand.nextInt(6)];
        }

        generateTendril(world, rand, at.offset(to), to.getOpposite(), placesLeft - 1, hintPositionSaver);
        if (rand.nextFloat() <= 0.2F)
        {
            to = EnumFacing.values()[rand.nextInt(6)];
            while (to == from || !world.isBlockLoaded(at.offset(to)))
            {
                to = EnumFacing.values()[rand.nextInt(6)];
            }

            generateTendril(world, rand, at.offset(to), to.getOpposite(), (int)(placesLeft / 1.5F), hintPositionSaver);
        }
    }
}
