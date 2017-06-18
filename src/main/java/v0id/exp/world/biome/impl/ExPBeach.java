package v0id.exp.world.biome.impl;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPBeach extends ExPBiome
{
	public ExPBeach(BiomeProperties properties, float... biomedata)
	{
		super(properties, "beach", biomedata);
		this.topBlock = Blocks.SAND.getDefaultState();
		this.fillerBlock = Blocks.SAND.getDefaultState();
		this.decorator.treesPerChunk = 3;
		this.decorator.deadBushPerChunk = 0;
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.PALM));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.BANANA));
	}
	
	@Override
	public void registerBiome(IForgeRegistry<Biome> registry)
	{
		super.registerBiome(registry);
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
