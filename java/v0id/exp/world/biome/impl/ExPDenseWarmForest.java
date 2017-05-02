package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPDenseWarmForest extends ExPBiome
{
	public ExPDenseWarmForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.FOREST);
		this.theBiomeDecorator.treesPerChunk = 20;
        this.theBiomeDecorator.grassPerChunk = 4;
        this.treesToGenerate.add(new TreeEntry(8, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.BIRCH));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.ACACIA));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.KAPOK));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.ASH));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(4, EnumTreeType.TEAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TUPELO));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.PARROTIA));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.SWEETGUM));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.JACKWOOD));
	}

	public static ExPDenseWarmForest create()
	{
		return new ExPDenseWarmForest(new Biome.BiomeProperties("densewarmforest"), 1.2F, 1.2F, 8F, 0F);
	}
}
