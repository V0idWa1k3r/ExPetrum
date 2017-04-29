package v0id.exp.handler;

import net.minecraftforge.event.terraingen.WorldTypeEvent.BiomeSize;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExPHandlerWorldGen
{
	@SubscribeEvent
	public void onBiomeSize(BiomeSize event)
	{
		event.setNewSize(6);
	}
}
