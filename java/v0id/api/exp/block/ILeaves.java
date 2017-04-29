package v0id.api.exp.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import v0id.api.core.VoidApi;

public interface ILeaves
{
	public static final int LEAVES_CHECK_RADIUS = 4;
	
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
	
	// Note that you do NOT need to use these if ou do not like to deal with threads. It just makes checks not on the server's tick thread which can save lots of milliseconds if your check is expensive.
	// For the default 9x9x9(729 blocks) it might be not necessary at all.
	// You want to use this if your checks take more than ~70us(nanoseconds) as that is the relative time required to create a new thread.
	// Measure nanoseconds with System.nanoTime or timersto see if it is more efficient.
	// Or create your own implementation which uses thread pools(preinitialized threads) which would 100% be worth it for ANY checks.
	// Google it!
	// Well... I am not entirely sure if what I've said is true at all :(
	// Since jvm1.6(java 6) it seems that jvm recycles threads that have previously finished their work
	// That means that in theory any new thread is kinda? in a thread pool already?
	// This WILL vary from jvm to jvm though for sure
	// Not sure what any of the above means? Google it!
	default void onWoodConnectionCheckFinished(World w, IBlockState state, BlockPos pos, boolean result)
	{
		if (!result)
		{
			w.setBlockToAir(pos);
		}
	}
	
	default void checkWoodConnectionMultiThreaded(final World w, final IBlockState state, final BlockPos pos)
	{
		Thread t = new Thread(() -> checkIsAttached(w, pos, this, state));
		t.setDaemon(true);
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
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
