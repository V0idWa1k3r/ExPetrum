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
import v0id.api.exp.recipe.RecipesBarrel;
import v0id.api.exp.util.ColorRGB;
import v0id.exp.container.ContainerBarrel;
import v0id.exp.tile.TileBarrel;

import java.util.List;

public class GuiBarrel extends GuiContainer
{
    public TileBarrel barrel;

    public GuiBarrel(InventoryPlayer playerInv, TileBarrel tileBarrel)
    {
        super(new ContainerBarrel(playerInv, tileBarrel));
        this.barrel = tileBarrel;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiBarrel);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        FluidStack fs = this.barrel.fluidInventory.getFluid();
        if (fs != null)
        {
            float val = fs.amount / 10000F;
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fs.getFluid().getStill(fs).toString());
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            ColorRGB c = ColorRGB.FromHEX(fs.getFluid().getColor(fs));
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(i + 14, j + 14 + 62 * (1 - val), 0).tex(sprite.getMinU(), sprite.getMinV()).color(c.getR(), c.getG(), c.getB(), 1F).endVertex();
            buffer.pos(i + 14, j + 76, 0).tex(sprite.getMinU(), sprite.getMaxV()).color(c.getR(), c.getG(), c.getB(), 1F).endVertex();
            buffer.pos(i + 18, j + 76, 0).tex(sprite.getMaxU(), sprite.getMaxV()).color(c.getR(), c.getG(), c.getB(), 1F).endVertex();
            buffer.pos(i + 18, j + 14 + 62 * (1 - val), 0).tex(sprite.getMaxU(), sprite.getMinV()).color(c.getR(), c.getG(), c.getB(), 1F).endVertex();
            Tessellator.getInstance().draw();
        }

        RecipesBarrel.IRecipeBarrel recipe = RecipesBarrel.findRecipe(this.barrel.inventory.getStackInSlot(0), this.barrel.fluidInventory.getFluid());
        if (recipe != null)
        {
            this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, I18n.format(recipe.getRecipeName(this.barrel.inventory.getStackInSlot(0))), i + 110, j + 8, 0xffffff);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        FluidStack fs = this.barrel.fluidInventory.getFluid();
        if (mouseX >= i + 7 && mouseX <= i + 25 && mouseY >= j + 7 && mouseY <= j + 83)
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
