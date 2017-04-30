package v0id.exp.handler;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import v0id.api.core.VoidApi;
import v0id.exp.client.render.hud.PlayerHUDRenderer;
import v0id.exp.client.render.sky.WorldSkyRenderer;
import v0id.exp.client.render.sky.WorldWeatherRenderer;

public class ExPHandlerClient
{
	@SubscribeEvent
	public void onHudRender(RenderGameOverlayEvent.Pre event)
	{
		if (event.getType() == ElementType.ALL)
		{
			PlayerHUDRenderer.render(event.getPartialTicks());
		}
		
		if (	event.getType() == ElementType.HOTBAR || 
				event.getType() == ElementType.FOOD || 
				event.getType() == ElementType.HEALTH ||
				event.getType() == ElementType.EXPERIENCE)
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (VoidApi.proxy.getClientWorld() != null && VoidApi.proxy.getClientWorld().provider != null && VoidApi.proxy.getClientWorld().provider.getDimension() == 0 && !(VoidApi.proxy.getClientWorld().provider.getSkyRenderer() instanceof WorldSkyRenderer))
		{
			VoidApi.proxy.getClientWorld().provider.setSkyRenderer(WorldSkyRenderer.getInstance());
			VoidApi.proxy.getClientWorld().provider.setWeatherRenderer(WorldWeatherRenderer.getInstance());
		}
	}
	
	@SubscribeEvent
	public void onFogRender(EntityViewRenderEvent.FogDensity event)
	{
		event.setDensity(0);
		event.setCanceled(true);
	}
}
