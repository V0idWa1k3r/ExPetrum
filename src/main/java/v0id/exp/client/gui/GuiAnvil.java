package v0id.exp.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.metal.AnvilMinigame;
import v0id.api.exp.recipe.RecipesAnvil;
import v0id.core.util.I18n;
import v0id.exp.container.ContainerAnvil;
import v0id.exp.net.PacketHandlerAnvilRecipe;
import v0id.exp.net.PacketHandlerCardClick;
import v0id.exp.net.PacketHandlerWeld;
import v0id.exp.tile.TileAnvil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GuiAnvil extends GuiContainer
{
    public TileAnvil anvil;
    public boolean choosingRecipe;

    public GuiAnvil(InventoryPlayer playerInv, TileAnvil anvil)
    {
        super(new ContainerAnvil(playerInv, anvil));
        this.anvil = anvil;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.addButton(new GuiButtonExP(0, i + 7, j + 25, 36, 20, I18n.format("exp.txt.weld")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        if (!this.choosingRecipe)
        {
            if (button.id == 0)
            {
                PacketHandlerWeld.sendPacket(this.anvil);
            }
        }
    }

    public RecipesAnvil.IAnvilRecipe getCurrentRecipe()
    {
        ItemStack is = this.anvil.inventory.getStackInSlot(3);
        if (!is.isEmpty() && is.hasTagCompound() && is.getTagCompound().hasKey("exp:smithing"))
        {
            return RecipesAnvil.allRecipes.get(is.getTagCompound().getCompoundTag("exp:smithing").getInteger("recipe"));
        }

        return null;
    }

    public List<RecipesAnvil.IAnvilRecipe> getRecipes()
    {
        ItemStack is = this.anvil.inventory.getStackInSlot(3);
        List<RecipesAnvil.IAnvilRecipe> ret = Lists.newArrayList();
        for (RecipesAnvil.IAnvilRecipe rec : RecipesAnvil.allRecipes)
        {
            if (rec.matchesForInterface(is, this.anvil.getWorld().getBlockState(this.anvil.getPos()).getValue(ExPBlockProperties.ANVIL_MATERIAL).getTier()))
            {
                ret.add(rec);
            }
        }

        return ret;
    }

    public int getHeat()
    {
        ItemStack is = this.anvil.inventory.getStackInSlot(3);
        if (!is.isEmpty() && is.hasTagCompound() && is.getTagCompound().hasKey("exp:smithing"))
        {
            return is.getTagCompound().getCompoundTag("exp:smithing").getInteger("heat");
        }

        return 0;
    }

    public int getIntegrity()
    {
        ItemStack is = this.anvil.inventory.getStackInSlot(3);
        if (!is.isEmpty() && is.hasTagCompound() && is.getTagCompound().hasKey("exp:smithing"))
        {
            return is.getTagCompound().getCompoundTag("exp:smithing").getInteger("integrity");
        }

        return 0;
    }

    public int getProgress()
    {
        ItemStack is = this.anvil.inventory.getStackInSlot(3);
        if (!is.isEmpty() && is.hasTagCompound() && is.getTagCompound().hasKey("exp:smithing"))
        {
            return is.getTagCompound().getCompoundTag("exp:smithing").getInteger("progress");
        }

        return 0;
    }

    public AnvilMinigame.Card[] getCards()
    {
        ItemStack is = this.anvil.inventory.getStackInSlot(3);
        if (!is.isEmpty() && is.hasTagCompound() && is.getTagCompound().hasKey("exp:smithing"))
        {
            AnvilMinigame.ensureAllCardsAreRegistered();
            AnvilMinigame.Card[] ret = new AnvilMinigame.Card[is.getTagCompound().getCompoundTag("exp:smithing").getByte("cards")];
            for (int i = 0; i < ret.length; ++i)
            {
                ret[i] = AnvilMinigame.Card.allCards.get(is.getTagCompound().getCompoundTag("exp:smithing").getByte("card_" + i));
            }

            return ret;
        }

        return null;
    }

    @Override
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
    {
        if (!this.choosingRecipe)
        {
            super.handleMouseClick(slotIn, slotId, mouseButton, type);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (!this.choosingRecipe)
        {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            if (mouseX >= i + 62 && mouseX <= i + 80 && mouseY >= j + 65 && mouseY <= j + 83)
            {
                if (!this.getRecipes().isEmpty() && this.getCurrentRecipe() == null)
                {
                    this.choosingRecipe = true;
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }

            if (this.getCurrentRecipe() != null)
            {
                AnvilMinigame.Card[] cards = this.getCards();
                int xOffset = cards.length == 2 ? 21 : 0;
                for (int k = 0; k < cards.length; ++k)
                {
                    boolean mouseOver = mouseX > i + xOffset + 43 + k * 42 && mouseX < i + xOffset + 82 + k * 42 && mouseY > j + 7 && mouseY < j + 65;
                    if (mouseOver)
                    {
                        PacketHandlerCardClick.sendPacket(this.anvil, cards[k].id);
                    }
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(this.choosingRecipe ? ExPTextures.guiNone : ExPTextures.guiAnvil);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        if (!this.choosingRecipe)
        {
            if (this.getCurrentRecipe() != null)
            {
                FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
                boolean unicode = fr.getUnicodeFlag();
                fr.setUnicodeFlag(true);
                AnvilMinigame.Card[] cards = this.getCards();
                int xOffset = cards.length == 2 ? 21 : 0;
                for (int k = 0; k < cards.length; ++k)
                {
                    boolean mouseOver = mouseX > i + xOffset + 43 + k * 42 && mouseX < i + xOffset + 82 + k * 42 && mouseY > j + 7 && mouseY < j + 65;
                    Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiAnvil);
                    this.drawTexturedModalRect(i + xOffset + 43 + k * 42, j + 7, 176, 0, 42, 58);
                    this.drawCenteredString(fr, I18n.format(cards[k].name), i + xOffset + 64 + k * 42, j + 12, mouseOver ? 0x00ff00 : 0xffffff);
                    String s = I18n.format(cards[k].desc);
                    String[] split = s.split("\\|");
                    for (int l = 0; l < split.length; ++l)
                    {
                        this.drawCenteredString(fr, split[l], i + xOffset + 64 + k * 42, j + 22 + 10 * l, 0xffffff);
                    }
                }

                Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiAnvil);
                this.drawTexturedModalRect(i + 82, j + 70, 176, 76, 8, 8);
                this.drawTexturedModalRect(i + 102, j + 65, 176, 84, 8, 8);
                this.drawTexturedModalRect(i + 102, j + 74, 176, 92, 8, 8);
                fr.drawString(Integer.toString(this.getHeat()), i + 90, j + 70, 0xffffff);
                fr.drawString(Integer.toString(this.getIntegrity()) + "/100", i + 110, j + 65, 0xffffff);
                fr.drawString(Integer.toString(this.getProgress()) + "/" + Integer.toString(this.getCurrentRecipe().getProgressRequired(this.anvil.inventory.getStackInSlot(3))), i + 110, j + 74, 0xffffff);
                fr.setUnicodeFlag(unicode);
            }
            else
            {
                if (mouseX >= i + 62 && mouseX <= i + 80 && mouseY >= j + 65 && mouseY <= j + 83)
                {
                    this.drawTexturedModalRect(i + 62, j + 65, 177, 58, 18, 18);
                }

                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.FILLED_MAP, 1, 0), i + 62, j + 66);
            }
        }
        else
        {
            List<RecipesAnvil.IAnvilRecipe> recipes = this.getRecipes();
            List<String> toDraw = null;
            for (int k = 0; k < recipes.size() + 1; ++k)
            {
                int x = 8 + (k % 9) * 18;
                int y = 8 + (k / 9) * 18;
                Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.guiAnvil);
                this.drawTexturedModalRect(i + x, j + y, 7, 83, 18, 18);
                if (mouseX >= i + x && mouseX <= i + x + 18 && mouseY >= j + y && mouseY <= j + y + 18)
                {
                    GlStateManager.pushMatrix();
                    if (k > 0)
                    {
                        toDraw = recipes.get(k - 1).getResult(this.anvil.inventory.getStackInSlot(3)).getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL);
                    }
                    else
                    {
                        toDraw = Collections.singletonList(I18n.format("exp.txt.back"));
                    }

                    GlStateManager.popMatrix();
                    if (Mouse.isButtonDown(0))
                    {
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        if (k == 0)
                        {
                            choosingRecipe = false;
                        }
                        else
                        {
                            choosingRecipe = false;
                            PacketHandlerAnvilRecipe.sendPacket(this.anvil, recipes.get(k - 1));
                        }
                    }
                }

                if (k != 0)
                {
                    Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(recipes.get(k - 1).getResult(this.anvil.inventory.getStackInSlot(3)), i + x + 1, j + y + 1);
                }
            }

            if (toDraw != null)
            {
                this.drawHoveringText(toDraw, mouseX, mouseY);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        if (!this.choosingRecipe)
        {
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.renderHoveredToolTip(mouseX, mouseY);
        }
        else
        {
            this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        }
    }
}
