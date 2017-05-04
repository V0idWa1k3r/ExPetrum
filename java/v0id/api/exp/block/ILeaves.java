package v0id.api.exp.block;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import v0id.api.core.VoidApi;

public interface ILeaves
{
	public static final int LEAVES_CHECK_RADIUS = 5;
	
	public static final ExecutorService pool = Executors.newFixedThreadPool(4);
	
	// of == blockstate to compare to
	// state = this state
	// pos = this position
	boolean isSameWoodType(World w, IBlockState state, BlockPos pos, IBlockState of);
	
	// calculate for seasons on your own... or return -1 and handle in textures(this is the way default implementaton works)
	// -1 == 0xffffffff(100% opaque, 100% white)
	int getLeavesColor(IBlockAccess access, IBlockState state, BlockPos pos);
	
	int getLeavesColorForMeta(int meta);
	
	EnumLeafState getLeavesState(IBlockAccess access, IBlockState state, BlockPos pos);
	
	// Really more for convenience as this is not directly referenced anywhere
	boolean isEvergreen(IBlockAccess access, IBlockState state, BlockPos pos);
	
	default void onWoodConnectionCheckFinished(World w, IBlockState state, BlockPos pos, boolean result)
	{
		if (!result)
		{
			w.setBlockToAir(pos);
		}
	}
	
	// Note that you do NOT need to use these if you do not like to deal with threads. It just makes checks not on the server's tick thread which can save lots of milliseconds if your check is expensive.
		// For the default 9x9x9(729 blocks) it might be not necessary at all.
	default void checkWoodConnectionMultiThreaded(final World w, final IBlockState state, final BlockPos pos)
	{
		pool.submit(() -> checkIsAttached(w, pos, this, state));
	}
	
	public static void checkIsAttached(World w, BlockPos origin, ILeaves leaf, IBlockState leafState)
	{
		IThreadListener runOn = w.isRemote ? VoidApi.proxy.getClientListener() : (WorldServer)w;
		
		for (int dx = -LEAVES_CHECK_RADIUS; dx <= LEAVES_CHECK_RADIUS; ++dx)
		{
			for (int dy = -LEAVES_CHECK_RADIUS; dy <= LEAVES_CHECK_RADIUS; ++dy)
			{
				for (int dz = -LEAVES_CHECK_RADIUS; dz <= LEAVES_CHECK_RADIUS; ++dz)
				{
					BlockPos offset = origin.add(dx, dy, dz);
					if (leaf.isSameWoodType(w, leafState, origin, w.getBlockState(offset)))
					{
						if (runOn != null)
						{
							runOn.addScheduledTask(() -> leaf.onWoodConnectionCheckFinished(w, leafState, origin, true));
						}
						
						return;
					}
				}
			}
		}
		
		if (runOn != null)
		{
			runOn.addScheduledTask(() -> leaf.onWoodConnectionCheckFinished(w, leafState, origin, false));
		}
	}
}
