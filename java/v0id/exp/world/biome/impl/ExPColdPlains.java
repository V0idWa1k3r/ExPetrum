package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPColdPlains extends ExPBiome
{
	public ExPColdPlains(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.PLAINS, Type.COLD);
		this.theBiomeDecorator.treesPerChunk = 0;
        this.theBiomeDecorator.extraTreeChance = 0.05F;
	}

	public static ExPColdPlains create()
	{
		return new ExPColdPlains(new Biome.BiomeProperties("coldplains").setBaseHeight(0.125F).setHeightVariation(0.05F), 1F, 1F, -9F, 0F);
	}
}
