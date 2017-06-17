package v0id.exp.world.biome.impl;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.ShrubEntry;
import v0id.exp.world.gen.tree.TreeEntry;

import java.util.Random;

public class ExPLake extends ExPBiome
{
	public ExPLake(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		this.decorator.treesPerChunk = 3;
		this.decorator.grassPerChunk = 3;
		this.decorator.deadBushPerChunk = 6;
		this.treesToGenerate.add(new TreeEntry(8, EnumTreeType.KALOPANAX));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.OAK));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.WILLOW));
		this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.MANGROVE));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.JACKWOOD));
		this.shrubsToGenerate.add(new ShrubEntry(20, EnumShrubType.SPOTTED_LAUREL));
		this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.CERCIS_CANADENSIS));
		this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.CORNUS_KOUSA));
		this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.EUONYMUS_JAPONICUS));
		this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.LAURUS_NOBILIS));
		this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.RED_ROBIN));
		this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.APPLE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.OLIVE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CHERRY));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.POMEGRANATE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CARAMBOLA));
	}
	
	@Override
	public void registerBiome(IForgeRegistry<Biome> registry)
	{
		super.registerBiome(registry);
		BiomeDictionary.addTypes(this, Type.SWAMP);
	}
	
	public static ExPLake create()
	{
		return new ExPLake(new Biome.BiomeProperties("lake").setBaseHeight(-0.2F).setHeightVariation(0.1F), 1.5F, 1.1F, -1F, 0.1F);
	}
	
	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        double d0 = GRASS_COLOR_NOISE.getValue((double)x * 0.25D, (double)z * 0.25D);

        if (d0 > 0.0D)
        {
            int i = x & 15;
            int j = z & 15;

            for (int k = 255; k >= 0; --k)
            {
                if (chunkPrimerIn.getBlockState(j, k, i).getMaterial() != Material.AIR)
                {
                    if (k == 62 && chunkPrimerIn.getBlockState(j, k, i) != this.FRESH_WATER)
                    {
                        chunkPrimerIn.setBlockState(j, k, i, this.FRESH_WATER);
                    }

                    break;
                }
            }
        }

        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}
