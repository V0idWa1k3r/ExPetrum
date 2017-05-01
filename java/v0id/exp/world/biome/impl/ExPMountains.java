package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPMountains extends ExPBiome
{
	public ExPMountains(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.MOUNTAIN);
	}

	public static ExPMountains create()
	{
		return new ExPMountains(new Biome.BiomeProperties("mountains").setBaseHeight(0.45F).setHeightVariation(0.3F), 1F, 1F, -4F, 0F);
	}
}
