package v0id.exp.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.recipe.RecipesBlastFurnace;
import v0id.exp.container.ContainerBlastFurnace;
import v0id.exp.tile.TileBlastFurnace;

import java.util.List;

public class GuiBlastFurnace extends GuiContainer
{
    public TileBlastFurnace blastFurnace;

    public GuiBlastFurnace(InventoryPlayer playerInv, TileBlastFurnace tileBlastFurnace)
    {
        super(new ContainerBlastFurnace(playerInv, tileBlastFurnace));
        this.blastFurnace = tileBlastFurnace;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiBlastFurnace);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        float temp = this.blastFurnace.temperatureHandler.getCurrentTemperature();
        int tOffset = (int)(Math.min(1800, temp) / 26.5F);
        this.drawTexturedModalRect(i + 7, j + 76 - tOffset, 248, 0, 8, 5);
        float burnVal = this.blastFurnace.work == 0 ? 0 : (float)this.blastFurnace.work / this.blastFurnace.maxWork;
        int bOffset = (int)Math.floor(Math.min(1, burnVal) * 14);
        this.drawTexturedModalRect(i + 63, j + 77 - bOffset, 176, (14 - bOffset), 14, bOffset);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        List<String> tooltip = null;
        if (this.blastFurnace.recipe != -1)
        {
            RecipesBlastFurnace.IBlastFurnaceRecipe recipe = RecipesBlastFurnace.allRecipes.get(this.blastFurnace.recipe);
            ItemStack[] data = recipe.getInput();
            if (data.length == 1)
            {
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(data[0], i + 62, j + 8);
                GlStateManager.translate(0, 0, 500);
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Integer.toString(data[0].getCount() * this.blastFurnace.recipeAmount), i + 70, j + 17, 0xffffff);
                GlStateManager.translate(0, 0, -500);
                if (mouseX >= i + 61 && mouseX <= i + 79 && mouseY >= j + 7 && mouseY <= j + 25)
                {
                    tooltip = data[0].getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL);
                }
            }
            else
            {
                if (data.length == 2)
                {
                    Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(data[0], i + 26, j + 8);
                    GlStateManager.translate(0, 0, 500);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Integer.toString(data[0].getCount() * this.blastFurnace.recipeAmount), i + 34, j + 17, 0xffffff);
                    GlStateManager.translate(0, 0, -500);
                    if (mouseX >= i + 25 && mouseX <= i + 43 && mouseY >= j + 7 && mouseY <= j + 25)
                    {
                        tooltip = data[0].getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL);
                    }

                    Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(data[1], i + 98, j + 8);
                    GlStateManager.translate(0, 0, 500);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Integer.toString(data[1].getCount() * this.blastFurnace.recipeAmount), i + 105, j + 17, 0xffffff);
                    GlStateManager.translate(0, 0, -500);
                    if (mouseX >= i + 97 && mouseX <= i + 115 && mouseY >= j + 7 && mouseY <= j + 25)
                    {
                        tooltip = data[1].getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL);
                    }
                }
                else
                {
                    if (data.length == 3)
                    {
                        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(data[0], i + 26, j + 8);
                        GlStateManager.translate(0, 0, 500);
                        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Integer.toString(data[0].getCount() * this.blastFurnace.recipeAmount), i + 34, j + 17, 0xffffff);
                        GlStateManager.translate(0, 0, -500);
                        if (mouseX >= i + 25 && mouseX <= i + 43 && mouseY >= j + 7 && mouseY <= j + 25)
                        {
                            tooltip = data[0].getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL);
                        }

                        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(data[1], i + 62, j + 8);
                        GlStateManager.translate(0, 0, 500);
                        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Integer.toString(data[1].getCount() * this.blastFurnace.recipeAmount), i + 70, j + 17, 0xffffff);
                        GlStateManager.translate(0, 0, -500);
                        if (mouseX >= i + 61 && mouseX <= i + 79 && mouseY >= j + 7 && mouseY <= j + 25)
                        {
                            tooltip = data[1].getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL);
                        }

                        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(data[2], i + 98, j + 8);
                        GlStateManager.translate(0, 0, 500);
                        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Integer.toString(data[2].getCount() * this.blastFurnace.recipeAmount), i + 105, j + 17, 0xffffff);
                        GlStateManager.translate(0, 0, -500);
                        if (mouseX >= i + 97 && mouseX <= i + 115 && mouseY >= j + 7 && mouseY <= j + 25)
                        {
                            tooltip = data[2].getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL);
                        }
                    }
                }
            }

            if (tooltip != null)
            {
                this.drawHoveringText(tooltip, mouseX, mouseY);
            }
        }
    }
}
