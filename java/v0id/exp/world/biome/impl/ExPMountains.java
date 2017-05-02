package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPMountains extends ExPBiome
{
	public ExPMountains(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.MOUNTAIN);
		this.theBiomeDecorator.treesPerChunk = 3;
		this.theBiomeDecorator.grassPerChunk = 4;
		this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.KALOPANAX));
		this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.BIRCH));
		this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CHESTNUT));
		this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.OAK));
		this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.HICKORY));
		this.treesToGenerate.add(new TreeEntry(15, EnumTreeType.FIR));
		this.treesToGenerate.add(new TreeEntry(15, EnumTreeType.CEDAR));
		this.treesToGenerate.add(new TreeEntry(7, EnumTreeType.SPRUCE));
		this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.PINE));
		this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.ELM));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ROWAN));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TSUGA));
	}

	public static ExPMountains create()
	{
		return new ExPMountains(new Biome.BiomeProperties("mountains").setBaseHeight(0.45F).setHeightVariation(0.3F), 1F, 1F, -4F, 0F);
	}
}
