package v0id.exp.world.gen.biome;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.apache.commons.lang3.tuple.Triple;
import v0id.core.logging.LogLevel;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.block.IOreHintReplaceable;
import v0id.api.exp.block.property.EnumRockClass;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPMisc;
import v0id.exp.tile.TileOre;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class OreGenerator extends WorldGenerator
{
	public final EnumOre toGenerate;
	
	public OreGenerator(Random rand)
	{
		this.toGenerate = WeightedRandom.getRandomItem(EnumOre.worldgenEntries, rand.nextInt(WeightedRandom.getTotalWeight(EnumOre.worldgenEntries))).getOreType();
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		ExPMisc.modLogger.log(LogLevel.Debug, "Generating ore %s at %s! Could possibly cause chunk runaway effects!", this.toGenerate.getName(), position.toString());
		Chunk at = worldIn.getChunkFromBlockCoords(position);
		boolean detectedRunaway = false;
		int veinSize = 64 + rand.nextInt(512);
		Set<Triple<Integer, Integer, EnumRockClass>> generatedAtSet = Sets.newHashSet();
		int yLevel = position.getY();
		while ((yLevel - 5) * 12 * 12 < veinSize)
		{
			++yLevel;
		}
		
		int tries = 0;
		int generatedResets = 0;
		byte oreRichness = createRichnessIndex(rand);
		byte meta = (byte) rand.nextInt(16);
		List<BlockPos> generatedAt = Lists.newArrayListWithExpectedSize(veinSize);
		while (veinSize > 0)
		{
			if (++tries > 4096)
			{
				ExPMisc.modLogger.log(LogLevel.Error, "Could not properly generate ore at %s! Detected possible recursion!", position);
				break;
			}
			
			if (rand.nextInt(512) < generatedResets)
			{
				generatedResets = 0;
				if (--yLevel <= 5)
				{
					ExPMisc.modLogger.log(LogLevel.Error, "Could not properly generate ore at %s! Ore generator hit bedrock level before generating all ore!", position);
					break;
				}
				
				continue;
			}
			
			Vec3d orePtr = new Vec3d(rand.nextDouble() - rand.nextDouble(), 0, rand.nextDouble() - rand.nextDouble());
			orePtr = orePtr.scale(rand.nextDouble() * 8);
			BlockPos orePos = new BlockPos(position.getX() + orePtr.x, yLevel, position.getZ() + orePtr.z);
			if (generatedAt.contains(orePos))
			{
				generatedResets += 3;
				continue;
			}
			
			if (worldIn.getChunkFromBlockCoords(orePos) != at && !detectedRunaway)
			{
				detectedRunaway = true;
				ExPMisc.modLogger.log(LogLevel.Warning, "Detected chunk runaway while generating ore at %s!", position);
			}
			
			IBlockState stateCurrent = worldIn.getBlockState(orePos);
			if (stateCurrent.getBlock().isReplaceableOreGen(stateCurrent, worldIn, orePos, state -> state.getBlock().isAssociatedBlock(Blocks.STONE)))
			{
				if (stateCurrent.getPropertyKeys().contains(ExPBlockProperties.ROCK_CLASS))
				{
					EnumRockClass erc = stateCurrent.getValue(ExPBlockProperties.ROCK_CLASS);
					generatedAt.add(orePos);
					generatedAtSet.add(Triple.of(orePos.getX(), orePos.getZ(), erc));
					worldIn.setBlockState(orePos, ExPBlocks.ore.getDefaultState().withProperty(ExPBlockProperties.ROCK_CLASS, erc), 2);
					TileEntity tile = worldIn.getTileEntity(orePos);
					if (tile instanceof TileOre)
					{
						TileOre ore = (TileOre) tile;
						ore.amount = oreRichness;
						ore.subtype = meta;
						ore.type = this.toGenerate;
						worldIn.notifyBlockUpdate(orePos, stateCurrent, stateCurrent, 2);
					}
					
					generatedResets += 1;
					--veinSize;
				}
				else
				{
					ExPMisc.modLogger.log(LogLevel.Debug, "Encountered an unknown stone block at %s!", orePos);
					generatedResets += 2;
				}
			}
			else
			{
				generatedResets += 2;
			}
		}
		
		generatedAtSet.forEach(p -> {
			if (rand.nextDouble() > 0.2)
			{
				return;
			}
			
			BlockPos hintAt = worldIn.getHeight(new BlockPos(p.getLeft(), 0, p.getMiddle())).up(6);
			while ((worldIn.isAirBlock(hintAt) || !worldIn.getBlockState(hintAt).isOpaqueCube()) && hintAt.getY() > 0)
			{
				hintAt = hintAt.down();
			}
			
			IBlockState preventing = worldIn.getBlockState(hintAt.up());
			if (preventing.getBlock().isReplaceable(worldIn, hintAt) || preventing.getBlock() instanceof IOreHintReplaceable)
			{
				hintAt = hintAt.up();
				IBlockState current = ExPBlocks.boulderOre.getDefaultState().withProperty(ExPBlockProperties.ROCK_CLASS, p.getRight());
				worldIn.setBlockState(hintAt, current, 2);
				TileEntity tile = worldIn.getTileEntity(hintAt);
				if (tile instanceof TileOre)
				{
					TileOre ore = (TileOre) tile;
					ore.amount = 10;
					ore.subtype = meta;
					ore.type = this.toGenerate;
					worldIn.notifyBlockUpdate(hintAt, current, current, 2);
				}
			}
		});
		
		return false;
	}

	public byte createRichnessIndex(Random rand)
	{
		if (rand.nextBoolean())
		{
			return (byte) (30 + rand.nextInt(10) - rand.nextInt(10));
		}
		else
		{
			if (rand.nextBoolean())
			{
				return (byte) (20 + rand.nextInt(10) - rand.nextInt(10));
			}
			else
			{
				return (byte) (50 + rand.nextInt(25) - rand.nextInt(25));
			}
		}
	}
}
