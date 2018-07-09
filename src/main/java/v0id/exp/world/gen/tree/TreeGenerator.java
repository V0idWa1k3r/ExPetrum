package v0id.exp.world.gen.tree;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumLeafState;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.world.EnumSeason;
import v0id.api.exp.world.IExPWorld;
import v0id.api.exp.world.gen.ITreeGenerator;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TreeGenerator extends WorldGenerator implements ITreeGenerator
{
	public final EnumTreeType treeType;
	public World worldGen;
	public IBlockState wood;
	public IBlockState leaves;
	public Function<Pair<ITreeGenerator, BlockPos>, Integer> trunkGenerator;
	public int defaultImplMinHeight;
	public int defaultImplMaxHeight;
	public BiConsumer<Pair<ITreeGenerator, BlockPos>, Integer> leavesGenerator;
	
	public TreeGenerator(EnumTreeType of)
	{
		this.treeType = of;
	}
	
	public IBlockState makeWood()
	{
		return ExPBlocks.logs[this.treeType.ordinal() / 5].getDefaultState().withProperty(ExPBlockProperties.TREE_TYPE, this.treeType);
	}
	
	public IBlockState makeLeaves()
	{
		IExPWorld w = IExPWorld.of(this.worldGen);
		EnumLeafState leafState = this.treeType.isEvergreen() ? EnumLeafState.NORMAL : w.getCurrentSeason() == EnumSeason.WINTER ? EnumLeafState.DEAD : w.getCurrentSeason() == EnumSeason.AUTUMN ? EnumLeafState.AUTUMN : EnumLeafState.NORMAL;
		return ExPBlocks.leaves[this.treeType.ordinal() / 5].getDefaultState().withProperty(ExPBlockProperties.TREE_TYPE, this.treeType).withProperty(ExPBlockProperties.LEAF_STATE, leafState);
	}
	
	@SuppressWarnings("UnusedReturnValue")
	public TreeGenerator updateState()
	{
		this.wood = this.makeWood();
		this.leaves = this.makeLeaves();
		return this;
	}
	
	public TreeGenerator defaultTrunk(int min, int max)
	{
		this.defaultImplMaxHeight = max;
		this.defaultImplMinHeight = min;
		this.trunkGenerator = this::trunkGenDefaultImpl;
		return this;
	}
	
	public TreeGenerator defaultLeaves()
	{
		this.leavesGenerator = this::leavesGenDefaultImpl;
		return this;
	}
	
	public TreeGenerator evergreenLeaves()
	{
		this.leavesGenerator = this::leavesGenEvergreenImpl;
		return this;
	}
	
	public TreeGenerator sphereLeaves()
	{
		this.leavesGenerator = this::leavesGenSphereImpl;
		return this;
	}
	
	@Override
	public ITreeGenerator withLeaves(BiConsumer<Pair<ITreeGenerator, BlockPos>, Integer> leavesHandler)
	{
		this.leavesGenerator = leavesHandler;
		return this;
	}
	
	@Override
	public ITreeGenerator withTrunk(Function<Pair<ITreeGenerator, BlockPos>, Integer> trunkHandler)
	{
		this.trunkGenerator = trunkHandler;
		return this;
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		assert this.trunkGenerator != null && this.leavesGenerator != null : "TreeGenerator was not initialized properly but was assigned to world generation! This is a severe issue!";
		this.worldGen = worldIn;
		this.updateState();
		BlockPos at = position;
		while (worldIn.isAirBlock(at))
		{
			at = at.down();
		}
		
		if (!worldIn.getBlockState(at).getBlock().isAssociatedBlock(Blocks.GRASS) && !worldIn.getBlockState(at).getBlock().isAssociatedBlock(Blocks.DIRT))
		{
			ExPMisc.modLogger.debug("Tried to generate tree at %s but there is no appropriate block for the tree!", position.toString());
			return false;
		}
		
		int trunkResult = this.trunkGenerator.apply(Pair.of(this, at.up()));
		if (trunkResult == -1)
		{
			return false;
		}
		
		this.leavesGenerator.accept(Pair.of(this, at.up()), trunkResult);
		return true;
	}
	
	public int trunkGenDefaultImpl(Pair<ITreeGenerator, BlockPos> p)
	{
		BlockPos initialPos = p.getRight();
		TreeGenerator gen = (TreeGenerator)p.getKey();
		int heightToGenerate = gen.defaultImplMinHeight + gen.worldGen.rand.nextInt(gen.defaultImplMaxHeight - gen.defaultImplMinHeight);
		int h = heightToGenerate + 1;
		while (h-- > 0)
		{
			BlockPos pos = initialPos.up(h);
			gen.worldGen.setBlockState(pos, this.wood, 2);
		}
		
		return heightToGenerate;
	}

	public void leavesGenDefaultImpl(Pair<ITreeGenerator, BlockPos> p, int height)
	{
		BlockPos start = p.getRight().up(height + 1);
		for (int i = 0; i < 5; ++i)
		{
			BlockPos at = start.down(i);
			leavesGenDefaultImplLayer(p.getKey().genWorld(), at, i);
		}
	}
	
	public void leavesGenEvergreenImpl(Pair<ITreeGenerator, BlockPos> p, int height)
	{
		BlockPos start = p.getRight().up(height + 1);
		for (int i = 0; i < 9; ++i)
		{
			BlockPos at = start.down(i);
			leavesGenEvergreenImplLayer(p.getKey().genWorld(), at, i);
		}
	}
	
	public void leavesGenSphereImpl(Pair<ITreeGenerator, BlockPos> p, int height)
	{
		BlockPos start = p.getRight().up(height + 1);
		for (int i = 0; i < 6; ++i)
		{
			BlockPos at = start.down(i);
			leavesGenSphereImplLayer(p.getKey().genWorld(), at, i);
		}
	}
	
	public void leavesGenDefaultImplLayer(World w, BlockPos at, int layer)
	{
		for (int dx = -2; dx <= 2; ++dx)
		{
			for (int dz = -2; dz <= 2; ++dz)
			{
				int dist = Math.abs(dx) + Math.abs(dz);
				if (dist <= layer)
				{
					BlockPos offset = at.add(dx, 0, dz);
					if (w.getBlockState(offset).getBlock().canBeReplacedByLeaves(w.getBlockState(offset), w, offset))
					{
						w.setBlockState(offset, this.leaves, 2);
					}
				}
			}
		}
	}
	
	public void leavesGenEvergreenImplLayer(World w, BlockPos at, int layer)
	{
		if (layer == 2 || layer == 5 || layer >= 9)
		{
			return;
		}
		
		int alayer = layer < 2 ? layer : layer < 5 ? layer - 2 : layer - 5;
		for (int dx = -3; dx <= 3; ++dx)
		{
			for (int dz = -3; dz <= 3; ++dz)
			{
				int dist = Math.abs(dx) + Math.abs(dz);
				if (dist <= alayer)
				{
					BlockPos offset = at.add(dx, 0, dz);
					if (w.getBlockState(offset).getBlock().canBeReplacedByLeaves(w.getBlockState(offset), w, offset))
					{
						w.setBlockState(offset, this.leaves, 2);
					}
				}
			}
		}
	}
	
	public void leavesGenSphereImplLayer(World w, BlockPos at, int layer)
	{
		if (layer == 0)
		{
			for (int dx = -1; dx <= 1; ++dx)
			{
				for (int dz = -1; dz <= 1; ++dz)
				{
					BlockPos offset = at.add(dx, 0, dz);
					if (w.getBlockState(offset).getBlock().canBeReplacedByLeaves(w.getBlockState(offset), w, offset))
					{
						w.setBlockState(offset, this.leaves, 2);
					}
				}
			}
			
			return;
		}
		
		int alayer = layer < 4 ? 3 : 6 - layer;
		for (int dx = -2; dx <= 2; ++dx)
		{
			for (int dz = -2; dz <= 2; ++dz)
			{
				int dist = Math.abs(dx) + Math.abs(dz);
				if (dist <= alayer)
				{
					BlockPos offset = at.add(dx, 0, dz);
					if (w.getBlockState(offset).getBlock().canBeReplacedByLeaves(w.getBlockState(offset), w, offset))
					{
						w.setBlockState(offset, this.leaves, 2);
					}
				}
			}
		}
	}

	@Override
	public World genWorld()
	{
		return this.worldGen;
	}

	@Override
	public IBlockState getLeaves()
	{
		return this.leaves;
	}

	@Override
	public IBlockState getWood()
	{
		return this.wood;
	}
}
