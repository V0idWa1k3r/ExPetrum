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

public class ExPMountains extends ExPBiome
{
	public ExPMountains(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		this.decorator.treesPerChunk = 3;
		this.decorator.grassPerChunk = 4;
		this.decorator.deadBushPerChunk = 2;
		this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.KALOPANAX));
		this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.BIRCH));
		this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CHESTNUT));
		this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.OAK));
		this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.HICKORY));
		this.treesToGenerate.add(new TreeEntry(15, EnumTreeType.FIR));
		this.treesToGenerate.add(new TreeEntry(15, EnumTreeType.CEDAR));
		this.treesToGenerate.add(new TreeEntry(7, EnumTreeType.SPRUCE));
		this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.PINE));
		this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.ELM));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ROWAN));
		this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TSUGA));
		this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.CHAMAEROPS));
		this.shrubsToGenerate.add(new ShrubEntry(2, EnumShrubType.RED_ROBIN));
		this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.WHIN));
		this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.PRUNUS));
		this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.PEACH));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.PLUM));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CHERRY));
	}

	@Override
	public void registerBiome(IForgeRegistry<Biome> registry)
	{
		super.registerBiome(registry);
		BiomeDictionary.addTypes(this, Type.MOUNTAIN);
	}
	
	public static ExPMountains create()
	{
		return new ExPMountains(new Biome.BiomeProperties("mountains").setBaseHeight(0.45F).setHeightVariation(0.3F), 1F, 1F, -4F, 0F);
	}
}
