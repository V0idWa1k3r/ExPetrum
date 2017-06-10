package v0id.api.exp.world.gen;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITreeGenerator
{
	ITreeGenerator withLeaves(BiConsumer<Pair<ITreeGenerator, BlockPos>, Integer> leavesHandler);
	
	ITreeGenerator withTrunk(Function<Pair<ITreeGenerator, BlockPos>, Integer> trunkHandler);
	
	World genWorld();
	
	IBlockState getLeaves();
	
	IBlockState getWood();
	
	boolean generate(World worldIn, Random rand, BlockPos position);
}
