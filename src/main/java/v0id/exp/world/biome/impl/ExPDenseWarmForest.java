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

public class ExPDenseWarmForest extends ExPBiome
{
	public ExPDenseWarmForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, ExPRegistryNames.biomeDenseWarmForest, biomedata);
		this.decorator.treesPerChunk = 20;
        this.decorator.grassPerChunk = 4;
        this.decorator.deadBushPerChunk = 6;
        this.treesToGenerate.add(new TreeEntry(8, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.BIRCH));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.ACACIA));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.KAPOK));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.ASH));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(4, EnumTreeType.TEAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TUPELO));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.PARROTIA));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.SWEETGUM));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.JACKWOOD));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.SPOTTED_LAUREL));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.BOX));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.CORNUS_KOUSA));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.ELAEAGNUS));
        this.shrubsToGenerate.add(new ShrubEntry(3, EnumShrubType.EUONYMUS_JAPONICUS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.ILEX));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.MAHONIA_X_MEDIA));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.APPLE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.OLIVE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.ORANGE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.LEMON));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.APRICOT));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.WALNUT));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.GRAPEFRUIT));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.AVOCADO));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CARAMBOLA));
        this.spawnableCreatureList.add(new SpawnListEntry(Chicken.class, 10, 4, 16));
        this.spawnableCreatureList.add(new SpawnListEntry(Cow.class, 10, 2, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(Sheep.class, 10, 2, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(Pig.class, 10, 2, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(Wolf.class, 10, 1, 2));
    }
	
	@Override
	public void registerTypes()
	{
		BiomeDictionary.addTypes(this, Type.FOREST);
	}

	public static ExPDenseWarmForest create()
	{
		return new ExPDenseWarmForest(new Biome.BiomeProperties("densewarmforest"), 1.2F, 1.2F, 8F, 0F);
	}
}
