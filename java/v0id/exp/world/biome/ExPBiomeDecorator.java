package v0id.exp.world.biome;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import v0id.api.core.logging.LogLevel;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.event.world.gen.EventGenPebble;
import v0id.api.exp.event.world.gen.EventGenTree;
import v0id.api.exp.event.world.gen.EventGenVegetation;
import v0id.api.exp.world.gen.ITreeGenerator;
import v0id.api.exp.world.gen.TreeGenerators;
import v0id.exp.world.gen.biome.PebbleGenerator;
import v0id.exp.world.gen.biome.VegetationGenerator;

public class ExPBiomeDecorator extends BiomeDecorator
{
	public static boolean printedWarnings;
	public static final VegetationGenerator genVegetation = new VegetationGenerator();
	public static final PebbleGenerator genPebble = new PebbleGenerator();
	
	@Override
	public void decorate(World worldIn, Random random, Biome biome, BlockPos pos)
    {
		if (!printedWarnings)
		{
			printWarnings();
		}
		
		this.doTreePass(worldIn, random, biome, pos);
		this.doVegetationPass(worldIn, random, biome, pos);
		this.doPebblePass(worldIn, random, biome, pos);
    }

	private static void printWarnings()
	{
		ExPMisc.modLogger.log(LogLevel.Fine, "Hello.");
		ExPMisc.modLogger.log(LogLevel.Fine, "ExPetrum would like to warn you about it's terrain generation");
		ExPMisc.modLogger.log(LogLevel.Fine, "It usually tries to avoid chunk runaway effects");
		ExPMisc.modLogger.log(LogLevel.Fine, "However it is not always possible, especially with ore pockets");
		ExPMisc.modLogger.log(LogLevel.Fine, "You might see some FML warnings about cascading worldgen lag later down the log file");
		ExPMisc.modLogger.log(LogLevel.Fine, "Please do not report them to V0id, he is well aware");
		ExPMisc.modLogger.log(LogLevel.Fine, "However that should not be the issue for the most part as chunk runaway should happen rarely");
		ExPMisc.modLogger.log(LogLevel.Fine, "Thanks for reading this. Have a nice day :)");
		printedWarnings = true;
	}
	
	public void doVegetationPass(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		for (int i = 0; i < this.grassPerChunk; ++i)
		{
			this.vegetationPassGenerate(worldIn, rand, biome, pos);
		}
	}
	
	public void doPebblePass(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		this.pebblePassGenerate(worldIn, rand, biome, pos);
	}
	
	public void pebblePassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		int x = rand.nextInt(16) + 8;
		int z = rand.nextInt(16) + 8;
		BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
		WorldGenerator gen = genPebble;
		EventGenPebble event = new EventGenPebble(worldIn, at, rand, gen);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
		{
			return;
		}
		
		event.generator.generate(worldIn, rand, at);
	}
	
	public void vegetationPassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		int x = rand.nextInt(16) + 8;
		int z = rand.nextInt(16) + 8;
		BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
		WorldGenerator vegetationGen = genVegetation;
		EventGenVegetation event = new EventGenVegetation(worldIn, at, rand, vegetationGen);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
		{
			return;
		}
		
		event.generator.generate(worldIn, rand, at);
	}
	
	public void doTreePass(World worldIn, Random random, Biome biome, BlockPos pos)
	{
		for (int i = 0; i < this.treesPerChunk; ++i)
		{
			treePassGenerate(worldIn, random, biome, pos);
		}
		
		if (random.nextDouble() <= this.extraTreeChance)
		{
			treePassGenerate(worldIn, random, biome, pos);
		}
	}
	
	public void treePassGenerate(World worldIn, Random random, Biome biome, BlockPos pos)
	{
		if (!(biome instanceof ExPBiome))
		{
			return;
		}
		
		int tries = 0;
		while (random.nextBoolean())
		{
			int x = random.nextInt(16) + 8;
			int z = random.nextInt(16) + 8;
			BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
			EnumTreeType type = ((ExPBiome)biome).provideTreeToGenerate(random).orElse(null);
			if (type == null)
			{
				return;
			}
			
			ITreeGenerator treeGen = TreeGenerators.generators.get(type);
			EventGenTree treeEvent = new EventGenTree(worldIn, at, random, treeGen);
			if (MinecraftForge.TERRAIN_GEN_BUS.post(treeEvent))
			{
				return;
			}
			else if (treeEvent.generator.generate(worldIn, random, at.up()))
			{
				return;
			}
		}
		
	}
}
