package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPHills extends ExPBiome
{
	public ExPHills(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.HILLS);
		this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.extraTreeChance = 0.05F;
	}
	
	public static ExPHills create()
	{
		return new ExPHills(new Biome.BiomeProperties("hills").setBaseHeight(0.3F).setHeightVariation(0.1F), 1F, 1F, 0F, 0F);
	}
}
