package v0id.exp.util;

import java.util.Iterator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import v0id.api.exp.block.IWater;
import v0id.api.exp.data.ExPBlocks;
import v0id.exp.block.BlockSnow;

public class WeatherUtils
{
	public static void handleServerTick(WorldServer w)
	{
		Iterator<Chunk> chunks = w.getPersistentChunkIterable(w.getPlayerChunkMap().getChunkIterator());
		while (chunks.hasNext())
		{
			Chunk c = chunks.next();
			ChunkPos pos = c.getPos();
			BlockPos randomPos = pos.getBlock(w.rand.nextInt(16), 256, w.rand.nextInt(16));
			BlockPos heightPos = w.getPrecipitationHeight(randomPos);
			if (heightPos.getY() >= w.getActualHeight())
			{
				return;
			}
			
			boolean snows = Helpers.getTemperatureAt(w, heightPos) <= 0;
			if (snows)
			{
				if (w.rand.nextInt(16) == 0)
				{
					IBlockState state = w.getBlockState(heightPos.down());
					IBlockState state1 = w.getBlockState(heightPos);
					if (state.getBlock() instanceof BlockSnow)
					{
						int lvl = state.getValue(net.minecraft.block.BlockSnow.LAYERS);
						if (lvl < 8)
						{
							w.setBlockState(heightPos.down(), state.cycleProperty(net.minecraft.block.BlockSnow.LAYERS), 2);
						}
						else
						{
							w.setBlockState(heightPos, ExPBlocks.snow.getDefaultState(), 2);
						}
					}
					else
					{
						if (state1.getBlock() instanceof BlockSnow)
						{
							int lvl = state1.getValue(net.minecraft.block.BlockSnow.LAYERS);
							if (lvl < 8)
							{
								w.setBlockState(heightPos, state1.cycleProperty(net.minecraft.block.BlockSnow.LAYERS), 2);
							}
						}
						else
						{
							if (state.isSideSolid(w, heightPos.down(), EnumFacing.UP) || state.isFullBlock())
							{
								w.setBlockState(heightPos, ExPBlocks.snow.getDefaultState(), 2);
							}
						}
					}
				}
			}
			else
			{
				if (w.rand.nextInt(512) == 0)
				{
					IBlockState state = w.getBlockState(heightPos.down());
					if (state.getBlock() instanceof IWater)
					{
						return;
					}
					
					w.setBlockState(heightPos, ExPBlocks.freshWater.getDefaultState(), 2);
				}
			}
		}
		
	}
	
	public static void handleClientTick(EntityPlayer clientPlayer)
	{
		
	}
}
