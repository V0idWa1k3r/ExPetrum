package v0id.exp.world.gen;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import v0id.api.exp.data.ExPBiomes;

public class WorldTypeExP extends WorldType
{
	public WorldTypeExP(String name)
	{
		super(name);
	}

	@Override
	public GenLayer getBiomeLayer(long worldSeed, GenLayer parentLayer, ChunkGeneratorSettings chunkProviderSettings)
	{
		 net.minecraft.world.gen.layer.GenLayer ret = new GenLayerBiomeMod(200L, parentLayer, this, chunkProviderSettings);
	     ret = net.minecraft.world.gen.layer.GenLayerZoom.magnify(1000L, ret, 2);
	     ret = new net.minecraft.world.gen.layer.GenLayerBiomeEdge(1000L, ret);
	     return ret;
	}

	@Override
	public float getCloudHeight()
	{
		return Float.MAX_VALUE;
	}
	
	@Override
	public BiomeProvider getBiomeProvider(World world)
    {
		return new BiomeProviderExP(world.getWorldInfo());
    }
	
	public static class BiomeCacheExP extends BiomeCache
	{
		private final BiomeProvider chunkManager;
	    /** The last time this BiomeCache was cleaned, in milliseconds. */
	    private long lastCleanupTime;
	    /** The map of keys to BiomeCacheBlocks. Keys are based on the chunk x, z coordinates as (x | z << 32). */
	    private final Long2ObjectMap<BiomeCache.Block> cacheMap = new Long2ObjectOpenHashMap(4096);
	    /** The list of cached BiomeCacheBlocks */
	    private final List<BiomeCache.Block> cache = Lists.<BiomeCache.Block>newArrayList();

	    public BiomeCacheExP(BiomeProvider chunkManagerIn)
	    {
	    	super(chunkManagerIn);
	        this.chunkManager = chunkManagerIn;
	    }

	    /**
	     * Returns a biome cache block at location specified.
	     */
	    @Override
		public BiomeCache.Block getBiomeCacheBlock(int x, int z)
	    {
	        x = x >> 4;
	        z = z >> 4;
	        long i = (long)x & 4294967295L | ((long)z & 4294967295L) << 32;
	        BiomeCache.Block biomecache$block = (BiomeCache.Block)this.cacheMap.get(i);

	        if (biomecache$block == null)
	        {
	            biomecache$block = new BiomeCache.Block(x, z);
	            this.cacheMap.put(i, biomecache$block);
	            this.cache.add(biomecache$block);
	        }

	        biomecache$block.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
	        return biomecache$block;
	    }

	    @Override
		public Biome getBiome(int x, int z, Biome defaultValue)
	    {
	        Biome biome = this.getBiomeCacheBlock(x, z).getBiome(x, z);
	        return biome == null ? defaultValue : biome;
	    }

	    /**
	     * Removes BiomeCacheBlocks from this cache that haven't been accessed in at least 30 seconds.
	     */
	    @Override
		public void cleanupCache()
	    {
	        long i = MinecraftServer.getCurrentTimeMillis();
	        long j = i - this.lastCleanupTime;

	        if (j > 1000L || j < 0L)
	        {
	            this.lastCleanupTime = i;

	            for (int k = 0; k < this.cache.size(); ++k)
	            {
	                BiomeCache.Block biomecache$block = (BiomeCache.Block)this.cache.get(k);
	                long l = i - biomecache$block.lastAccessTime;

	                if (l > 3000L || l < 0L)
	                {
	                    this.cache.remove(k--);
	                    long i1 = (long)biomecache$block.x & 4294967295L | ((long)biomecache$block.z & 4294967295L) << 32;
	                    this.cacheMap.remove(i1);
	                }
	            }
	        }
	    }

	    /**
	     * Returns the array of cached biome types in the BiomeCacheBlock at the given location.
	     */
	    @Override
		public Biome[] getCachedBiomes(int x, int z)
	    {
	        return this.getBiomeCacheBlock(x, z).biomes;
	    }

