package v0id.exp.world.biome.impl;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPDesert extends ExPBiome
{
	public ExPDesert(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.PLAINS);
		this.theBiomeDecorator.treesPerChunk = -999;
		this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();
	}
	
	public static ExPDesert create()
	{
		return new ExPDesert(new Biome.BiomeProperties("desert").setBaseHeight(0.125F).setHeightVariation(0.05F), 1.1F, 0.2F, 12F, 0F);
	}
}
