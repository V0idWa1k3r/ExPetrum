package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPDenseForest extends ExPBiome
{
	public ExPDenseForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.FOREST);
		this.theBiomeDecorator.treesPerChunk = 20;
        this.theBiomeDecorator.grassPerChunk = 4;
	}

	public static ExPDenseForest create()
	{
		return new ExPDenseForest(new Biome.BiomeProperties("denseforest"), 1.2F, 1.2F, 0F, 0F);
	}
}
