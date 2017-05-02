package v0id.exp.world.biome;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.block.property.EnumDirtClass;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.world.EnumSeason;
import v0id.api.exp.world.IBiome;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.world.biome.impl.ExPOcean;
import v0id.exp.world.biome.impl.ExPRiver;
import v0id.exp.world.gen.GenerationHelper;
import v0id.exp.world.gen.tree.TreeEntry;

public class ExPBiome extends Biome implements IBiome
{
	public final float
		temperatureMultiplier,
		humidityMultiplier,
		temperatureBaseModifier,
		humidityBaseModifier;
	
	public final IBlockState 
		SALT_WATER,
		FRESH_WATER,
		LAVA;
	
	public final List<TreeEntry> treesToGenerate = Lists.newArrayList();
	
	public ExPBiome(BiomeProperties properties, float... biomedata)
	{
		super(properties);
		this.temperatureMultiplier = biomedata[0];
		this.humidityMultiplier = biomedata[1];
		this.temperatureBaseModifier = biomedata[2];
		this.humidityBaseModifier = biomedata[3];
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.SALT_WATER = ExPBlocks.saltWater.getStateFromMeta(9);
		this.FRESH_WATER = ExPBlocks.freshWater.getStateFromMeta(9);
		this.LAVA = ExPBlocks.lava.getStateFromMeta(9);
		this.setRegistryName("exp", this.getBiomeName());
		GameRegistry.register(this);
	}
	
	public Optional<EnumTreeType> provideTreeToGenerate(Random rand)
	{
		if (this.treesToGenerate.isEmpty())
		{
			return Optional.empty();
		}
		
		int weight = rand.nextInt(WeightedRandom.getTotalWeight(this.treesToGenerate));
		return Optional.of(WeightedRandom.getRandomItem(this.treesToGenerate, weight).treeType);
	}
	
	@Override
	public boolean isWaterSalt()
	{
		return false;
	}
	
	@Override
	public boolean canRain()
    {
		return false;
    }
	
	@Override
	public BiomeDecorator createBiomeDecorator()
	{
		return new ExPBiomeDecorator();
	}

	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
		int i = worldIn.getSeaLevel();
		int pz = z & 15;
		int px = x & 15;
		IBlockState stoneBlock = ExPBlocks.rock.getDefaultState().withProperty(ExPBlockProperties.ROCK_CLASS, GenerationHelper.getStoneTypeAt(worldIn, new BlockPos(x, 0, z)));
		IBlockState dirtBlock = ExPBlocks.soil.getDefaultState().withProperty(ExPBlockProperties.DIRT_CLASS, GenerationHelper.getDirtTypeAt(worldIn, new BlockPos(x, 0, z)));
		IBlockState sandBlock = ExPBlocks.sand.getDefaultState().withProperty(ExPBlockProperties.ROCK_CLASS, GenerationHelper.getStoneTypeAt(worldIn, new BlockPos(x, 0, z)));
		int translationIndex = 0;
		while (dirtBlock.getValue(ExPBlockProperties.DIRT_CLASS) == EnumDirtClass.PEAT && (this.humidityMultiplier <= 1 || this instanceof ExPRiver || this instanceof ExPOcean))
		{
			dirtBlock = ExPBlocks.soil.getDefaultState().withProperty(ExPBlockProperties.DIRT_CLASS, GenerationHelper.getDirtTypeAt(worldIn, new BlockPos(x + (translationIndex++ * 10), 0, z)));
		}
		
		EnumSeason season = IExPWorld.of(worldIn).getCurrentSeason();
		Block b = season == EnumSeason.WINTER ? ExPBlocks.grass_dead : season == EnumSeason.AUTUMN ? ExPBlocks.grass_dry : ExPBlocks.grass;
		IBlockState grassBlock = b.getDefaultState().withProperty(ExPBlockProperties.DIRT_CLASS, dirtBlock.getValue(ExPBlockProperties.DIRT_CLASS));
		int airNoise = -1;
		int fillerNoise = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		IBlockState grassSetter = 
				this.topBlock.getBlock().isAssociatedBlock(Blocks.GRASS) ? grassBlock : 
				this.topBlock.getBlock().isAssociatedBlock(Blocks.SAND) ? sandBlock : 
					this.topBlock;
		IBlockState dirtSetter = 
				this.fillerBlock.getBlock().isAssociatedBlock(Blocks.DIRT) ? dirtBlock : 
				this.fillerBlock.getBlock().isAssociatedBlock(Blocks.SAND) ? sandBlock : 
					this.fillerBlock;
		float temp = IExPWorld.of(worldIn).getOverhaulTemperature();
		temp *= this.temperatureMultiplier;
		temp += this.temperatureBaseModifier;
		
