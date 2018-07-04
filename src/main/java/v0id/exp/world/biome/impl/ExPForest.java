package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.entity.impl.Chicken;
import v0id.exp.entity.impl.Cow;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.ShrubEntry;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPForest extends ExPBiome
{
	public ExPForest(BiomeProperties properties, float... biomedata)
	{
		super(properties, ExPRegistryNames.biomeForest, biomedata);
		this.decorator.treesPerChunk = 10;
        this.decorator.grassPerChunk = 4;
        this.decorator.deadBushPerChunk = 4;
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.BIRCH));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ASH));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.WILLOW));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.MAPLE));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.REDWOOD));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.SPRUCE));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.PINE));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.ASPEN));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TUPELO));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.KOELREUTERIA));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.SPOTTED_LAUREL));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.CERCIS_CANADENSIS));
        this.shrubsToGenerate.add(new ShrubEntry(1, EnumShrubType.CHAMAEROPS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.VARIEGATED_DOGWOOD));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.ELAEAGNUS));
        this.shrubsToGenerate.add(new ShrubEntry(3, EnumShrubType.EUONYMUS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.LAURUS_NOBILIS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.RED_ROBIN));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.APPLE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.OLIVE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.ORANGE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.PLUM));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.LEMON));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CHERRY));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.POMEGRANATE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.GRAPEFRUIT));
        this.spawnableCreatureList.add(new SpawnListEntry(Chicken.class, 10, 4, 16));
        this.spawnableCreatureList.add(new SpawnListEntry(Cow.class, 10, 2, 4));
    }
	
	@Override
	public void registerTypes()
	{
		BiomeDictionary.addTypes(this, Type.FOREST);
	}

	public static ExPForest create()
	{
		return new ExPForest(new Biome.BiomeProperties("forest"), 1.05F, 1F, 3F, 0F);
	}
}
