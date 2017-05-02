package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPColdForest extends ExPBiome
{
	public ExPColdForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.FOREST, Type.COLD);
		this.theBiomeDecorator.treesPerChunk = 10;
        this.theBiomeDecorator.grassPerChunk = 4;
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.BIRCH));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(30, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.ASH));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.WILLOW));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.MAPLE));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.FIR));
        this.treesToGenerate.add(new TreeEntry(20, EnumTreeType.SPRUCE));
        this.treesToGenerate.add(new TreeEntry(20, EnumTreeType.PINE));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ASPEN));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ROWAN));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TUPELO));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TSUGA));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.VIBURNUM));
	}

	public static ExPColdForest create()
	{
		return new ExPColdForest(new Biome.BiomeProperties("coldforest"), 1.05F, 1F, -7F, 0F);
	}
}