		for (int y = worldIn.getActualHeight() - 1; y >= 0; --y)
		{
			if (y <= 1)
			{
				chunkPrimerIn.setBlockState(px, y, pz, BEDROCK);
				continue;
			}
			
			IBlockState current = chunkPrimerIn.getBlockState(px, y, pz);
			if (current.getMaterial() == Material.AIR)
			{
				airNoise = -1;
			}
			else
			{
				if (current.getBlock().isAssociatedBlock(Blocks.STONE))
				{
					// The block above us is air
					if (airNoise == -1)
					{
						// Generate random 'scorches' on the terrain where there are no grass or soil blocks.
						if (fillerNoise <= 0)
                        {
							grassSetter = AIR;
							dirtSetter = stoneBlock;
                        }
						else
						{
							// We are ~3 blocks below sea level. Set blocks to dirt
							if (y >= i - 4 && y <= i + 1)
	                        {
								grassSetter = grassBlock;
								dirtSetter = dirtBlock;
	                        }
						}
						
						// We are below sea level, the top block was air and we do not have a top filler block(we are ocean)
						if (y < i && (grassSetter == null || grassSetter.getMaterial() == Material.AIR))
                        {
							// Cold
							if (temp <= 0)
							{
								// TODO implement ice blocks
								grassSetter = ICE;
							}
							else
							{
								grassSetter = this.isWaterSalt() ? this.SALT_WATER : this.FRESH_WATER;
							}
                        }
						
						airNoise = fillerNoise;
						
						// We are the first block below air and we are at sea level or greater
						if (y >= i - 1)
                        {
                            chunkPrimerIn.setBlockState(px, y, pz, grassSetter);
                        }
						else
						{
							// We are 7 or more blocks below sea level and we are the first non air block (ocean bottom)
							if (y < i - 7 - fillerNoise)
							{
								grassSetter = AIR;
								dirtSetter = stoneBlock;
	                            chunkPrimerIn.setBlockState(px, y, pz, ExPBlocks.sand.getDefaultState().withProperty(ExPBlockProperties.ROCK_CLASS, stoneBlock.getValue(ExPBlockProperties.ROCK_CLASS)));
							}
							else
							{
								// we are inbetween 7 blocks below sea level and sea level
								chunkPrimerIn.setBlockState(px, y, pz, dirtSetter);
							}
						}
					}
					else
					{
						// there is a block above us
						
						// we are setting dirt
						if (airNoise > 0)
						{
							--airNoise;
							chunkPrimerIn.setBlockState(px, y, pz, dirtSetter);
						}
						else
						{
							// replacing stone with our stone
							chunkPrimerIn.setBlockState(px, y, pz, stoneBlock);
						}
					}
				}
				else
				{
					if (current.getBlock() == Blocks.WATER || current.getBlock() == Blocks.FLOWING_WATER)
					{
						chunkPrimerIn.setBlockState(px, y, pz, this.isWaterSalt() ? this.SALT_WATER : this.FRESH_WATER);
					}
					
					if (current.getBlock() == Blocks.LAVA || current.getBlock() == Blocks.FLOWING_LAVA)
					{
						chunkPrimerIn.setBlockState(px, y, pz, this.LAVA);
					}
				}
			}
		}
    }

	@Override
	public float getTemperatureMultiplier()
	{
		return this.temperatureMultiplier;
	}

	@Override
	public float getHumidityMultiplier()
	{
		return this.humidityMultiplier;
	}

	@Override
	public float getTemperatureBaseModifier()
	{
		return this.temperatureBaseModifier;
	}

	@Override
	public float getHumidityBaseModifier()
	{
		return this.humidityBaseModifier;
	}
	
	

}
