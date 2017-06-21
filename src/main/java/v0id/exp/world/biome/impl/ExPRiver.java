package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.world.biome.ExPBiome;

public class ExPRiver extends ExPBiome
{
	public ExPRiver(BiomeProperties properties, float... biomedata)
	{
		super(properties, ExPRegistryNames.biomeRiver, biomedata);
	}
	
	@Override
	public void registerTypes()
	{
		BiomeDictionary.addTypes(this, Type.RIVER);
	}
	
	public static ExPRiver create()
	{
		return new ExPRiver(new Biome.BiomeProperties("river").setBaseHeight(-0.5F).setHeightVariation(0.0F), 1F, 1.2F, 0F, 0F);
	}
}
