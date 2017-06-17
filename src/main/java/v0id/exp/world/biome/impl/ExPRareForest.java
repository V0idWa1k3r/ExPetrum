package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.ShrubEntry;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPRareForest extends ExPBiome
{
	public ExPRareForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		this.decorator.treesPerChunk = 4;
        this.decorator.grassPerChunk = 8;
        this.decorator.deadBushPerChunk = 4;
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
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.CERCIS_CANADENSIS));
        this.shrubsToGenerate.add(new ShrubEntry(1, EnumShrubType.CHAMAEROPS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.EUONYMUS));
        this.shrubsToGenerate.add(new ShrubEntry(2, EnumShrubType.KAPUKA));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.LAURUS_NOBILIS));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.APPLE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.PEACH));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.ORANGE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.PLUM));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.APRICOT));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.GRAPEFRUIT));
	}
	
	@Override
	public void registerBiome(IForgeRegistry<Biome> registry)
	{
		super.registerBiome(registry);
		BiomeDictionary.addTypes(this, Type.FOREST);
	}

	public static ExPRareForest create()
	{
		return new ExPRareForest(new Biome.BiomeProperties("rareforest"), 1.05F, 1F, 3F, 0F);
	}
}
