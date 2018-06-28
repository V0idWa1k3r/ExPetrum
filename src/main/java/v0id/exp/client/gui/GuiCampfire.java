package v0id.exp.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import v0id.api.exp.data.ExPTextures;
import v0id.exp.container.ContainerCampfire;
import v0id.exp.tile.TileCampfire;

public class GuiCampfire extends GuiContainer
{
    public TileCampfire campfire;

    public GuiCampfire(InventoryPlayer playerInv, TileCampfire tileCampfire)
    {
        super(new ContainerCampfire(playerInv, tileCampfire));
        this.campfire = tileCampfire;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiCampfire);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        float temp = this.campfire.temperature_handler.getCurrentTemperature();
        int tOffset = (int)Math.min(1800, temp) / 25;
        this.drawTexturedModalRect(i + 7, j + 76 - tOffset, 248, 0, 8, 5);
        float burnVal = this.campfire.maxBurnTime == 0 ? 0 : (float)this.campfire.burnTimeLeft / this.campfire.maxBurnTime;
        int bOffset = (int)Math.floor(Math.min(1, burnVal) * 14);
        this.drawTexturedModalRect(i + 80, j + 51 - bOffset, 176, (14 - bOffset), 14, bOffset);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
