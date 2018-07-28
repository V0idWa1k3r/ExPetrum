package v0id.exp.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import v0id.api.exp.data.ExPMisc;

import java.io.IOException;

public class GuiViolatedFingerprint extends GuiScreen
{
    @Override
    public void initGui()
    {
        super.initGui();
        this.addButton(new GuiButton(0, 40, this.height - 40, 200, 20, I18n.format("exp.btn.exit")));
        this.addButton(new GuiButton(1, this.width - 240, this.height - 40, 200, 20, I18n.format("exp.btn.continue")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        if (button.id == 0)
        {
            ExPMisc.modLogger.info("Shutting down the game as the user asked for it.");
            FMLCommonHandler.instance().exitJava(0, false);
        }
        else
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.scale(3, 3, 3);
        this.drawCenteredString(this.fontRenderer, I18n.format("exp.txt.securityException"), this.width / 6, 10, 0xff0000);
        GlStateManager.popMatrix();
        this.fontRenderer.drawSplitString(I18n.format("exp.txt.securityText"), 20, 80, this.width - 40, 0xffffff);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2, 2, 2);
        this.drawCenteredString(this.fontRenderer, I18n.format("exp.txt.securityWarning"), this.width / 4, 80, 0xffff00);
        GlStateManager.popMatrix();
    }
}
