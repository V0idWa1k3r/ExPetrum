package v0id.exp.world.biome.impl;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.exp.world.biome.ExPBiome;

public class ExPOcean extends ExPBiome
{
	public ExPOcean(BiomeProperties properties, float... biomedata)
	{
		super(properties, "ocean", biomedata);
		this.decorator.deadBushPerChunk = 0;
		this.topBlock = Blocks.SAND.getDefaultState();
		this.fillerBlock = Blocks.SAND.getDefaultState();
	}
	
	@Override
	public void registerBiome(IForgeRegistry<Biome> registry)
	{
		super.registerBiome(registry);
		BiomeDictionary.addTypes(this, Type.OCEAN);
	}
	
	public static ExPOcean create()
	{
		return new ExPOcean(new Biome.BiomeProperties("ocean").setBaseHeight(-1F).setHeightVariation(0.1F), 0.8F, 1.8F, 0F, 0F);
	}

	@Override
	public Biome.TempCategory getTempCategory()
    {
        return Biome.TempCategory.OCEAN;
    }
	
	@Override
	public boolean isWaterSalt()
	{
		return true;
	}
}
