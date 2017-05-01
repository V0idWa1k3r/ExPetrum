package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPWarmForest extends ExPBiome
{
	public ExPWarmForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.FOREST, Type.HOT);
		this.theBiomeDecorator.treesPerChunk = 10;
        this.theBiomeDecorator.grassPerChunk = 2;
	}

	public static ExPWarmForest create()
	{
		return new ExPWarmForest(new Biome.BiomeProperties("warmforest"), 1.05F, 1F, 9F, 0F);
	}
}
