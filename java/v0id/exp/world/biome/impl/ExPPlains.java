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

public class ExPPlains extends ExPBiome
{
	public ExPPlains(BiomeProperties properties, float... biomedata)
	{
		super(properties, biomedata);
		this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.extraTreeChance = 0.05F;
        this.theBiomeDecorator.grassPerChunk = 8;
        this.theBiomeDecorator.deadBushPerChunk = 4;
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.BIRCH));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ASH));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.WILLOW));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.MAPLE));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.ROWAN));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TUPELO));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.KOELREUTERIA));
        this.shrubsToGenerate.add(new ShrubEntry(3, EnumShrubType.SPOTTED_LAUREL));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.CERCIS_CANADENSIS));
        this.shrubsToGenerate.add(new ShrubEntry(1, EnumShrubType.CHAMAEROPS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.VARIEGATED_DOGWOOD));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.EUONYMUS));
        this.shrubsToGenerate.add(new ShrubEntry(3, EnumShrubType.EUONYMUS_JAPONICUS));
        this.shrubsToGenerate.add(new ShrubEntry(2, EnumShrubType.KAPUKA));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.LAURUS_NOBILIS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.RED_ROBIN));
	}
	
	@Override
	public void registerBiome(IForgeRegistry<Biome> registry)
	{
		super.registerBiome(registry);
		BiomeDictionary.addTypes(this, Type.PLAINS);
	}
	
	public static ExPPlains create()
	{
		return new ExPPlains(new Biome.BiomeProperties("plains").setBaseHeight(0.125F).setHeightVariation(0.05F), 1F, 1F, 0F, 0F);
	}
}
