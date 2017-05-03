package v0id.exp.world.gen;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.IntCache;

public class FeatureProvider
{
	public GenLayer genLayerRock;
	public GenLayer genLayerSoil;
	public Cache cacheRocks;
	public Cache cacheSoil;
	public final long seed;
	
	public FeatureProvider(long seed)
	{
		super();
		this.seed = seed;
		this.initCaches();
		this.genGenLayers(seed);
	}

	public void initCaches()
	{
		this.cacheRocks = new Cache(this, this::providerStone);
		this.cacheSoil = new Cache(this, this::providerSoil);
	}
	
	public void genGenLayers(long seed)
	{
		GenLayer islandsInTheVoid = new GenLayerIsland(1L);
		GenLayerRandomIsland moreIslands = new GenLayerRandomIsland(1L, islandsInTheVoid);
		GenLayerRandomIsland evenMoreIslandNoise = new GenLayerRandomIsland(0, moreIslands);
		for (int i = 1; i < 32; ++i)
		{
			evenMoreIslandNoise = new GenLayerRandomIsland(10 + i * 20, moreIslands);
		}
		
		GenLayer zoomAfter = new GenLayerFuzzyZoom(2000L, evenMoreIslandNoise);
		GenLayerZoom zoomedIslands = new GenLayerZoom(2001L, zoomAfter);
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, zoomedIslands, GenLayerEdge.Mode.SPECIAL);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        GenLayerZoom genlayerzoom1 = new GenLayerZoom(2002L, genlayeredge);
        genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);
        GenLayer genlayer4 = GenLayerZoom.magnify(1000L, genlayerzoom1, 0);
        GenLayer lvt_7_1_ = GenLayerZoom.magnify(1000L, genlayer4, 0);
        GenLayer lvt_9_1_ = GenLayerZoom.magnify(1000L, lvt_7_1_, 2);
        GenLayer genlayer5 = GenLayerZoom.magnify(1000L, lvt_7_1_, 2);
        for (int k = 0; k < 3; ++k)
        {
        	genlayer4 = new GenLayerZoom((long)(1000 + k), genlayer4);
        }
        
        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, genlayer4);
        GenLayer genlayer3 = new GenLayerVoronoiZoom(10L, genlayersmooth1);
        genlayer3.initWorldGenSeed(seed);
		this.genLayerRock = new GenLayerBytes(seed, genlayer3);
		this.genLayerSoil = new GenLayerBytes(new Random(seed).nextLong(), genlayer3);
		
	}
	
	public void cleanupCaches()
	{
		this.cacheRocks.cleanupCache();
		this.cacheSoil.cleanupCache();
	}
	
	public byte getByte(BlockPos pos, Cache of)
	{
		return of.getByte(pos.getX(), pos.getZ());
	}
	
	public byte[] providerStone(Pair<Triple<byte[], Integer, Integer>, Triple<Integer, Integer, Boolean>> p)
	{
		return this.getBytes(p.getLeft().getLeft(), p.getLeft().getMiddle(), p.getLeft().getRight(), p.getRight().getLeft(), p.getRight().getMiddle(), p.getRight().getRight(), this.genLayerRock, this.cacheRocks);
	}
	
	public byte[] providerSoil(Pair<Triple<byte[], Integer, Integer>, Triple<Integer, Integer, Boolean>> p)
	{
		return this.getBytes(p.getLeft().getLeft(), p.getLeft().getMiddle(), p.getLeft().getRight(), p.getRight().getLeft(), p.getRight().getMiddle(), p.getRight().getRight(), this.genLayerSoil, this.cacheSoil);
	}
	
	public byte[] getBytes(byte[] toReuse, int x, int z, int w, int l, boolean cacheFlag, GenLayer of, Cache cacheOf)
	{
		IntCache.resetIntCache();
		if (toReuse == null || toReuse.length < w * l)
        {
			toReuse = new byte[w * l];
        }

        if (cacheFlag && w == 16 && l == 16 && (x & 15) == 0 && (z & 15) == 0)
        {
            byte[] bytes = cacheOf.getCacheAt(x, z);
            System.arraycopy(bytes, 0, toReuse, 0, w * l);
            return toReuse;
        }
        else
        {
            int[] aint = of.getInts(x, z, w, l);
            for (int i = 0; i < w * l; ++i)
            {
            	toReuse[i] = (byte) aint[i];
            }

            return toReuse;
        }
	}
	
	public class GenLayerRandomIsland extends GenLayer
	{
		public GenLayerRandomIsland(long p_i2119_1_, GenLayer p_i2119_3_)
	    {
	        super(p_i2119_1_);
	        this.parent = p_i2119_3_;
	    }

	    /**
	     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
	     * amounts, or Biome ID's based on the particular GenLayer subclass.
	     */
	    @Override
		public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	    {
	        int i = areaX - 1;
	        int j = areaY - 1;
	        int k = areaWidth + 2;
	        int l = areaHeight + 2;
	        int[] aint = this.parent.getInts(i, j, k, l);
	        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

	        for (int i1 = 0; i1 < areaHeight; ++i1)
	        {
	            for (int j1 = 0; j1 < areaWidth; ++j1)
	            {
	                int k1 = aint[j1 + 0 + (i1 + 0) * k];
	                int l1 = aint[j1 + 2 + (i1 + 0) * k];
	                int i2 = aint[j1 + 0 + (i1 + 2) * k];
	                int j2 = aint[j1 + 2 + (i1 + 2) * k];
	                int k2 = aint[j1 + 1 + (i1 + 1) * k];
	                this.initChunkSeed((long)(j1 + areaX), (long)(i1 + areaY));

	                if (k2 != 0 || k1 == 0 && l1 == 0 && i2 == 0 && j2 == 0)
	                {
	                    if (k2 > 0 && (k1 == 0 || l1 == 0 || i2 == 0 || j2 == 0))
	                    {
	                        if (this.nextInt(5) == 0)
	                        {
	                            if (k2 == 4)
	                            {
	                                aint1[j1 + i1 * areaWidth] = this.nextInt(16);
	                            }
	                            else
	                            {
	                                aint1[j1 + i1 * areaWidth] = this.nextInt(16);
	                            }
	                        }
	                        else
	                        {
	                            aint1[j1 + i1 * areaWidth] = k2;
	                        }
	                    }
	                    else
	                    {
	                        aint1[j1 + i1 * areaWidth] = k2;
	                    }
	                }
	                else
	                {
	                    int l2 = 1;
	                    int i3 = 1;

	                    if (k1 != 0 && this.nextInt(l2++) == 0)
	                    {
	                        i3 = k1;
	                    }

	                    if (l1 != 0 && this.nextInt(l2++) == 0)
	                    {
	                        i3 = l1;
	                    }

	                    if (i2 != 0 && this.nextInt(l2++) == 0)
	                    {
	                        i3 = i2;
	                    }

	                    if (j2 != 0 && this.nextInt(l2++) == 0)
	                    {
	                        i3 = j2;
	                    }

	                    if (this.nextInt(3) == 0)
	                    {
	                        aint1[j1 + i1 * areaWidth] = i3;
	                    }
	                    else if (i3 == 4)
	                    {
	                        aint1[j1 + i1 * areaWidth] = this.nextInt(16);
	                    }
	                    else
	                    {
	                        aint1[j1 + i1 * areaWidth] = this.nextInt(16);
	                    }
	                }
	            }
	        }

	        return aint1;
	    }
	}
	
	public class GenLayerBytes extends GenLayer
	{
		public GenLayerBytes(long seed, GenLayer parent)
		{
			super(seed);
			this.parent = parent;
		}

		@Override
		public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
		{
			int[] ints = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
			int[] newBlock = IntCache.getIntCache(areaHeight * areaWidth);
			for (int i = 0; i < areaHeight; ++i)
	        {
	            for (int j = 0; j < areaWidth; ++j)
	            {
	            	this.initChunkSeed((long)(j + areaX), (long)(i + areaY));
	                int k = ints[j + i * areaWidth];
	                newBlock[j + i * areaWidth] = k & 255;
	            }
	        }
			
			return newBlock;
		}
		
	}
	
	public class Cache
	{
		public long lastCleanupTime;
		public final Long2ObjectMap<Block> cacheMap = new Long2ObjectOpenHashMap(4096);
		public final List<Block> cache = Lists.newArrayList();
		public FeatureProvider provider;
		public Function<Pair<Triple<byte[], Integer, Integer>, Triple<Integer, Integer, Boolean>>, byte[]> bytesProvider;

		public Cache(FeatureProvider provider, Function<Pair<Triple<byte[], Integer, Integer>, Triple<Integer, Integer, Boolean>>, byte[]> bytesProvider)
		{
			super();
			this.provider = provider;
			this.bytesProvider = bytesProvider;
		}

		public long getLongmapKey(int x, int z)
		{
			return (long)x & 4294967295L | ((long)z & 4294967295L) << 32;
		}
		
		public void cleanupCache()
		{
			long current = MinecraftServer.getCurrentTimeMillis();
			long delta = current - this.lastCleanupTime;
			if (delta > 3000)
			{
				this.lastCleanupTime = current;
				for (int i = 0; i < this.cache.size(); ++i)
	            {
					Block b = this.cache.get(i);
					long deltaBlock = current - b.lastAccessTime;
					if (deltaBlock > 10000 || deltaBlock < 0)
					{
						this.cache.remove(i--);
	                    long i1 = b.toLongmapKey();
	                    this.cacheMap.remove(i1);
					}
	            }
			}
		}
		
		public Block getCachedBlock(int x, int z)
		{
			x = x >> 4;
	        z = z >> 4;
	        long longKey = this.getLongmapKey(x, z);
	        Block b = this.cacheMap.get(longKey);
	        if (b == null)
	        {
	            b = new Block(x, z);
	            this.cacheMap.put(longKey, b);
	            this.cache.add(b);
	        }

	        b.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
	        return b;
		}
		
		public byte getByte(int x, int z)
	    {
			return this.getCachedBlock(x, z).getByte(x, z);
	    }
		
		public byte[] getCacheAt(int x, int z)
		{
			return this.getCachedBlock(x, z).bytes;
		}
		
		public class Block
		{
			public byte[] bytes = new byte[256];
			public int x;
			public int z;
			public long lastAccessTime;
			
			public Block(int x, int z)
			{
				this.x = x;
				this.z = z;
				this.bytes = Cache.this.bytesProvider.apply(Pair.of(Triple.of(this.bytes, x << 4, z << 4), Triple.of(16, 16, false)));
			}
			
			public byte getByte(int x, int z)
			{
				return this.bytes[x & 15 | (z & 15) << 4];
			}
			
			public long toLongmapKey()
			{
				return (long)this.x & 4294967295L | ((long)this.z & 4294967295L) << 32;
			}
		}
	}
}
