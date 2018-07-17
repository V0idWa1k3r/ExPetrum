package v0id.exp.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.util.ColorRGB;
import v0id.exp.container.ContainerOven;
import v0id.exp.tile.TileOven;

import java.util.List;

public class GuiOven extends GuiContainer
{
    public TileOven oven;

    public GuiOven(InventoryPlayer playerInv, TileOven tileOven)
    {
        super(new ContainerOven(playerInv, tileOven));
        this.oven = tileOven;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiOven);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        float temp = this.oven.temperature_handler.getCurrentTemperature();
        int tOffset = (int)(Math.min(1800, temp) / 26.5F);
        this.drawTexturedModalRect(i + 7, j + 76 - tOffset, 248, 0, 8, 5);
        float burnVal = this.oven.maxBurnTime == 0 ? 0 : (float)this.oven.burnTimeLeft / this.oven.maxBurnTime;
        int bOffset = (int)Math.floor(Math.min(1, burnVal) * 14);
        this.drawTexturedModalRect(i + 116, j + 49 - bOffset, 176, (14 - bOffset), 14, bOffset);
        FluidStack fs = this.oven.fluidTank.getFluid();
        if (fs != null)
        {
            float val = fs.amount / 1000F;
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fs.getFluid().getStill(fs).toString());
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            ColorRGB c = ColorRGB.FromHEX(fs.getFluid().getColor(fs));
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(i + 32, j + 14 + 62 * (1 - val), 0).tex(sprite.getMinU(), sprite.getMinV()).color(c.getR(), c.getG(), c.getB(), 1F).endVertex();
            buffer.pos(i + 32, j + 76, 0).tex(sprite.getMinU(), sprite.getMaxV()).color(c.getR(), c.getG(), c.getB(), 1F).endVertex();
            buffer.pos(i + 36, j + 76, 0).tex(sprite.getMaxU(), sprite.getMaxV()).color(c.getR(), c.getG(), c.getB(), 1F).endVertex();
            buffer.pos(i + 36, j + 14 + 62 * (1 - val), 0).tex(sprite.getMaxU(), sprite.getMinV()).color(c.getR(), c.getG(), c.getB(), 1F).endVertex();
            Tessellator.getInstance().draw();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        FluidStack fs = this.oven.fluidTank.getFluid();
        if (mouseX >= i + 25 && mouseX <= i + 43 && mouseY >= j + 7 && mouseY <= j + 83)
        {
            List<String> lines = Lists.newArrayList();
            if (fs == null)
            {
                lines.add(I18n.format("exp.txt.mb", 0));
            }
            else
            {
                lines.add(I18n.format(fs.getUnlocalizedName()));
                lines.add(I18n.format("exp.txt.mb", fs.amount));
            }

            this.drawHoveringText(lines, mouseX, mouseY);
        }
    }
}
