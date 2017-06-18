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

public class ExPJungle extends ExPBiome
{
	public ExPJungle(BiomeProperties properties, float... biomedata)
	{
		super(properties, "jungle", biomedata);
		this.decorator.treesPerChunk = 20;
        this.decorator.grassPerChunk = 4;
        this.decorator.deadBushPerChunk = 6;
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.KAPOK));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(20, EnumTreeType.MANGROVE));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.JACKWOOD));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.SPOTTED_LAUREL));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.BOX));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.CORNUS_KOUSA));
        this.shrubsToGenerate.add(new ShrubEntry(3, EnumShrubType.EUONYMUS_JAPONICUS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.ILEX));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.MAHONIA_X_MEDIA));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.OLIVE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.LEMON));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.APRICOT));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.WALNUT));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.GRAPEFRUIT));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.AVOCADO));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CARAMBOLA));
	}
	
	@Override
	public void registerBiome(IForgeRegistry<Biome> registry)
	{
		super.registerBiome(registry);
		BiomeDictionary.addTypes(this, Type.JUNGLE, Type.HOT, Type.LUSH);
	}

	public static ExPJungle create()
	{
		return new ExPJungle(new Biome.BiomeProperties("jungle"), 1.3F, 1.5F, 6F, 0.1F);
	}
}
