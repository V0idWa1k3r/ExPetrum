package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPDenseWarmForest extends ExPBiome
{
	public ExPDenseWarmForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.FOREST);
		this.theBiomeDecorator.treesPerChunk = 20;
        this.theBiomeDecorator.grassPerChunk = 4;
	}

	public static ExPDenseWarmForest create()
	{
		return new ExPDenseWarmForest(new Biome.BiomeProperties("densewarmforest"), 1.2F, 1.2F, 8F, 0F);
	}
}
