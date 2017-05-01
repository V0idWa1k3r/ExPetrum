package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPPlains extends ExPBiome
{
	public ExPPlains(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.PLAINS);
		this.theBiomeDecorator.treesPerChunk = 0;
        this.theBiomeDecorator.extraTreeChance = 0.05F;
	}
	
	public static ExPPlains create()
	{
		return new ExPPlains(new Biome.BiomeProperties("plains").setBaseHeight(0.125F).setHeightVariation(0.05F), 1F, 1F, 0F, 0F);
	}
}
