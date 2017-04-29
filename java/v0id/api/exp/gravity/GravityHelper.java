package v0id.api.exp.gravity;

import java.lang.reflect.Constructor;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import v0id.api.core.logging.LogLevel;
import v0id.api.exp.ExPApi;

public class GravityHelper
{
	public static Constructor<?> newGravFallingBlock;
	
	public static boolean isSupported(World world, BlockPos pos)
	{
		// If there is a full block under our block and there is a full block under that block and a full block under that block consider the block supported. 
		if (world.getBlockState(pos.down()).isOpaqueCube())
		{
			return true;
		}
		
		return false;
	}

	public static void doFall(IBlockState fallingBlock, World w, BlockPos pos, BlockPos trigger)
	{
		Block block = fallingBlock.getBlock();
		assert block instanceof IGravitySusceptible : String.format("Can't make block %s fall as it is not an instance of IGravitySusceptible!", fallingBlock);
		IGravitySusceptible gravityManager = (IGravitySusceptible)block;
		if (!gravityManager.fall(w, fallingBlock, pos, trigger))
		{
			fall(fallingBlock, w, pos);
		}
		
		tryFallRecursive(w, pos);
	}

	private static void tryFallRecursive(World w, BlockPos pos)
	{
		if (w.rand.nextBoolean())
		{
			BlockPos newFallPos = pos.add((w.rand.nextDouble() - w.rand.nextDouble()) * 6, (w.rand.nextDouble() - w.rand.nextDouble()) * 2, (w.rand.nextDouble() - w.rand.nextDouble()) * 6);
			IBlockState state = w.getBlockState(newFallPos);
			if (state.getBlock() instanceof IGravitySusceptible && ((IGravitySusceptible)state.getBlock()).canFall(w, state, newFallPos, pos))
			{
				doFall(state, w, newFallPos, pos);
			}
		}
	}

	private static void fall(IBlockState fallingBlock, World w, BlockPos pos)
	{
		try
		{
			if (newGravFallingBlock == null)
			{
				Class<?> clazz = Class.forName("v0id.exp.entity.EntityGravFallingBlock");
				newGravFallingBlock = clazz.getDeclaredConstructor(World.class, double.class, double.class, double.class, IBlockState.class);
			}
		
			EntityFallingBlock efb = (EntityFallingBlock) newGravFallingBlock.newInstance(w, (double)pos.getX() + 0.5d, (double)pos.getY(), (double)pos.getZ() + 0.5d, fallingBlock);
			if (!w.isRemote)
			{
				w.setBlockToAir(pos);
				w.spawnEntity(efb);
			}
		}
		catch (Exception ex)
		{
			ExPApi.apiLogger.log(LogLevel.Fatal, "Api could not reflect %s! Is the core mod ExPetrum installed?", ex, "v0id.exp.entity.EntityGravFallingBlock.<init>!");
		}
	}
}
