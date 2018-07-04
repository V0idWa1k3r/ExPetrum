package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.entity.impl.Chicken;
import v0id.exp.entity.impl.Cow;
import v0id.exp.entity.impl.Pig;
import v0id.exp.entity.impl.Sheep;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.ShrubEntry;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPWarmPlains extends ExPBiome
{
	public ExPWarmPlains(BiomeProperties properties, float... biomedata)
	{
		super(properties, ExPRegistryNames.biomeWarmPlains, biomedata);
		this.decorator.treesPerChunk = 1;
        this.decorator.extraTreeChance = 0.05F;
        this.decorator.grassPerChunk = 8;
        this.decorator.deadBushPerChunk = 4;
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
        this.shrubsToGenerate.add(new ShrubEntry(5, EnumShrubType.SPOTTED_LAUREL));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.CORNUS_KOUSA));
        this.shrubsToGenerate.add(new ShrubEntry(3, EnumShrubType.EUONYMUS));
        this.shrubsToGenerate.add(new ShrubEntry(3, EnumShrubType.EUONYMUS_JAPONICUS));
        this.shrubsToGenerate.add(new ShrubEntry(1, EnumShrubType.ILEX));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.LAURUS_NOBILIS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.MAHONIA_X_MEDIA));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.BANANA));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.APRICOT));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.WALNUT));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.AVOCADO));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CARAMBOLA));
        this.spawnableCreatureList.add(new SpawnListEntry(Chicken.class, 10, 4, 16));
        this.spawnableCreatureList.add(new SpawnListEntry(Cow.class, 10, 2, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(Sheep.class, 10, 2, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(Pig.class, 10, 2, 4));
    }
	
	@Override
	public void registerTypes()
	{
		BiomeDictionary.addTypes(this, Type.PLAINS, Type.HOT);
	}
	
	public static ExPWarmPlains create()
	{
		return new ExPWarmPlains(new Biome.BiomeProperties("warmplains").setBaseHeight(0.125F).setHeightVariation(0.05F), 1F, 1F, 5F, 0F);
	}
}
