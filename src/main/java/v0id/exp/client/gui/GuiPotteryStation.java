package v0id.exp.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.util.Strings;
import org.lwjgl.opengl.GL11;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.recipe.RecipesPottery;
import v0id.core.VCStatics;
import v0id.exp.container.ContainerPotteryStation;
import v0id.exp.net.PacketHandlerCraftPottery;
import v0id.exp.tile.TilePotteryStation;
import v0id.exp.util.OreDictManager;

import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.Arrays;

public class GuiPotteryStation extends GuiContainer
{
    public TilePotteryStation potteryStation;
    public int currentRecipe;
    public Puzzle currentPuzzle;

    public GuiPotteryStation(InventoryPlayer playerInv, TilePotteryStation potteryStation)
    {
        super(new ContainerPotteryStation(playerInv, potteryStation));
        this.potteryStation = potteryStation;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.addButton(new GuiButtonExP(0, i + 6, j + 46, 20, 20, Strings.EMPTY));
        ItemStack is = this.potteryStation.inventory.getStackInSlot(0);
        if (!is.isEmpty() && Arrays.stream(OreDictManager.getOreNames(is)).anyMatch(s -> s.equalsIgnoreCase("clay")) && is.getCount() == 8)
        {
            RecipesPottery.RecipePottery recipe = RecipesPottery.allRecipes.get(this.currentRecipe);
            this.currentPuzzle = new Puzzle(recipe.getDivider(), recipe.getGuiTexture());
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        if (button.id == 0)
        {
            ItemStack is = this.potteryStation.inventory.getStackInSlot(0);
            if (!is.isEmpty() && Arrays.stream(OreDictManager.getOreNames(is)).anyMatch(s -> s.equalsIgnoreCase("clay")) && is.getCount() == 8)
            {
                ++this.currentRecipe;
                if (this.currentRecipe >= RecipesPottery.allRecipes.size())
                {
                    this.currentRecipe = 0;
                }

                RecipesPottery.RecipePottery recipe = RecipesPottery.allRecipes.get(this.currentRecipe);
                this.currentPuzzle = new Puzzle(recipe.getDivider(), recipe.getGuiTexture());
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.currentPuzzle != null)
        {
            this.currentPuzzle.click(mouseX, mouseY, mouseButton);
            if (this.currentPuzzle.complete)
            {
                PacketHandlerCraftPottery.sendCraftingPacket(this.currentRecipe, this.potteryStation);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiPotteryStation);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        if (this.currentPuzzle != null)
        {
            ItemStack is = this.potteryStation.inventory.getStackInSlot(0);
            if (!is.isEmpty() && Arrays.stream(OreDictManager.getOreNames(is)).anyMatch(s -> s.equalsIgnoreCase("clay")) && is.getCount() == 8)
            {
                this.currentPuzzle.renderPuzzle(i + 60, j + 14, mouseX, mouseY);
            }
            else
            {
                this.currentPuzzle = null;
            }
        }
        else
        {
            ItemStack is = this.potteryStation.inventory.getStackInSlot(0);
            if (!is.isEmpty() && Arrays.stream(OreDictManager.getOreNames(is)).anyMatch(s -> s.equalsIgnoreCase("clay")) && is.getCount() == 8)
            {
                RecipesPottery.RecipePottery recipe = RecipesPottery.allRecipes.get(this.currentRecipe);
                this.currentPuzzle = new Puzzle(recipe.getDivider(), recipe.getGuiTexture());
            }
        }

        this.renderHoveredToolTip(mouseX, mouseY);
        RecipesPottery.RecipePottery currentRecipe = RecipesPottery.allRecipes.get(this.currentRecipe);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(currentRecipe.getItem(), i + 8, j + 48);
        if (this.buttonList.get(0).isMouseOver())
        {
            this.drawHoveringText(currentRecipe.getItem().getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL), mouseX, mouseY);
        }
    }

    public static class Puzzle
    {
        public PuzzlePiece[] pieces = new PuzzlePiece[0];
        public PuzzlePiece activePiece;
        public int size;
        public ResourceLocation location;
        public float lastPosX;
        public float lastPosY;
        public boolean complete;

        public Puzzle(int size, ResourceLocation location)
        {
            this.size = size;
            this.location = location;
            this.createPuzzle();
        }

        public void createPuzzle()
        {
            this.pieces = new PuzzlePiece[this.size * this.size];
            for (int x = 0; x < this.size; ++x)
            {
                for (int y = 0; y < this.size; ++y)
                {
                    float step = 1F / this.size;
                    float minU = x * step;
                    float maxU = (x + 1) * step;
                    float minV = y * step;
                    float maxV = (y + 1) * step;
                    this.pieces[x + y * this.size] = new PuzzlePiece(this, x, y, x, y, minU, maxU, minV, maxV, 0);
                }
            }

            for (int i = 0; i < this.size + VCStatics.rng.nextInt(this.size); ++i)
            {
                PuzzlePiece p1 = this.pieces[VCStatics.rng.nextInt(this.pieces.length)];
                PuzzlePiece p2 = this.pieces[VCStatics.rng.nextInt(this.pieces.length)];
                while (p2 == p1)
                {
                    p2 = this.pieces[VCStatics.rng.nextInt(this.pieces.length)];
                }

                int px = p2.posX;
                int py = p2.posY;
                p2.posX = p1.posX;
                p2.posY = p1.posY;
                p1.posX = px;
                p1.posY = py;
            }
        }

        public void renderPuzzle(float x, float y, float mouseX, float mouseY)
        {
            this.lastPosX = x;
            this.lastPosY = y;
            RenderHelper.disableStandardItemLighting();
            RenderHelper.enableGUIStandardItemLighting();
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.disableTexture2D();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            for (PuzzlePiece piece : pieces)
            {
                piece.render(x, y, this.size, buffer);
            }

            Tessellator.getInstance().draw();
            GlStateManager.enableTexture2D();
            Minecraft.getMinecraft().renderEngine.bindTexture(this.location);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            for (PuzzlePiece piece : pieces)
            {
                piece.render(x, y, this.size, buffer);
            }

            Tessellator.getInstance().draw();
            RenderHelper.enableStandardItemLighting();
        }

        public void click(float mx, float my, int button)
        {
            if (button == 0)
            {
                float step = 54F / this.size;
                for (PuzzlePiece piece : this.pieces)
                {
                    float px = this.lastPosX + piece.posX * step + piece.posX * 3;
                    float py = this.lastPosY + piece.posY * step + piece.posY * 3;
                    float pex = px + step;
                    float pey = py + step;
                    if (mx >= px && mx <= pex && my >= py && my <= pey)
                    {
                        if (this.activePiece == null)
                        {
                            this.activePiece = piece;
                        }
                        else
                        {
                            int ppx = this.activePiece.posX;
                            int ppy = this.activePiece.posY;
                            this.activePiece.posX = piece.posX;
                            this.activePiece.posY = piece.posY;
                            piece.posX = ppx;
                            piece.posY = ppy;
                            this.activePiece = null;
                            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_GRAVEL_BREAK, 2.0F));
                        }

                        break;
                    }
                }
            }

            for (PuzzlePiece piece : this.pieces)
            {
                if (piece.posX != piece.correctPosX || piece.posY != piece.correctPosY)
                {
                    return;
                }
            }

            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1F));
            this.complete = true;
        }

        public void clickPiece(PuzzlePiece piece)
        {
        }

        public void rightClickPiece(PuzzlePiece piece)
        {
        }
    }

    public static class PuzzlePiece
    {
        public int posX;
        public int posY;
        public int correctPosX;
        public int correctPosY;
        public float minU;
        public float maxU;
        public float minV;
        public float maxV;
        public int rotation;
        public Puzzle parent;

        public PuzzlePiece(Puzzle parent, int posX, int posY, int correctPosX, int correctPosY, float minU, float maxU, float minV, float maxV, int rotation)
        {
            this.parent = parent;
            this.posX = posX;
            this.posY = posY;
            this.correctPosX = correctPosX;
            this.correctPosY = correctPosY;
            this.minU = minU;
            this.maxU = maxU;
            this.minV = minV;
            this.maxV = maxV;
            this.rotation = rotation;
        }

        public void render(float x, float y, int size, BufferBuilder buffer)
        {
            Vector3f c = this == this.parent.activePiece ? new Vector3f(0, 1, 1) : new Vector3f(0, 0, 0);
            float step = 54F / size;
            float px = x + this.posX * step + this.posX * 3;
            float py = y + this.posY * step + this.posY * 3;
            if (buffer.getVertexFormat() == DefaultVertexFormats.POSITION_COLOR)
            {
                buffer.pos(px, py, 1).color(c.x, c.y, c.z, 1.0F).endVertex();
                buffer.pos(px, py + step, 1).color(c.x, c.y, c.z, 1.0F).endVertex();
                buffer.pos(px + step, py + step, 1).color(c.x, c.y, c.z, 1.0F).endVertex();
                buffer.pos(px + step, py, 1).color(c.x, c.y, c.z, 1.0F).endVertex();
            }
            else
            {
                buffer.pos(px, py, 1).tex(minU, minV).color(1, 1, 1, 1.0F).endVertex();
                buffer.pos(px, py + step, 1).tex(minU, maxV).color(1, 1, 1, 1.0F).endVertex();
                buffer.pos(px + step, py + step, 1).tex(maxU, maxV).color(1, 1, 1, 1.0F).endVertex();
                buffer.pos(px + step, py, 1).tex(maxU, minV).color(1, 1, 1, 1.0F).endVertex();
            }
        }
    }
}
