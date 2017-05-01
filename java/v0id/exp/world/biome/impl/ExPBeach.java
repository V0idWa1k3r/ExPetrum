package v0id.exp.world.biome.impl;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.exp.world.biome.ExPBiome;

public class ExPBeach extends ExPBiome
{
	public ExPBeach(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		this.topBlock = Blocks.SAND.getDefaultState();
		this.fillerBlock = Blocks.SAND.getDefaultState();
		BiomeDictionary.addTypes(this, Type.BEACH);
	}
	
	public static ExPBeach create()
	{
		return new ExPBeach(new Biome.BiomeProperties("beach").setBaseHeight(-1F).setHeightVariation(0.1F), 0.9F, 1.5F, 0F, 0F);
	}

	@Override
	public boolean isWaterSalt()
	{
		return true;
	}
}
