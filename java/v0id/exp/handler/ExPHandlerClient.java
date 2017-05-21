package v0id.exp.handler;

import java.lang.reflect.Field;

import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import v0id.api.core.VoidApi;
import v0id.api.core.logging.LogLevel;
import v0id.api.exp.data.ExPMisc;
import v0id.exp.client.render.hud.PlayerHUDRenderer;
import v0id.exp.client.render.sky.WorldSkyRenderer;
import v0id.exp.client.render.sky.WorldWeatherRenderer;
import v0id.exp.util.WeatherUtils;

public class ExPHandlerClient
{
	@SubscribeEvent
	public void onInitGui(GuiScreenEvent.InitGuiEvent event)
	{
		if (event.getGui() instanceof GuiCreateWorld)
		{
			try
			{
				Field f = null;
				Field[] allFlds = GuiCreateWorld.class.getDeclaredFields();
				for (int i = allFlds.length - 1; i >= 0; --i)
				{
					if (allFlds[i].getType() == Integer.TYPE)
					{
						f = allFlds[i];
						break;
					}
				}
				
				f.setAccessible(true);
				f.set(event.getGui(), ExPMisc.worldTypeExP.getWorldTypeID());
			}
			catch (Exception ex)
			{
				ExPMisc.modLogger.log(LogLevel.Debug, "Could not reflect GuiCreateWorld!", ex);
			}
		}
	}
	
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
		if (VoidApi.proxy.getClientWorld() != null && VoidApi.proxy.getClientWorld().provider != null && VoidApi.proxy.getClientWorld().provider.getDimension() == 0)
		{
			if (!(VoidApi.proxy.getClientWorld().provider.getSkyRenderer() instanceof WorldSkyRenderer))
			{
				VoidApi.proxy.getClientWorld().provider.setSkyRenderer(WorldSkyRenderer.getInstance());
				VoidApi.proxy.getClientWorld().provider.setWeatherRenderer(WorldWeatherRenderer.getInstance());
			}
			
			if (VoidApi.proxy.getClientWorld().isRaining())
			{
				WeatherUtils.handleClientTick(VoidApi.proxy.getClientPlayer());
			}
		}
	}
	
	@SubscribeEvent
	public void onFogRender(EntityViewRenderEvent.FogDensity event)
	{
		event.setDensity(0);
		event.setCanceled(true);
	}
}
