package v0id.exp.world.biome;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import v0id.api.core.logging.LogLevel;
import v0id.api.exp.block.EnumShrubState;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.event.world.gen.*;
import v0id.api.exp.event.world.gen.EventGenVegetation.Type;
import v0id.api.exp.world.EnumSeason;
import v0id.api.exp.world.IBiome;
import v0id.api.exp.world.IExPWorld;
import v0id.api.exp.world.gen.ITreeGenerator;
import v0id.api.exp.world.gen.TreeGenerators;
import v0id.exp.world.gen.biome.*;

import java.util.Random;

public class ExPBiomeDecorator extends BiomeDecorator
{
	public static boolean printedWarnings;
	public static final VegetationGenerator genVegetation = new VegetationGenerator();
	public static final PebbleGenerator genPebble = new PebbleGenerator();
	public static final BoulderGenerator genBoulders = new BoulderGenerator();
	public static final CattailGenerator genCattails = new CattailGenerator();
	public static final SeaweedGenerator genSeaweed = new SeaweedGenerator();
	public static final CoralGenerator genCoral = new CoralGenerator();
	public static final OilGenerator genOil = new OilGenerator();
	
	@Override
	public void decorate(World worldIn, Random random, Biome biome, BlockPos pos)
    {
		if (!printedWarnings)
		{
			printWarnings();
		}
		
		this.doTreePass(worldIn, random, biome, pos);
		this.doVegetationPass(worldIn, random, biome, pos);
		this.doShrubsPass(worldIn, random, biome, pos);
		this.doBerryBushesPass(worldIn, random, biome, pos);
		this.doPebblePass(worldIn, random, biome, pos);
		this.doBoulderPass(worldIn, random, biome, pos);
		this.doOrePass(worldIn, random, biome, pos);
		this.doCropsPass(worldIn, random, biome, pos);
		this.doCoralPass(worldIn, random, biome, pos);
		this.doOilPass(worldIn, random, biome, pos);
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
	
	public void doCoralPass(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		this.coralPassGenerate(worldIn, rand, biome, pos);
	}
	
	public void doCropsPass(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		this.cropsPassGenerate(worldIn, rand, biome, pos);
	}

    public void doBerryBushesPass(World worldIn, Random rand, Biome biome, BlockPos pos)
    {
        for (int i = 0; i < this.deadBushPerChunk; ++i)
        {
            if (rand.nextBoolean())
            {
                this.beyyBushesPassGenerate(worldIn, rand, biome, pos);
            }
        }
    }
	
	public void doShrubsPass(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		for (int i = 0; i < this.deadBushPerChunk; ++i)
		{
			this.shrubsPassGenerate(worldIn, rand, biome, pos);
		}
	}
	
	public void doVegetationPass(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		for (int i = 0; i < this.grassPerChunk; ++i)
		{
			this.tallgrassPassGenerate(worldIn, rand, biome, pos);
		}
		
		this.cattailPassGenerate(worldIn, rand, biome, pos);
		this.seaweedPassGenerate(worldIn, rand, biome, pos);
	}
	
	public void doPebblePass(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		this.pebblePassGenerate(worldIn, rand, biome, pos);
	}
	
	public void doBoulderPass(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		this.boulderPassGenerate(worldIn, rand, biome, pos);
	}
	
	public void doOrePass(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		if (rand.nextDouble() <= 0.1)
		{
			this.orePassGenerate(worldIn, rand, biome, pos);
		}
	}

	public void doOilPass(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		this.oilPassGenerate(worldIn, rand, biome, pos);
	}

	public void oilPassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		float chance = BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) ? 0.03125F : 0.015625F;
		if (rand.nextFloat() < chance)
		{
			int x = rand.nextInt(16) + 8;
			int z = rand.nextInt(16) + 8;
			BlockPos at = new BlockPos(x, 0, z);
			EventGenOil event = new EventGenOil(worldIn, at, rand, genOil);
			if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
			{
				return;
			}

			event.generator.generate(worldIn, rand, pos.add(at));
		}
	}
	
	public void coralPassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		if (!BiomeDictionary.areSimilar(biome, Biomes.OCEAN))
		{
			return;
		}
		
		int x = rand.nextInt(16) + 8;
		int z = rand.nextInt(16) + 8;
		BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
		EventGenCoral event = new EventGenCoral(worldIn, at, rand, genCoral);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
		{
			return;
		}
		
		event.generator.generate(worldIn, rand, at);
	}
	
	public void cropsPassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		if (!(biome instanceof ExPBiome))
		{
			return;
		}
		
		int x = rand.nextInt(16) + 8;
		int z = rand.nextInt(16) + 8;
		BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
		
		WorldGenerator cropsGen = new CropGenerator((ExPBiome) biome);
		EventGenVegetation event = new EventGenVegetation(worldIn, at, rand, cropsGen, Type.WILD_CROP);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
		{
			return;
		}
		
		event.generator.generate(worldIn, rand, at);
	}
	
	public void shrubsPassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		if (!(biome instanceof ExPBiome))
		{
			return;
		}
		
		int x = rand.nextInt(16) + 8;
		int z = rand.nextInt(16) + 8;
		BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
		EnumShrubType typeToGenerate = ((ExPBiome)biome).provideShrubToGenerate(rand).orElse(null);
		if (typeToGenerate == null)
		{
			return;
		}
		
		EnumSeason currentSeason = IExPWorld.of(worldIn).getCurrentSeason();
		EnumShrubState stateToGenerate = currentSeason == EnumSeason.WINTER ? EnumShrubState.DEAD : currentSeason == EnumSeason.AUTUMN ? EnumShrubState.AUTUMN : EnumShrubState.NORMAL;
		WorldGenerator shrubsGen = new ShrubGenerator(typeToGenerate, stateToGenerate);
		EventGenVegetation event = new EventGenVegetation(worldIn, at, rand, shrubsGen, Type.BUSH);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
		{
			return;
		}
		
		event.generator.generate(worldIn, rand, at);
	}

    public void beyyBushesPassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
    {
        if (!(biome instanceof ExPBiome))
        {
            return;
        }

        int x = rand.nextInt(16) + 8;
        int z = rand.nextInt(16) + 8;
        BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
        EnumSeason currentSeason = IExPWorld.of(worldIn).getCurrentSeason();
        EnumShrubState stateToGenerate = currentSeason == EnumSeason.WINTER ? EnumShrubState.DEAD : currentSeason == EnumSeason.AUTUMN ? EnumShrubState.AUTUMN : rand.nextFloat() < 0.3 ? EnumShrubState.BLOOMING : EnumShrubState.NORMAL;
        WorldGenerator gen = new BerryBushGenerator(stateToGenerate);
        EventGenVegetation event = new EventGenVegetation(worldIn, at, rand, gen, Type.BERRY_BUSH);
        if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
        {
            return;
        }

        event.generator.generate(worldIn, rand, at);
    }
	
	public void cattailPassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		if (!(biome instanceof IBiome) || ((IBiome)biome).isWaterSalt())
		{
			return;
		}
		
		int x = rand.nextInt(16) + 8;
		int z = rand.nextInt(16) + 8;
		BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
		EventGenVegetation event = new EventGenVegetation(worldIn, at, rand, genCattails, Type.CATTAIL);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
		{
			return;
		}
		
		event.generator.generate(worldIn, rand, at);
	}
	
	public void seaweedPassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		if (!(biome instanceof IBiome) || !((IBiome)biome).isWaterSalt())
		{
			return;
		}
		
		int x = rand.nextInt(16) + 8;
		int z = rand.nextInt(16) + 8;
		BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
		EventGenVegetation event = new EventGenVegetation(worldIn, at, rand, genSeaweed, Type.SEAWEED);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
		{
			return;
		}
		
		event.generator.generate(worldIn, rand, at);
	}
	
	public void orePassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		int x = rand.nextInt(16) + 8;
		int z = rand.nextInt(16) + 8;
		int y = 30 + rand.nextInt(worldIn.getHeight(pos.add(x, 0, z)).getY() - 40);
		BlockPos at = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
		WorldGenerator gen = new OreGenerator(rand);
		EventGenOre event = new EventGenOre(worldIn, at, rand, gen);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
		{
			return;
		}
		
		event.generator.generate(worldIn, rand, at);
	}
	
	public void pebblePassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		int x = rand.nextInt(16) + 8;
		int z = rand.nextInt(16) + 8;
		BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
		EventGenPebble event = new EventGenPebble(worldIn, at, rand, genPebble);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
		{
			return;
		}
		
		event.generator.generate(worldIn, rand, at);
	}
	
	public void boulderPassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		int x = rand.nextInt(16) + 8;
		int z = rand.nextInt(16) + 8;
		BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
		EventGenBoulder event = new EventGenBoulder(worldIn, at, rand, genBoulders);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(event))
		{
			return;
		}
		
		event.generator.generate(worldIn, rand, at);
	}
	
	public void tallgrassPassGenerate(World worldIn, Random rand, Biome biome, BlockPos pos)
	{
		int x = rand.nextInt(16) + 8;
		int z = rand.nextInt(16) + 8;
		BlockPos at = worldIn.getHeight(pos.add(x, 0, z));
		EventGenVegetation event = new EventGenVegetation(worldIn, at, rand, genVegetation, Type.TALLGRASS);
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
