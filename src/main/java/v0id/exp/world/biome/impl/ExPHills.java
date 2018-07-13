package v0id.exp.world.biome.impl;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.exp.entity.impl.Cow;
import v0id.exp.entity.impl.Pig;
import v0id.exp.entity.impl.Sheep;
import v0id.exp.entity.impl.Wolf;
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.ShrubEntry;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPHills extends ExPBiome
{
	public ExPHills(BiomeProperties properties, float... biomedata)
	{
		super(properties, ExPRegistryNames.biomeHills, biomedata);
		this.decorator.treesPerChunk = 1;
        this.decorator.extraTreeChance = 0.05F;
        this.decorator.grassPerChunk = 4;
        this.decorator.deadBushPerChunk = 2;
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(8, EnumTreeType.BIRCH));
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(6, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(2, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(15, EnumTreeType.FIR));
        this.treesToGenerate.add(new TreeEntry(7, EnumTreeType.CEDAR));
        this.treesToGenerate.add(new TreeEntry(7, EnumTreeType.SPRUCE));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.PINE));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ROWAN));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TSUGA));
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.VIBURNUM));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.CHAMAEROPS));
        this.shrubsToGenerate.add(new ShrubEntry(2, EnumShrubType.RED_ROBIN));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.WHIN));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.PRUNUS));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.PEACH));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.PEAR));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.PLUM));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CHERRY));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.POMEGRANATE));
        this.spawnableCreatureList.add(new SpawnListEntry(Cow.class, 10, 2, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(Sheep.class, 10, 2, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(Pig.class, 10, 2, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(Wolf.class, 10, 1, 2));
	}
	
	@Override
	public void registerTypes()
	{
		BiomeDictionary.addTypes(this, Type.HILLS);
	}
	
	public static ExPHills create()
	{
		return new ExPHills(new Biome.BiomeProperties("hills").setBaseHeight(0.3F).setHeightVariation(0.1F), 1F, 1F, 0F, 0F);
	}
}
