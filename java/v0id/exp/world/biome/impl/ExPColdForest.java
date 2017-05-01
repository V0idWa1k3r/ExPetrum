package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPColdForest extends ExPBiome
{
	public ExPColdForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.FOREST, Type.COLD);
		this.theBiomeDecorator.treesPerChunk = 10;
        this.theBiomeDecorator.grassPerChunk = 2;
	}

	public static ExPColdForest create()
	{
		return new ExPColdForest(new Biome.BiomeProperties("coldforest"), 1.05F, 1F, -7F, 0F);
	}
}
