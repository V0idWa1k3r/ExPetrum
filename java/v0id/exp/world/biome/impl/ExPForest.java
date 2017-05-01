package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPForest extends ExPBiome
{
	public ExPForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.FOREST);
		this.theBiomeDecorator.treesPerChunk = 10;
        this.theBiomeDecorator.grassPerChunk = 2;
	}

	public static ExPForest create()
	{
		return new ExPForest(new Biome.BiomeProperties("forest"), 1.05F, 1F, 3F, 0F);
	}
}
