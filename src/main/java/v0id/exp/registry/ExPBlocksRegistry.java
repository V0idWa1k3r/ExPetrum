package v0id.exp.registry;

import v0id.api.exp.block.EnumShrubState;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPBlocks;
import v0id.exp.block.*;
import v0id.exp.block.fluid.BlockFreshWater;
import v0id.exp.block.fluid.BlockLava;
import v0id.exp.block.fluid.BlockOil;
import v0id.exp.block.fluid.BlockSaltWater;
import v0id.exp.block.plant.*;
import v0id.exp.block.tree.BlockLeaf;
import v0id.exp.block.tree.BlockLog;

import java.util.stream.Stream;

public class ExPBlocksRegistry extends AbstractRegistry
{
	static
	{
		ExPBlocks.rock = new BlockStone();
		ExPBlocks.soil = new BlockSoil();
		ExPBlocks.grass = new BlockGrass();
		ExPBlocks.grass_dry = new BlockGrass.Dry();
		ExPBlocks.grass_dead = new BlockGrass.Dead();
		ExPBlocks.saltWater = new BlockSaltWater();
		ExPBlocks.freshWater = new BlockFreshWater();
		ExPBlocks.lava = new BlockLava();
		ExPBlocks.waterLily = new BlockWaterLily();
		ExPBlocks.cattail = new BlockCattail();
		ExPBlocks.vegetation = new BlockVegetation();
		ExPBlocks.sand = new BlockSand();
		ExPBlocks.seaweed = new BlockSeaweed();
		ExPBlocks.coralRock = new BlockCoralRock();
		ExPBlocks.coralPlant = new BlockCoralPlant();
		ExPBlocks.logs = new BlockLog[9];
		ExPBlocks.logsDeco = new BlockLog[9];
		ExPBlocks.leaves = new BlockLeaf[9];
		for (int i = 0; i < 9; ++i)
		{
			for (int j = 0; j < 5; ++j)
			{
				EnumTreeType.typesForIndex[i][j] = EnumTreeType.values()[i * 5 + j];
			}
		}
		
		for (int i = 0; i < 9; ++i)
		{
			ExPBlocks.logs[i] = new BlockLog(i);
			ExPBlocks.logsDeco[i] = new BlockLog.Decorative(i);
			ExPBlocks.leaves[i] = new BlockLeaf(i);
		}
		
		ExPBlocks.ore = new BlockOre();
		ExPBlocks.pebble = new BlockPebble();
		ExPBlocks.boulder = new BlockBoulder();
		ExPBlocks.boulderOre = new BlockBoulderOre();
		ExPBlocks.oil = new BlockOil();
		ExPBlocks.shrubs = new BlockShrub[EnumShrubState.values().length];
		ExPBlocks.berryBushes = new BlockBerryBush[EnumShrubState.values().length];
		Stream.of(EnumShrubState.values()).forEach(s -> { 
			ExPBlocks.shrubs[s.ordinal()] = new BlockShrub(s); ExPBlocks.berryBushes[s.ordinal()] = new BlockBerryBush(s); 
		});
		
		ExPBlocks.snow = new BlockSnow();
		ExPBlocks.ice = new BlockIce();
		ExPBlocks.workedBoulder = new BlockWorkedBoulder();
		ExPBlocks.crop = new BlockCrop();
		ExPBlocks.farmland = new BlockFarmland();
	}
	
	public ExPBlocksRegistry()
	{
		super();
	}
}
