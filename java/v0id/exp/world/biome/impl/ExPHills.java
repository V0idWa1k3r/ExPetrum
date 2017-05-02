package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPHills extends ExPBiome
{
	public ExPHills(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.HILLS);
		this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.extraTreeChance = 0.05F;
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(8, EnumTreeType.BIRCH));
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(6, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(15, EnumTreeType.FIR));
        this.treesToGenerate.add(new TreeEntry(7, EnumTreeType.CEDAR));
        this.treesToGenerate.add(new TreeEntry(7, EnumTreeType.SPRUCE));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.PINE));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ROWAN));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TSUGA));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.VIBURNUM));
	}
	
	public static ExPHills create()
	{
		return new ExPHills(new Biome.BiomeProperties("hills").setBaseHeight(0.3F).setHeightVariation(0.1F), 1F, 1F, 0F, 0F);
	}
}
