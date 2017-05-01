package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPWarmPlains extends ExPBiome
{
	public ExPWarmPlains(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.PLAINS, Type.HOT);
		this.theBiomeDecorator.treesPerChunk = 0;
        this.theBiomeDecorator.extraTreeChance = 0.05F;
	}
	
	public static ExPWarmPlains create()
	{
		return new ExPWarmPlains(new Biome.BiomeProperties("warmplains").setBaseHeight(0.125F).setHeightVariation(0.05F), 1F, 1F, 5F, 0F);
	}
}