	    public class Block
	    {
	        /** The array of biome types stored in this BiomeCacheBlock. */
	        public Biome[] biomes = new Biome[256];
	        /** The x coordinate of the BiomeCacheBlock. */
	        public int xPosition;
	        /** The z coordinate of the BiomeCacheBlock. */
	        public int zPosition;
	        /** The last time this BiomeCacheBlock was accessed, in milliseconds. */
	        public long lastAccessTime;

	        public Block(int x, int z)
	        {
	            this.xPosition = x;
	            this.zPosition = z;
	            BiomeCacheExP.this.chunkManager.getBiomes(this.biomes, x << 4, z << 4, 16, 16, false);
	        }

	        /**
	         * Returns the BiomeGenBase related to the x, z position from the cache block.
	         */
	        public Biome getBiome(int x, int z)
	        {
	            return this.biomes[x & 15 | (z & 15) << 4];
	        }
	    }
	}
	
	public static class BiomeProviderExP extends BiomeProvider
	{
		private ChunkGeneratorSettings settings;
	    private GenLayer genBiomes;
	    /** A GenLayer containing the indices into BiomeGenBase.biomeList[] */
	    private GenLayer biomeIndexLayer;
	    /** The biome list. */
	    private final BiomeCacheExP biomeCache;
	    /** A list of biomes that the player can spawn in. */
	    private final List<Biome> biomesToSpawnIn;
	    public static List<Biome> allowedBiomes = Lists.newArrayList();
	    public FeatureProvider featureProvider;
	    
	    protected BiomeProviderExP()
	    {
	        this.biomeCache = new BiomeCacheExP(this);
	        allowedBiomes.clear();
	        allowedBiomes.add(ExPBiomes.plains);
	        allowedBiomes.add(ExPBiomes.forest);
	        allowedBiomes.add(ExPBiomes.mountains);
	        allowedBiomes.add(ExPBiomes.cold_plains);
	        allowedBiomes.add(ExPBiomes.cold_forest);
	        allowedBiomes.add(ExPBiomes.warm_plains);
	        allowedBiomes.add(ExPBiomes.warm_forest);
	        allowedBiomes.add(ExPBiomes.dense_forest);
	        allowedBiomes.add(ExPBiomes.dense_warm_forest);
	        allowedBiomes.add(ExPBiomes.dense_cold_forest);
	        allowedBiomes.add(ExPBiomes.swampland);
	        allowedBiomes.add(ExPBiomes.jungle);
	        allowedBiomes.add(ExPBiomes.desert);
	        allowedBiomes.add(ExPBiomes.rare_forest);
	        allowedBiomes.add(ExPBiomes.hills);
	        allowedBiomes.add(ExPBiomes.savanna);
	        this.biomesToSpawnIn = Lists.newArrayList(allowedBiomes);
	    }

	    private BiomeProviderExP(long seed, WorldType worldTypeIn, String options)
	    {
	        this();

	        if (worldTypeIn == WorldType.CUSTOMIZED && !options.isEmpty())
	        {
	            this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(options).build();
	        }

	        GenLayer[] agenlayer = GenLayer.initializeAllBiomeGenerators(seed, worldTypeIn, this.settings);
	        agenlayer = getModdedBiomeGenerators(worldTypeIn, seed, agenlayer);
	        this.genBiomes = agenlayer[0];
	        this.biomeIndexLayer = agenlayer[1];
	        this.featureProvider = new FeatureProvider(seed);
	    }

	    public BiomeProviderExP(WorldInfo info)
	    {
	        this(info.getSeed(), info.getTerrainType(), info.getGeneratorOptions());
	    }

	    @Override
		public List<Biome> getBiomesToSpawnIn()
	    {
	        return this.biomesToSpawnIn;
	    }

