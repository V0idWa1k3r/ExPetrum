package v0id.exp.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import v0id.api.exp.data.ExPBlocks;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.util.Gradient;
import v0id.exp.ExPetrum;
import v0id.exp.client.fx.ParticleEngine;
import v0id.exp.client.model.EntityModelDynamic;
import v0id.exp.client.registry.ClientRegistry;
import v0id.exp.client.render.gui.PlayerInventoryRenderer;
import v0id.exp.client.render.hud.PlayerHUDRenderer;
import v0id.exp.client.render.hud.SpecialAttackRenderer;
import v0id.exp.client.render.sky.WorldSkyRenderer;
import v0id.exp.client.render.sky.WorldWeatherRenderer;
import v0id.exp.client.render.tile.TESRQuern;
import v0id.exp.combat.ClientCombatHandler;
import v0id.exp.item.ItemFood;
import v0id.exp.item.tool.IExPTool;
import v0id.exp.player.inventory.ManagedSlot;
import v0id.exp.player.inventory.PlayerInventoryHelper;
import v0id.exp.settings.impl.SettingsClient;
import v0id.exp.settings.impl.SettingsFlags;
import v0id.exp.util.temperature.TemperatureUtils;

import javax.vecmath.Vector3f;
import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = ExPRegistryNames.modid, value = Side.CLIENT)
public class ExPHandlerClient
{
    @SubscribeEvent
    public static void onKeyboardEvent(InputEvent.KeyInputEvent event)
    {
        if (ExPetrum.isDevEnvironment)
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_R))
            {
                EntityModelDynamic.reloadAllModels();
            }
        }
    }

    @SubscribeEvent
	public static void onModelRegistry(ModelRegistryEvent event)
    {
        // TODO remove this if forge fixes(changes) the ObjectHolder fields population.
        if (ExPBlocks.logs[0] == null)
        {
            ExPBlocks.logs = new Block[]{ ExPBlocks.log0, ExPBlocks.log1, ExPBlocks.log2, ExPBlocks.log3, ExPBlocks.log4, ExPBlocks.log5, ExPBlocks.log6, ExPBlocks.log7, ExPBlocks.log8 };
            ExPBlocks.logsDeco = new Block[]{ ExPBlocks.logDeco0, ExPBlocks.logDeco1, ExPBlocks.logDeco2, ExPBlocks.logDeco3, ExPBlocks.logDeco4, ExPBlocks.logDeco5, ExPBlocks.logDeco6, ExPBlocks.logDeco7, ExPBlocks.logDeco8 };
            ExPBlocks.leaves = new Block[]{ ExPBlocks.leaf0, ExPBlocks.leaf1, ExPBlocks.leaf2, ExPBlocks.leaf3, ExPBlocks.leaf4, ExPBlocks.leaf5, ExPBlocks.leaf6, ExPBlocks.leaf7, ExPBlocks.leaf8 };
            ExPBlocks.shrubs = new Block[]{ ExPBlocks.shrubNormal, ExPBlocks.shrubBlooming, ExPBlocks.shrubAutumn, ExPBlocks.shrubDead };
            ExPBlocks.berryBushes = new Block[]{ ExPBlocks.berryBushNormal, ExPBlocks.berryBushBerries, ExPBlocks.berryBushAutumn, ExPBlocks.berryBushDead };
            ExPBlocks.planks = new Block[]{ ExPBlocks.planks0, ExPBlocks.planks1, ExPBlocks.planks2 };
            ExPBlocks.craftingTables = new Block[]{ ExPBlocks.craftingTable0, ExPBlocks.craftingTable1, ExPBlocks.craftingTable2 };
        }

        ClientRegistry.registerModels();
    }

    @SubscribeEvent
    public static void onSoundRegistry(RegistryEvent.Register<SoundEvent> event)
    {
        ClientRegistry.registerSounds(event);
    }

    @SubscribeEvent
    public static void onWorldRenderLast(RenderWorldLastEvent event)
    {
        ExPMisc.defaultParticleEngineImpl.renderTick(event.getPartialTicks());
    }

	@SubscribeEvent
	public static void onWorldLoaded(WorldEvent.Load event)
	{
        ParticleEngine.onWorldChanged();
	}

	@SubscribeEvent
	public static void onRenderHand(RenderHandEvent event)
	{
        if (!SettingsFlags.instance.enableNewCombat)
        {
            return;
        }

		if (SpecialAttackRenderer.render(event.getPartialTicks()))
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onMouseStateChanged(MouseEvent event)
	{
	    if (!SettingsFlags.instance.enableNewCombat)
        {
            return;
        }

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
	public static void onOpenGui(GuiScreenEvent.InitGuiEvent event)
	{
		if (event.getGui() instanceof GuiContainer)
		{
            if (!SettingsFlags.instance.enableCustomInventory)
            {
                return;
            }

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

        if (event.getGui() instanceof GuiCreateWorld)
        {
            if (!SettingsFlags.instance.enableInvasiveChanges)
            {
                return;
            }

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
                f.set(event.getGui(), ExPMisc.worldTypeExP.getId());
            }
            catch (Exception ex)
            {
                ExPMisc.modLogger.error("Could not reflect GuiCreateWorld!", ex);
            }
        }
	}
	
	@SubscribeEvent
	public static void onDrawGui(GuiScreenEvent.DrawScreenEvent.Pre event)
	{
        if (!SettingsFlags.instance.enableCustomInventory)
        {
            return;
        }

		if (event.getGui() instanceof GuiContainer)
		{
			PlayerInventoryRenderer.render(Minecraft.getMinecraft().player, event.getGui());
		}

        if (event.getGui() instanceof GuiContainer)
        {
            GlStateManager.disableTexture2D();
            GlStateManager.color(1F, 1F, 1F, 1F);
            BufferBuilder bb = Tessellator.getInstance().getBuffer();
            GuiContainer container = (GuiContainer) event.getGui();
            int i = container.getGuiLeft();
            int j = container.getGuiTop();
            bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            for (Slot s : container.inventorySlots.inventorySlots)
            {
                if (!s.getStack().isEmpty())
                {
                    float x = s.xPos;
                    float y = s.yPos;
                    if (s.getStack().getItem() instanceof ItemFood)
                    {
                        bb.pos(i + x + 2, j + y + 17, 1).color(0.2F, 0.2F, 0.2F, 1F).endVertex();
                        bb.pos(i + x + 15, j + y + 17, 1).color(0.2F, 0.2F, 0.2F, 1F).endVertex();
                        bb.pos(i + x + 15, j + y + 16, 1).color(0.2F, 0.2F, 0.2F, 1F).endVertex();
                        bb.pos(i + x + 2, j + y + 16, 1).color(0.2F, 0.2F, 0.2F, 1F).endVertex();
                        float val = PlayerInventoryHelper.getWeight(s.getStack()) / 10F;
                        bb.pos(i + x + 2, j + y + 16, 1).color(0.9F, 1F, 1F, 1F).endVertex();
                        bb.pos(i + x + 2 + 13 * val, j + y + 16, 1).color(1F, 1F, 1F, 1F).endVertex();
                        bb.pos(i + x + 2 + 13 * val, j + y + 15, 1).color(1F, 1F, 1F, 1F).endVertex();
                        bb.pos(i + x + 2, j + y + 15, 1).color(1F, 1F, 1F, 1F).endVertex();
                    }

                    if (TemperatureUtils.getTemperature(s.getStack()) > 0)
                    {
                        bb.pos(i + x + 0, j + y + 4, 1).color(0.2F, 0.2F, 0.2F, 1F).endVertex();
                        bb.pos(i + x + 4, j + y + 4, 1).color(0.2F, 0.2F, 0.2F, 1F).endVertex();
                        bb.pos(i + x + 4, j + y + 0, 1).color(0.2F, 0.2F, 0.2F, 1F).endVertex();
                        bb.pos(i + x + 0, j + y + 0, 1).color(0.2F, 0.2F, 0.2F, 1F).endVertex();
                        Vector3f c = TemperatureUtils.TEMPERATURE_GRADIENT.interpolate(Math.min(1800, TemperatureUtils.getTemperature(s.getStack())), Gradient.Linear);
                        bb.pos(i + x + 1, j + y + 3, 1).color(c.x, c.y, c.z, 1F).endVertex();
                        bb.pos(i + x + 3, j + y + 3, 1).color(c.x, c.y, c.z, 1F).endVertex();
                        bb.pos(i + x + 3, j + y + 1, 1).color(c.x, c.y, c.z, 1F).endVertex();
                        bb.pos(i + x + 1, j + y + 1, 1).color(c.x, c.y, c.z, 1F).endVertex();
                    }
                }
            }

            Tessellator.getInstance().draw();
            GlStateManager.enableTexture2D();
        }
	}

	@SubscribeEvent
	public static void onHudRender(RenderGameOverlayEvent.Pre event)
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
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().world.provider != null && Minecraft.getMinecraft().world.provider.getDimension() == 0)
		{
		    if (SettingsClient.instance.enableCustomRain || SettingsClient.instance.enableCustomRain)
            {
                if (!(Minecraft.getMinecraft().world.provider.getSkyRenderer() instanceof WorldSkyRenderer))
                {
                    if (SettingsClient.instance.enableCustomSky)
                    {
                        Minecraft.getMinecraft().world.provider.setSkyRenderer(WorldSkyRenderer.getInstance());
                    }

                    if (SettingsClient.instance.enableCustomRain)
                    {
                        Minecraft.getMinecraft().world.provider.setWeatherRenderer(WorldWeatherRenderer.getInstance());
                    }
                }
            }
		}

        if (Minecraft.getMinecraft().world != null && event.phase == TickEvent.Phase.START)
        {
            ExPMisc.defaultParticleEngineImpl.tick();
            PlayerHUDRenderer.tick();
        }
	}
	
	@SubscribeEvent
	public static void onFogRender(EntityViewRenderEvent.FogDensity event)
	{
	    if (SettingsClient.instance.disableFog)
        {
            event.setDensity(0);
            event.setCanceled(true);
        }
	}

	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event)
    {
        ItemStack is = event.getItemStack();
        Pair<Byte, Byte> volume = PlayerInventoryHelper.getVolume(is);
        float weight = PlayerInventoryHelper.getWeight(is);
        int vval = volume.getLeft() * volume.getRight();
        int vindex = vval == 1 ? 0 : vval <= 4 ? 1 : vval <= 6 ? 2 : vval <= 9 ? 3 : 4;
        event.getToolTip().add(I18n.format("exp.txt.weightvolume", (int)(weight * 1000), I18n.format("exp.txt.volume." + vindex)));
        float temp = TemperatureUtils.getTemperature(is);
        if (temp > 0)
        {
            event.getToolTip().add(I18n.format("exp.temp." + TemperatureUtils.getTemperatureIndex(temp)));
        }

        if (is.getItem() instanceof ItemTool && !(is.getItem() instanceof IExPTool) && is.getItem().getRegistryName().getResourceDomain().equalsIgnoreCase("minecraft"))
        {
            event.getToolTip().add(I18n.format("exp.txt.toolDisabled"));
        }

        if (event.getFlags().isAdvanced())
        {
            int[] ids = OreDictionary.getOreIDs(is);
            for (int i : ids)
            {
                event.getToolTip().add(OreDictionary.getOreName(i));
            }
        }
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event)
    {
        event.getMap().registerSprite(new ResourceLocation("exp", "blocks/fluid/clay"));
        event.getMap().registerSprite(new ResourceLocation("exp", "blocks/fluid/lava_flow"));
        event.getMap().registerSprite(new ResourceLocation("exp", "blocks/fluid/lava_still"));
        event.getMap().registerSprite(new ResourceLocation("exp", "blocks/fluid/generic"));
        event.getMap().registerSprite(new ResourceLocation("exp", "blocks/fluid/water_flow"));
        event.getMap().registerSprite(new ResourceLocation("exp", "blocks/fluid/water_still"));
        event.getMap().registerSprite(new ResourceLocation("exp", "blocks/quern_side"));
        event.getMap().registerSprite(new ResourceLocation("exp", "blocks/windmill"));
        event.getMap().registerSprite(new ResourceLocation("exp", "blocks/saw"));
        TESRQuern.quernTopTexture = event.getMap().registerSprite(new ResourceLocation("exp", "blocks/quern_top"));
    }
}
