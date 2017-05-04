package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.ShrubEntry;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPSavanna extends ExPBiome
{
	public ExPSavanna(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.PLAINS, Type.HOT);
		this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.extraTreeChance = 0.05F;
        this.theBiomeDecorator.grassPerChunk = 8;
        this.theBiomeDecorator.deadBushPerChunk = 3;
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(60, EnumTreeType.ACACIA));
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(20, EnumTreeType.BAOBAB));
        this.treesToGenerate.add(new TreeEntry(20, EnumTreeType.EUCALYPTUS));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TEAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.JACKWOOD));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.EUONYMUS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.LAURUS_NOBILIS));
        this.shrubsToGenerate.add(new ShrubEntry(3, EnumShrubType.MAHONIA_X_MEDIA));
	}
	
	public static ExPSavanna create()
	{
		return new ExPSavanna(new Biome.BiomeProperties("savanna").setBaseHeight(0.125F).setHeightVariation(0.05F), 0.6F, 0.8F, 12F, 0F);
	}
}
