package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPColdPlains extends ExPBiome
{
	public ExPColdPlains(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		BiomeDictionary.addTypes(this, Type.PLAINS, Type.COLD);
		this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.extraTreeChance = 0.05F;
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.BIRCH));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(30, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.ASH));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.WILLOW));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.FIR));
        this.treesToGenerate.add(new TreeEntry(20, EnumTreeType.SPRUCE));
        this.treesToGenerate.add(new TreeEntry(20, EnumTreeType.PINE));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ASPEN));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.ROWAN));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TUPELO));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TSUGA));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.VIBURNUM));
	}

	public static ExPColdPlains create()
	{
		return new ExPColdPlains(new Biome.BiomeProperties("coldplains").setBaseHeight(0.125F).setHeightVariation(0.05F), 1F, 1F, -9F, 0F);
	}
}
