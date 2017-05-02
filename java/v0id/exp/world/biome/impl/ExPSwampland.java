package v0id.exp.world.biome.impl;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.block.property.EnumWaterLilyType;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPSwampland extends ExPBiome
{
	public ExPSwampland(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.SWAMP);
		this.theBiomeDecorator.treesPerChunk = 3;
		this.treesToGenerate.add(new TreeEntry(8, EnumTreeType.KALOPANAX));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.OAK));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.WILLOW));
		this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.MANGROVE));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.JACKWOOD));
	}
	
	public static ExPSwampland create()
	{
		return new ExPSwampland(new Biome.BiomeProperties("swampland").setBaseHeight(-0.2F).setHeightVariation(0.1F), 1.5F, 1.1F, -1F, 0.1F);
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

                        if (d0 < 0.12D)
                        {
                        	EnumWaterLilyType lilyType = EnumWaterLilyType.values()[rand.nextInt(EnumWaterLilyType.values().length)];
                            chunkPrimerIn.setBlockState(j, k + 1, i, ExPBlocks.waterLily.getDefaultState().withProperty(ExPBlockProperties.LILY_TYPE, lilyType));
                        }
                    }

                    break;
                }
            }
        }

        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}
