package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPWarmPlains extends ExPBiome
{
	public ExPWarmPlains(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.PLAINS, Type.HOT);
		this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.extraTreeChance = 0.05F;
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(8, EnumTreeType.ACACIA));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.BAOBAB));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.EUCALYPTUS));
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TEAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TUPELO));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.PARROTIA));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.SWEETGUM));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.JACKWOOD));
	}
	
	public static ExPWarmPlains create()
	{
		return new ExPWarmPlains(new Biome.BiomeProperties("warmplains").setBaseHeight(0.125F).setHeightVariation(0.05F), 1F, 1F, 5F, 0F);
	}
}
