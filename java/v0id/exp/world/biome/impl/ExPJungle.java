package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPJungle extends ExPBiome
{
	public ExPJungle(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.JUNGLE, Type.HOT, Type.LUSH);
		this.theBiomeDecorator.treesPerChunk = 50;
        this.theBiomeDecorator.grassPerChunk = 2;
	}

	public static ExPJungle create()
	{
		return new ExPJungle(new Biome.BiomeProperties("jungle"), 1.3F, 1.5F, 6F, 0.1F);
	}
}
