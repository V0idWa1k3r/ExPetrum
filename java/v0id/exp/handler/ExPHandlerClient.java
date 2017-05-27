package v0id.exp.handler;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import v0id.api.core.VoidApi;
import v0id.api.core.logging.LogLevel;
import v0id.api.exp.data.ExPMisc;
import v0id.exp.client.render.gui.PlayerInventoryRenderer;
import v0id.exp.client.render.hud.PlayerHUDRenderer;
import v0id.exp.client.render.hud.SpecialAttackRenderer;
import v0id.exp.client.render.sky.WorldSkyRenderer;
import v0id.exp.client.render.sky.WorldWeatherRenderer;
import v0id.exp.combat.ClientCombatHandler;
import v0id.exp.player.inventory.ManagedSlot;
import v0id.exp.util.WeatherUtils;

public class ExPHandlerClient
{
	@SubscribeEvent
	public void onRenderWorldLast(RenderHandEvent event)
	{
		if (SpecialAttackRenderer.render(event.getPartialTicks()))
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onMouseStateChanged(MouseEvent event)
	{
		if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().currentScreen == null && Minecraft.getMinecraft().player.getCooledAttackStrength(0.0F) == 1)
		{
			if (event.isButtonstate())
			{
				if (event.getButton() == 0 || event.getButton() == 1)
				{
					ClientCombatHandler.processClick(Minecraft.getMinecraft().player, event.getButton() == 1);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onOpenGui(GuiScreenEvent.InitGuiEvent event)
	{
		if (event.getGui() instanceof GuiContainer)
		{
			GuiContainer gc = (GuiContainer) event.getGui();
			if ((gc instanceof GuiInventory || gc instanceof GuiContainerCreative) && Minecraft.getMinecraft().player.capabilities.isCreativeMode)
			{
				return;
			}
			
			Container c = gc.inventorySlots;
			for (int i = 0; i < c.inventorySlots.size(); ++i)
			{
				Slot s = c.inventorySlots.get(i);
				if (s.getClass().equals(Slot.class) && s.inventory instanceof InventoryPlayer && !(s instanceof ManagedSlot) && s.getSlotIndex() >= 9 && s.getSlotIndex() < 36)
				{
					ManagedSlot wrapper = new ManagedSlot(s);
					c.inventorySlots.remove(i);
					c.inventorySlots.add(i, wrapper);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onDrawGui(GuiScreenEvent.DrawScreenEvent.Pre event)
	{
		if (event.getGui() instanceof GuiContainer)
		{
			PlayerInventoryRenderer.render(Minecraft.getMinecraft().player, event.getGui());
		}
	}
	
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
