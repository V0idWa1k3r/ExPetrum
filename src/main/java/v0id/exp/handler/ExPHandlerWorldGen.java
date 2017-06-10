package v0id.exp.handler;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType;
import net.minecraftforge.event.terraingen.WorldTypeEvent.BiomeSize;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import v0id.api.exp.data.ExPMisc;

public class ExPHandlerWorldGen
{
	@SubscribeEvent
	public void onBiomeSize(BiomeSize event)
	{
		event.setNewSize(6);
	}
	
	@SubscribeEvent
	public void onPopulate(PopulateChunkEvent.Populate event)
	{
		if (event.getWorld().getWorldType() != ExPMisc.worldTypeExP)
		{
			return;
		}
		
		if (event.getType() == EventType.LAKE || event.getType() == EventType.LAVA)
		{
			event.setResult(Result.DENY);
		}
	}
}
