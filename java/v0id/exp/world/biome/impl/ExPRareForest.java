package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPRareForest extends ExPBiome
{
	public ExPRareForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.FOREST);
		this.theBiomeDecorator.treesPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 1;
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.BIRCH));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ASH));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.REDWOOD));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.SPRUCE));
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.PINE));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.ASPEN));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.KOELREUTERIA));
	}

	public static ExPRareForest create()
	{
		return new ExPRareForest(new Biome.BiomeProperties("rareforest"), 1.05F, 1F, 3F, 0F);
	}
}
