package v0id.exp.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import v0id.api.exp.data.ExPTextures;
import v0id.exp.container.ContainerForge;
import v0id.exp.tile.TileForge;

public class GuiForge extends GuiContainer
{
    public TileForge forge;

    public GuiForge(InventoryPlayer playerInv, TileForge forge)
    {
        super(new ContainerForge(playerInv, forge));
        this.forge = forge;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiForge);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        float temp = this.forge.temperature_handler.getCurrentTemperature();
        int tOffset = (int)Math.min(1800, temp) / 25;
        this.drawTexturedModalRect(i + 7, j + 76 - tOffset, 248, 0, 8, 5);
        float burnVal = this.forge.maxBurnTime == 0 ? 0 : (float)this.forge.burnTimeLeft / this.forge.maxBurnTime;
        int bOffset = (int)Math.floor(Math.min(1, burnVal) * 14);
        this.drawTexturedModalRect(i + 80, j + 59 - bOffset, 176, (14 - bOffset), 14, bOffset);
        this.drawTexturedModalRect(i + 62, j + 59 - bOffset, 176, (14 - bOffset), 14, bOffset);
        this.drawTexturedModalRect(i + 98, j + 59 - bOffset, 176, (14 - bOffset), 14, bOffset);
        this.drawTexturedModalRect(i + 44, j + 41 - bOffset, 176, (14 - bOffset), 14, bOffset);
        this.drawTexturedModalRect(i + 116, j + 41 - bOffset, 176, (14 - bOffset), 14, bOffset);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
