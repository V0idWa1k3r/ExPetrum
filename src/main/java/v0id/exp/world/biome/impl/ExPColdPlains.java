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
import v0id.exp.world.biome.ExPBiome;
import v0id.exp.world.gen.ShrubEntry;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPColdPlains extends ExPBiome
{
	public ExPColdPlains(BiomeProperties properties, float... biomedata)
	{
		super(properties, ExPRegistryNames.biomeColdPlains, biomedata);
		this.decorator.treesPerChunk = 1;
        this.decorator.extraTreeChance = 0.05F;
        this.decorator.grassPerChunk = 8;
        this.decorator.deadBushPerChunk = 2;
        this.treesToGenerate.add(new TreeEntry(3, EnumTreeType.KALOPANAX));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.BIRCH));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.CHESTNUT));
        this.treesToGenerate.add(new TreeEntry(30, EnumTreeType.OAK));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.HICKORY));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.ASH));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.WILLOW));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.FIR));
        this.treesToGenerate.add(new TreeEntry(20, EnumTreeType.SPRUCE));
        this.treesToGenerate.add(new TreeEntry(20, EnumTreeType.PINE));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ELM));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.ASPEN));
        this.treesToGenerate.add(new TreeEntry(5, EnumTreeType.ROWAN));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TUPELO));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.TSUGA));
        this.treesToGenerate.add(new TreeEntry(10, EnumTreeType.VIBURNUM));
        this.shrubsToGenerate.add(new ShrubEntry(8, EnumShrubType.CHAMAEROPS));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.KAPUKA));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.WHIN));
        this.shrubsToGenerate.add(new ShrubEntry(10, EnumShrubType.PRUNUS));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.APPLE));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.PEACH));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.PEAR));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.CHERRY));
        this.treesToGenerate.add(new TreeEntry(1, EnumTreeType.OLIVE));
        this.spawnableCreatureList.add(new SpawnListEntry(Cow.class, 10, 2, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(Sheep.class, 10, 2, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(Pig.class, 10, 2, 4));
    }
	
	@Override
	public void registerTypes()
	{
		BiomeDictionary.addTypes(this, Type.PLAINS, Type.COLD);
	}

	public static ExPColdPlains create()
	{
		return new ExPColdPlains(new Biome.BiomeProperties("coldplains").setBaseHeight(0.125F).setHeightVariation(0.05F), 1F, 1F, -9F, 0F);
	}
}
