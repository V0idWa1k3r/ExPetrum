package v0id.exp.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import v0id.api.exp.data.ExPTextures;
import v0id.exp.container.ContainerCrate;
import v0id.exp.tile.TileCrate;

public class GuiCrate extends GuiContainer
{
    public TileCrate crate;

    public GuiCrate(InventoryPlayer playerInv, TileCrate crate)
    {
        super(new ContainerCrate(playerInv, crate));
        this.crate = crate;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiInv9);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
