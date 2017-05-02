package v0id.exp.world.biome;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import v0id.api.core.logging.LogLevel;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.world.gen.ITreeGenerator;
import v0id.api.exp.world.gen.TreeGenerators;

public class ExPBiomeDecorator extends BiomeDecorator
{
	public static boolean printedWarnings;
	
	@Override
	public void decorate(World worldIn, Random random, Biome biome, BlockPos pos)
    {
		if (!printedWarnings)
		{
			printWarnings();
		}
		
		this.doTreePass(worldIn, random, biome, pos);
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
			if (treeGen.generate(worldIn, random, at.up()))
			{
				return;
			}
		}
		
	}
}
