package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.entity.impl.*;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.ShrubEntry;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPSavanna extends ExPBiome
{
	public ExPSavanna(BiomeProperties properties, float... biomedata)
	{
		super(properties, ExPRegistryNames.biomeSavanna, biomedata);
		this.decorator.treesPerChunk = 1;
        this.decorator.extraTreeChance = 0.05F;
        this.decorator.grassPerChunk = 8;
        this.decorator.deadBushPerChunk = 3;
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
		this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.BANANA));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.APRICOT));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.WALNUT));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.AVOCADO));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CARAMBOLA));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.OLIVE));
        this.spawnableCreatureList.add(new SpawnListEntry(Chicken.class, 10, 4, 16));
        this.spawnableCreatureList.add(new SpawnListEntry(Cow.class, 10, 2, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(Sheep.class, 10, 2, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(Pig.class, 10, 2, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(Wolf.class, 10, 1, 2));
    }
	
	@Override
	public void registerTypes()
	{
		BiomeDictionary.addTypes(this, Type.PLAINS, Type.HOT);
	}
	
	public static ExPSavanna create()
	{
		return new ExPSavanna(new Biome.BiomeProperties("savanna").setBaseHeight(0.125F).setHeightVariation(0.05F), 0.6F, 0.8F, 12F, 0F);
	}
}
