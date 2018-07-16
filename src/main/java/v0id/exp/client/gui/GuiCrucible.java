package v0id.exp.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.metal.EnumMetal;
import v0id.exp.container.ContainerCrucible;
import v0id.exp.tile.TileCrucible;

import java.util.Map;

public class GuiCrucible extends GuiContainer
{
    public TileCrucible crucible;

    public GuiCrucible(InventoryPlayer playerInv, TileCrucible tileCrucible)
    {
        super(new ContainerCrucible(playerInv, tileCrucible));
        this.crucible = tileCrucible;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiCrucible);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        float temp = this.crucible.temperature_handler.getCurrentTemperature();
        int tOffset = (int)(Math.min(1800, temp) / 26.5F);
        this.drawTexturedModalRect(i + 7, j + 76 - tOffset, 248, 0, 8, 5);
        if (!this.crucible.metalMap.isEmpty())
        {
            int overallMetal = this.crucible.metalMap.values().stream().mapToInt(f -> f.intValue()).sum();
            int k = 0;
            for (Map.Entry<EnumMetal, Float> entry : this.crucible.metalMap.entrySet())
            {
                if (k > 6)
                {
                    break;
                }

                Minecraft.getMinecraft().fontRenderer.drawString(String.format(I18n.format("exp.item.ingot.desc.type." + entry.getKey().getName()) + " %.1f%%", entry.getValue() / overallMetal * 100), i + 68, j + 14 + k * 10, 0xffffff);
                ++k;
            }

            this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, overallMetal + "/10000", i + 115, j + 74, 0xffffff);
        }

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
