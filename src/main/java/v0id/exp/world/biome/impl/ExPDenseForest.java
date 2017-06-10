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

public class ExPDenseForest extends ExPBiome
{
	public ExPDenseForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		this.decorator.treesPerChunk = 20;
        this.decorator.grassPerChunk = 4;
        this.decorator.deadBushPerChunk = 8;
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.BIRCH));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ASH));
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.WILLOW));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.MAPLE));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.REDWOOD));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.SPRUCE));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.PINE));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.ASPEN));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TUPELO));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.SPOTTED_LAUREL));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.CERCIS_CANADENSIS));
        this.shrubsToGenerate.add(new ShrubEntry(1, EnumShrubType.CHAMAEROPS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.VARIEGATED_DOGWOOD));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.ELAEAGNUS));
        this.shrubsToGenerate.add(new ShrubEntry(3, EnumShrubType.ILEX));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.LAURUS_NOBILIS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.RED_ROBIN));
	}
	
	@Override
	public void registerBiome(IForgeRegistry<Biome> registry)
	{
		super.registerBiome(registry);
		BiomeDictionary.addTypes(this, Type.FOREST);
	}

	public static ExPDenseForest create()
	{
		return new ExPDenseForest(new Biome.BiomeProperties("denseforest"), 1.2F, 1.2F, 0F, 0F);
	}
}
