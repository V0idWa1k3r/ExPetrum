package v0id.core.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

@SuppressWarnings("unused")
public class ChunkUtils
{
	public static final ListMultimap<Integer, ChunkPos> loadedChunks = ArrayListMultimap.create();
	
	public static boolean isChunkLoaded(DimChunkPos pos)
	{
		return loadedChunks.containsEntry(pos.dim, pos.pos);
	}
	
	public static void setBiomeAt(Biome b, BlockPos pos, World w)
	{
		byte[] bArray = w.getChunkFromBlockCoords(pos).getBiomeArray();
		int i = pos.getX() & 15;
        int j = pos.getZ() & 15;
        bArray[j << 4 | i] = (byte) Biome.getIdForBiome(b);
	}
}
