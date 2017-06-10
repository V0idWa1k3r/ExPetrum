package v0id.exp.world.gen.tree;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import v0id.api.exp.world.gen.ITreeGenerator;

public class TreeGenImpl
{
	public static void leavesGenBirchImpl(Pair<ITreeGenerator, BlockPos> p, int height)
	{
		int len = 3 + p.getKey().genWorld().rand.nextInt(4);
		BlockPos start = p.getRight().up(height + 2);
		for (int i = 0; i < len + 2; ++i)
		{
			BlockPos at = start.down(i);
			leavesGenBirchImplLayer(p.getKey(), p.getKey().genWorld(), at, i, len);
		}
	}
	
	public static void leavesGenBirchImplLayer(ITreeGenerator gen, World w, BlockPos at, int layer, int len)
	{
		int alayer = layer < len ? layer : (len + 2) - layer;
		for (int dx = -1; dx <= 1; ++dx)
		{
			for (int dz = -1; dz <= 1; ++dz)
			{
				int dist = Math.abs(dx) + Math.abs(dz);
				if (dist <= alayer / 2)
				{
					BlockPos offset = at.add(dx, 0, dz);
					if (w.getBlockState(offset).getBlock().canBeReplacedByLeaves(w.getBlockState(offset), w, offset))
					{
						w.setBlockState(offset, gen.getLeaves(), 2);
					}
				}
			}
		}
	}
	
	public static void leavesGenAcaciaImpl(Pair<ITreeGenerator, BlockPos> p, int height)
	{
		int sx = 2 + p.getKey().genWorld().rand.nextInt(3);
		int sz = 2 + p.getKey().genWorld().rand.nextInt(3);
		BlockPos start = p.getRight().up(height + 1);
		for (int i = 0; i < 3; ++i)
		{
			BlockPos at = start.down(i);
			leavesGenAcaciaImplLayer(p.getKey(), p.getKey().genWorld(), at, i, sx, sz);
		}
	}
	
	public static void leavesGenAcaciaImplLayer(ITreeGenerator gen, World w, BlockPos at, int layer, int sx, int sz)
	{
		int alayer = (layer + 1) * 2;
		for (int dx = -sx; dx <= sx; ++dx)
		{
			for (int dz = -sz; dz <= sz; ++dz)
			{
				int dist = Math.abs(dx) + Math.abs(dz);
				if (dist <= alayer)
				{
					BlockPos offset = at.add(dx, 0, dz);
					if (w.getBlockState(offset).getBlock().canBeReplacedByLeaves(w.getBlockState(offset), w, offset))
					{
						w.setBlockState(offset, gen.getLeaves(), 2);
					}
				}
			}
		}
	}
	
	public static void leavesGenHickoryImpl(Pair<ITreeGenerator, BlockPos> p, int height)
	{
		BlockPos start = p.getRight().up(height + 2);
		for (int i = 0; i < 6 + p.getLeft().genWorld().rand.nextInt(3) && i < height - 1; ++i)
		{
			BlockPos at = start.down(i);
			leavesGenHickoryImplLayer(p.getKey(), p.getKey().genWorld(), at, i);
		}
	}
	
