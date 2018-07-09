package v0id.exp.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.InventoryPlayer;
import org.apache.logging.log4j.util.Strings;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.recipe.RecipesPottery;
import v0id.exp.container.ContainerMechanicalPotteryStation;
import v0id.exp.net.ExPNetwork;
import v0id.exp.tile.TileMechanicalPotteryStation;

import java.io.IOException;

public class GuiMechanicalPotteryStation extends GuiContainer
{
    public TileMechanicalPotteryStation potteryStation;

    public GuiMechanicalPotteryStation(InventoryPlayer playerInv, TileMechanicalPotteryStation potteryStation)
    {
        super(new ContainerMechanicalPotteryStation(playerInv, potteryStation));
        this.potteryStation = potteryStation;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.addButton(new GuiButtonExP(0, i + 78, j + 32, 20, 20, Strings.EMPTY));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        if (button.id == 0)
        {
            int recipeNext = this.potteryStation.selectedRecipe + 1;
            if (recipeNext >= RecipesPottery.allRecipes.size())
            {
                recipeNext = 0;
            }

            this.potteryStation.selectedRecipe = recipeNext;
            ExPNetwork.sendSelectPottery(this.potteryStation, recipeNext);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiMechanicalPotteryStation);
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
        this.renderHoveredToolTip(mouseX, mouseY);
        RecipesPottery.RecipePottery currentRecipe = RecipesPottery.allRecipes.get(this.potteryStation.selectedRecipe);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(currentRecipe.getItem(), i + 80, j + 34);
        if (this.buttonList.get(0).isMouseOver())
        {
            this.drawHoveringText(currentRecipe.getItem().getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL), mouseX, mouseY);
        }
    }
}
