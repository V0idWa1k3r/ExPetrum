package v0id.exp.handler;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import v0id.api.core.VoidApi;
import v0id.exp.client.render.sky.WorldSkyRenderer;

public class ExPHandlerClient
{
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (VoidApi.proxy.getClientWorld() != null && VoidApi.proxy.getClientWorld().provider != null && VoidApi.proxy.getClientWorld().provider.getDimension() == 0 && !(VoidApi.proxy.getClientWorld().provider.getSkyRenderer() instanceof WorldSkyRenderer))
		{
			VoidApi.proxy.getClientWorld().provider.setSkyRenderer(WorldSkyRenderer.getInstance());
		}
	}
	
	@SubscribeEvent
	public void onFogRender(EntityViewRenderEvent.FogDensity event)
	{
		event.setDensity(0);
		event.setCanceled(true);
	}
}
