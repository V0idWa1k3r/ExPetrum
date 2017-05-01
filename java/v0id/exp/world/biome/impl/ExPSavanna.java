package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPSavanna extends ExPBiome
{
	public ExPSavanna(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.PLAINS, Type.HOT);
		this.theBiomeDecorator.treesPerChunk = 0;
        this.theBiomeDecorator.extraTreeChance = 0.05F;
	}
	
	public static ExPSavanna create()
	{
		return new ExPSavanna(new Biome.BiomeProperties("savanna").setBaseHeight(0.125F).setHeightVariation(0.05F), 0.6F, 0.8F, 12F, 0F);
	}
}
