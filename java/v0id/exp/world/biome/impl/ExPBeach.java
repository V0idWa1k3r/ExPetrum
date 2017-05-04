package v0id.exp.world.biome.impl;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPBeach extends ExPBiome
{
	public ExPBeach(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		this.topBlock = Blocks.SAND.getDefaultState();
		this.fillerBlock = Blocks.SAND.getDefaultState();
		BiomeDictionary.addTypes(this, Type.BEACH);
		this.theBiomeDecorator.treesPerChunk = 3;
		this.theBiomeDecorator.deadBushPerChunk = 0;
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.PALM));
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