	    public Biome swapHackBiome(Biome b)
	    {
	    	return b == Biomes.RIVER || b == Biomes.FROZEN_RIVER ? ExPBiomes.river : b == Biomes.OCEAN || b == Biomes.DEEP_OCEAN || b == Biomes.FROZEN_OCEAN ? ExPBiomes.ocean : b == Biomes.BEACH ? ExPBiomes.beach : b == Biomes.PLAINS ? ExPBiomes.plains : b == Biomes.FOREST_HILLS ? ExPBiomes.hills : b == Biomes.JUNGLE || b == Biomes.JUNGLE_EDGE || b == Biomes.JUNGLE_HILLS ? ExPBiomes.jungle : b == Biomes.FOREST ? ExPBiomes.forest : b;
	    }
	    
	    @Override
		public Biome getBiome(BlockPos pos)
	    {
	        return this.getBiome(pos, (Biome)null);
	    }

	    @Override
		public Biome getBiome(BlockPos pos, Biome defaultBiome)
	    {
	        return swapHackBiome(this.biomeCache.getBiome(pos.getX(), pos.getZ(), defaultBiome));
	    }

	    @Override
		public float getTemperatureAtHeight(float p_76939_1_, int p_76939_2_)
	    {
	        return p_76939_1_;
	    }

