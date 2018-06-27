package v0id.core.event.handler;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.core.VCLoggers;
import v0id.core.VCStatics;
import v0id.core.event.ExtraBlockTickEvent;
import v0id.core.logging.LogLevel;
import v0id.core.util.ChunkUtils;
import v0id.core.util.DimBlockPos;
import v0id.core.util.MC;

import java.util.List;

@EventBusSubscriber(modid = ExPRegistryNames.modid)
public class VCEventHandler 
{
	@SubscribeEvent
	public void onChunkLoaded(ChunkEvent.Load event)
	{
		if (MC.getSide() != Side.SERVER)
		{
			return;
		}
		
		try
		{
			int dim = event.getWorld().provider.getDimension();
			ChunkPos chunkPos = new ChunkPos(event.getChunk().x, event.getChunk().z);
			if (ChunkUtils.loadedChunks.containsEntry(dim, chunkPos))
			{
				VCLoggers.loggerWarnings.log(LogLevel.Warning, "Chunk %s already exists in a list of loaded chunks but the load event for it got caught! Is the chunk loading twice without unloading first?", event.getChunk().toString());
			}
			else
			{
				ChunkUtils.loadedChunks.put(dim, chunkPos);
				VCLoggers.loggerDebug.log(LogLevel.Debug, "Added chunk %s to the list of loaded chunks.", event.getChunk().toString());
			}
		}
		catch (Exception ex)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Caught an exception when trying to handle ChunkLoadEvent for chunk %s!", ex, event.getChunk().toString());
		}
	}
	
	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload event)
	{
		if (MC.getSide() != Side.SERVER)
		{
			return;
		}
		
		try
		{
			int dim = event.getWorld().provider.getDimension();
			ChunkPos chunkPos = new ChunkPos(event.getChunk().x, event.getChunk().z);
			if (!ChunkUtils.loadedChunks.containsEntry(dim, chunkPos))
			{
				VCLoggers.loggerWarnings.log(LogLevel.Warning, "Chunk %s is being unloaded but the load event for it was never caught!", event.getChunk().toString());
			}
			else
			{
				ChunkUtils.loadedChunks.remove(dim, chunkPos);
				VCLoggers.loggerDebug.log(LogLevel.Debug, "Removed chunk %s from the list of loaded chunks.", event.getChunk().toString());
			}
		}
		catch (Exception ex)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Caught an exception when trying to handle ChunkUnloadEvent for chunk %s!", ex, event.getChunk().toString());
		}
	}
	
	@SubscribeEvent
	public void onWorldServerTick(TickEvent.WorldTickEvent event)
	{
		if (MC.getSide() != Side.SERVER)
		{
			return;
		}
		
		try
		{
			int dim = event.world.provider.getDimension();
			if (ChunkUtils.loadedChunks.containsKey(dim))
			{
				List<ChunkPos> loadedChunks = ChunkUtils.loadedChunks.get(dim);
				ChunkPos cp = loadedChunks.get(VCStatics.rng.nextInt(loadedChunks.size()));
				DimBlockPos pos = new DimBlockPos(new BlockPos(cp.x >> 4, VCStatics.rng.nextInt(event.world.getHeight()), cp.z >> 4), dim);
				MinecraftForge.EVENT_BUS.post(new ExtraBlockTickEvent(pos));
			}
			else
			{
				VCLoggers.loggerWarnings.log(LogLevel.Warning, "World for dimension %d ticks but contains no loaded chunks?", dim);
			}
		}
		catch (Exception ex)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Caught an exception when trying to handle WorldTickEvent!", ex);
		}
	}
}
