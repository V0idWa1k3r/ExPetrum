package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.entity.impl.Cow;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.ShrubEntry;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPDenseColdForest extends ExPBiome
{
	public ExPDenseColdForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, ExPRegistryNames.biomeDenseColdForest, biomedata);
		this.decorator.treesPerChunk = 20;
        this.decorator.grassPerChunk = 4;
        this.decorator.deadBushPerChunk = 8;
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
        this.shrubsToGenerate.add(new ShrubEntry(3, EnumShrubType.CHAMAEROPS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.ELAEAGNUS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.WHIN));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.PRUNUS));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.APPLE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.PEAR));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CHERRY));
        this.spawnableCreatureList.add(new SpawnListEntry(Cow.class, 10, 2, 4));
	}
	
	@Override
	public void registerTypes()
	{
		BiomeDictionary.addTypes(this, Type.FOREST);
	}

	public static ExPDenseColdForest create()
	{
		return new ExPDenseColdForest(new Biome.BiomeProperties("densecoldforest"), 1.2F, 1.2F, -8F, 0F);
	}
}
