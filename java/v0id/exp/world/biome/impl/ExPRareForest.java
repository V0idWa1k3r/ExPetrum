package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPRareForest extends ExPBiome
{
	public ExPRareForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.FOREST);
		this.theBiomeDecorator.treesPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 1;
	}

	public static ExPRareForest create()
	{
		return new ExPRareForest(new Biome.BiomeProperties("rareforest"), 1.05F, 1F, 3F, 0F);
	}
}
