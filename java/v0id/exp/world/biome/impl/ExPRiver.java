package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.exp.world.biome.ExPBiome;

public class ExPRiver extends ExPBiome
{
	public ExPRiver(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
	}
	
	@Override
	public void registerBiome(IForgeRegistry<Biome> registry)
	{
		super.registerBiome(registry);
		BiomeDictionary.addTypes(this, Type.RIVER);
	}
	
	public static ExPRiver create()
	{
		return new ExPRiver(new Biome.BiomeProperties("river").setBaseHeight(-0.5F).setHeightVariation(0.0F), 1F, 1.2F, 0F, 0F);
	}
}