	public static void leavesGenHickoryImplLayer(ITreeGenerator gen, World w, BlockPos at, int layer)
	{
		int alayer = layer / 2;
		alayer = Math.min(2, alayer);
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
						w.setBlockState(offset, gen.getLeaves(), 2);
					}
				}
			}
		}
	}
	
	public static void leavesGenBaobabImpl(Pair<ITreeGenerator, BlockPos> p, int height)
	{
		BlockPos start = p.getRight().up(height + 1);
		for (int i = 0; i < 3; ++i)
		{
			BlockPos at = start.down(i);
			leavesGenBaobabImplLayer(p.getKey(), p.getKey().genWorld(), at, i);
		}
	}
	
	public static void leavesGenBaobabImplLayer(ITreeGenerator gen, World w, BlockPos at, int layer)
	{
		int alayer = layer == 0 ? 1 : layer == 1 ? 4 : layer == 2 ? 2 : -1;
		for (int dx = -4; dx <= 4; ++dx)
		{
			for (int dz = -4; dz <= 4; ++dz)
			{
				int dist = Math.abs(dx) + Math.abs(dz);
				if (dist <= alayer)
				{
					BlockPos offset = at.add(dx, 0, dz);
					if (w.getBlockState(offset).getBlock().canBeReplacedByLeaves(w.getBlockState(offset), w, offset))
					{
						w.setBlockState(offset, gen.getLeaves(), 2);
					}
				}
			}
		}
	}
	
	public static void leavesGenEucalyptusImpl(Pair<ITreeGenerator, BlockPos> p, int height)
	{
		BlockPos start = p.getRight().up(height + 2);
		for (int i = 0; i < 6 + p.getLeft().genWorld().rand.nextInt(3) && i < height - 1; ++i)
		{
			BlockPos at = start.down(i);
			leavesGenHickoryImplLayer(p.getKey(), p.getKey().genWorld(), at, i);
		}
	}
	
	public static void leavesGenWillowImpl(Pair<ITreeGenerator, BlockPos> p, int height)
	{
		int sx = 2 + p.getKey().genWorld().rand.nextInt(3);
		int sz = 2 + p.getKey().genWorld().rand.nextInt(3);
		BlockPos start = p.getRight().up(height + 1);
		for (int i = 0; i < 5; ++i)
		{
			BlockPos at = start.down(i);
			leavesGenWillowImplLayer(p.getKey(), p.getKey().genWorld(), at, i, sx, sz);
		}
	}
	
	public static void leavesGenWillowImplLayer(ITreeGenerator gen, World w, BlockPos at, int layer, int sx, int sz)
	{
		int alayer = (Math.min(layer, 2) + 1) * 2;
		for (int dx = -sx; dx <= sx; ++dx)
		{
			for (int dz = -sz; dz <= sz; ++dz)
			{
				int dist = Math.abs(dx) + Math.abs(dz);
				if (dist <= alayer)
				{
					BlockPos offset = at.add(dx, 0, dz);
					if (layer < 3)
					{
						if (w.getBlockState(offset).getBlock().canBeReplacedByLeaves(w.getBlockState(offset), w, offset))
						{
							w.setBlockState(offset, gen.getLeaves(), 2);
						}
					}
					else
					{
						if (layer == 3)
						{
							if (w.getBlockState(offset).getBlock().canBeReplacedByLeaves(w.getBlockState(offset), w, offset) && w.rand.nextDouble() < 0.3)
							{
								w.setBlockState(offset, gen.getLeaves(), 2);
							}
						}
						else
						{
							if (w.getBlockState(offset).getBlock().canBeReplacedByLeaves(w.getBlockState(offset), w, offset) && w.getBlockState(offset.up()) == gen.getLeaves())
							{
								w.setBlockState(offset, gen.getLeaves(), 2);
							}
						}
					}
				}
			}
		}
	}
	
	public static void leavesGenPalmImpl(Pair<ITreeGenerator, BlockPos> p, int height)
	{
		BlockPos start = p.getRight().up(height + 1);
		for (int i = 0; i < 3; ++i)
		{
			BlockPos at = start.down(i);
			leavesGenPalmImplLayer(p.getKey(), p.getLeft().genWorld(), at, i);
		}
		
		int w = 2 + p.getLeft().genWorld().rand.nextInt(3);
		for (int i = 2; i <= w; ++i)
		{
			for (EnumFacing face : EnumFacing.HORIZONTALS)
			{
				BlockPos offset = start.down().offset(face, i);
				p.getLeft().genWorld().setBlockState(offset, p.getLeft().getLeaves(), 2);
				if (i == w)
				{
					p.getLeft().genWorld().setBlockState(offset.down(), p.getLeft().getLeaves(), 2);
				}
			}
		}
	}
	
	public static void leavesGenPalmImplLayer(ITreeGenerator gen, World w, BlockPos at, int layer)
	{
		int alayer = layer == 0 ? 1 : layer == 1 ? 2 : 1;
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
						w.setBlockState(offset, gen.getLeaves(), 2);
					}
				}
			}
		}
	}
}