	    @Override
		public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height)
	    {
	        IntCache.resetIntCache();

	        if (biomes == null || biomes.length < width * height)
	        {
	            biomes = new Biome[width * height];
	        }

	        int[] aint = this.genBiomes.getInts(x, z, width, height);

	        try
	        {
	            for (int i = 0; i < width * height; ++i)
	            {
	                biomes[i] = swapHackBiome(Biome.getBiome(aint[i], Biomes.DEFAULT));
	            }

	            return biomes;
	        }
	        catch (Throwable throwable)
	        {
	            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
	            CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
	            crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(biomes.length));
	            crashreportcategory.addCrashSection("x", Integer.valueOf(x));
	            crashreportcategory.addCrashSection("z", Integer.valueOf(z));
	            crashreportcategory.addCrashSection("w", Integer.valueOf(width));
	            crashreportcategory.addCrashSection("h", Integer.valueOf(height));
	            throw new ReportedException(crashreport);
	        }
	    }

	    /**
	     * Gets biomes to use for the blocks and loads the other data like temperature and humidity onto the
	     * WorldChunkManager.
	     */
	    @Override
		public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth)
	    {
	        return this.getBiomes(oldBiomeList, x, z, width, depth, true);
	    }

	    /**
	     * Gets a list of biomes for the specified blocks.
	     */
	    @Override
		public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
	    {
	        IntCache.resetIntCache();

	        if (listToReuse == null || listToReuse.length < width * length)
	        {
	            listToReuse = new Biome[width * length];
	        }

	        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
	        {
	            Biome[] abiome = this.biomeCache.getCachedBiomes(x, z);
	            for (int i = 0; i < width * length; ++i)
	            {
	            	listToReuse[i] = swapHackBiome(abiome[i]);
	            }
	            
	            return listToReuse;
	        }
	        else
	        {
	            int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);

	            for (int i = 0; i < width * length; ++i)
	            {
	            	listToReuse[i] = swapHackBiome(Biome.getBiome(aint[i], Biomes.DEFAULT));
	            }

	            return listToReuse;
	        }
	    }

	    /**
	     * checks given Chunk's Biomes against List of allowed ones
	     */
	    @Override
		public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed)
	    {
	        return false;
	    }

	    @Override
		@Nullable
	    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random)
	    {
	        IntCache.resetIntCache();
	        int i = x - range >> 2;
	        int j = z - range >> 2;
	        int k = x + range >> 2;
	        int l = z + range >> 2;
	        int i1 = k - i + 1;
	        int j1 = l - j + 1;
	        int[] aint = this.genBiomes.getInts(i, j, i1, j1);
	        BlockPos blockpos = null;
	        int k1 = 0;

	        for (int l1 = 0; l1 < i1 * j1; ++l1)
	        {
	            int i2 = i + l1 % i1 << 2;
	            int j2 = j + l1 / i1 << 2;
	            Biome biome = Biome.getBiome(aint[l1]);

	            if (biomes.contains(biome) && (blockpos == null || random.nextInt(k1 + 1) == 0))
	            {
	                blockpos = new BlockPos(i2, 0, j2);
	                ++k1;
	            }
	        }

	        return blockpos;
	    }

	    /**
	     * Calls the WorldChunkManager's biomeCache.cleanupCache()
	     */
	    @Override
		public void cleanupCache()
	    {
	        this.biomeCache.cleanupCache();
	        this.featureProvider.cleanupCaches();
	    }

	    @Override
		public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original)
	    {
	        net.minecraftforge.event.terraingen.WorldTypeEvent.InitBiomeGens event = new net.minecraftforge.event.terraingen.WorldTypeEvent.InitBiomeGens(worldType, seed, original);
	        net.minecraftforge.common.MinecraftForge.TERRAIN_GEN_BUS.post(event);
	        return event.getNewBiomeGens();
	    }

	    @Override
		public boolean isFixedBiome()
	    {
	        return false;
	    }

	    @Override
		public Biome getFixedBiome()
	    {
	        return null;
	    }
	}
	
	public class GenLayerBiomeMod extends GenLayer
	{
		@SuppressWarnings("unchecked")
	    private java.util.List<net.minecraftforge.common.BiomeManager.BiomeEntry> biomes = Lists.newArrayList();
	    private final ChunkGeneratorSettings settings;

	    public GenLayerBiomeMod(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, ChunkGeneratorSettings p_i45560_5_)
	    {
	        super(p_i45560_1_);
	        this.parent = p_i45560_3_;
	        this.biomes.add(new BiomeEntry(ExPBiomes.plains, 10));
            this.biomes.add(new BiomeEntry(ExPBiomes.forest, 10));
            this.biomes.add(new BiomeEntry(ExPBiomes.mountains, 10));
            this.biomes.add(new BiomeEntry(ExPBiomes.dense_forest, 10));
            this.biomes.add(new BiomeEntry(ExPBiomes.swampland, 10));
            this.biomes.add(new BiomeEntry(ExPBiomes.rare_forest, 10));
            this.biomes.add(new BiomeEntry(ExPBiomes.hills, 10));
            this.biomes.add(new BiomeEntry(ExPBiomes.cold_forest, 10));
        	this.biomes.add(new BiomeEntry(ExPBiomes.dense_cold_forest, 10));
        	this.biomes.add(new BiomeEntry(ExPBiomes.cold_plains, 10));
        	this.biomes.add(new BiomeEntry(ExPBiomes.savanna, 10));
        	this.biomes.add(new BiomeEntry(ExPBiomes.warm_forest, 10));
        	this.biomes.add(new BiomeEntry(ExPBiomes.warm_plains, 10));
        	this.biomes.add(new BiomeEntry(ExPBiomes.dense_warm_forest, 10));
        	this.biomes.add(new BiomeEntry(ExPBiomes.jungle, 10));
	        this.biomes.add(new BiomeEntry(ExPBiomes.desert, 10));
	        this.settings = p_i45560_5_;
	    }

	    /**
	     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
	     * amounts, or Biome ID's based on the particular GenLayer subclass.
	     */
	    @Override
		public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	    {
	        int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
	        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

	        for (int i = 0; i < areaHeight; ++i)
	        {
	            for (int j = 0; j < areaWidth; ++j)
	            {
	                this.initChunkSeed((long)(j + areaX), (long)(i + areaY));
	                int k = aint[j + i * areaWidth];
	                k = k & -3841;
	                if (isBiomeOceanic(k))
	                {
	                    aint1[j + i * areaWidth] = k;
	                }
	                else 
	                {
	                	aint1[j + i * areaWidth] = Biome.getIdForBiome(getWeightedBiomeEntry().biome);
	                }
	            }
	        }

	        return aint1;
	    }

	    protected net.minecraftforge.common.BiomeManager.BiomeEntry getWeightedBiomeEntry()
	    {
	        int totalWeight = net.minecraft.util.WeightedRandom.getTotalWeight(this.biomes);
	        int weight = nextInt(totalWeight);
	        return (net.minecraftforge.common.BiomeManager.BiomeEntry)net.minecraft.util.WeightedRandom.getRandomItem(this.biomes, weight);
	    }
	}
}
