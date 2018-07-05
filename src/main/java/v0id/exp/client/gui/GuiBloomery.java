package v0id.exp.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import v0id.api.exp.data.ExPTextures;
import v0id.exp.container.ContainerBloomery;
import v0id.exp.tile.TileBloomery;

public class GuiBloomery extends GuiContainer
{
    public TileBloomery bloomery;

    public GuiBloomery(InventoryPlayer playerInv, TileBloomery tileBloomery)
    {
        super(new ContainerBloomery(playerInv, tileBloomery));
        this.bloomery = tileBloomery;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiBloomery);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        float temp = this.bloomery.temperatureHandler.getCurrentTemperature();
        int tOffset = (int)Math.min(1800, temp) / 25;
        this.drawTexturedModalRect(i + 7, j + 76 - tOffset, 248, 0, 8, 5);
        float burnVal = this.bloomery.work == 0 ? 0 : (float)this.bloomery.work / 12000;
        int bOffset = (int)Math.floor(Math.min(1, burnVal) * 14);
        this.drawTexturedModalRect(i + 45, j + 77 - bOffset, 176, (14 - bOffset), 14, bOffset);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
