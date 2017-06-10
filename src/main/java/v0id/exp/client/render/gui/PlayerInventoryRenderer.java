package v0id.exp.client.render.gui;

import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.exp.player.inventory.ManagedSlot;
import v0id.exp.player.inventory.PlayerInventoryHelper;

public class PlayerInventoryRenderer
{
	public static void render(EntityPlayer player, Gui inventoryUI)
	{
		GuiContainer gInv = (GuiContainer) inventoryUI;
		Tessellator tec = Tessellator.getInstance();
		VertexBuffer buffer = tec.getBuffer();
		InventoryPlayer inventory = player.inventory;
		ManagedSlot mSlot = null;
		for (Slot s : gInv.inventorySlots.inventorySlots)
		{
			if (s instanceof ManagedSlot)
			{
				mSlot = (ManagedSlot) s;
				break;
			}
		}
		
		if (mSlot == null)
		{
			return;
		}
		
		buffer.setTranslation(gInv.getGuiLeft() + mSlot.xPos - 1, gInv.getGuiTop() + mSlot.yPos - 1, 1);
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		GlStateManager.disableTexture2D();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableLighting();
		float colorG = (float)Math.abs(Math.sin(Math.toRadians(player.ticksExisted * 5))) / 2;
		for (int i = 9; i < 36; ++i)
		{
			ItemStack is = inventory.getStackInSlot(i);
			if (is.isEmpty())
			{
				continue;
			}
			
			Pair<Byte, Byte> volume = PlayerInventoryHelper.getVolume(is);
			int x = (i - 9) % 9;
			int y = (i - 9) / 9;
			putOutlinedRectangle(buffer, x * 18 + 1, y * 18 + 1, volume.getLeft() * 18 - 2, volume.getRight() * 18 - 2, 0.5F, 0.5F, 0.5F, 1F, 0, colorG, 1F, 1F);
			//buffer.pos(x * 18, 							y * 18 + volume.getRight() * 18, 0)	.color(0F, 0F, 0F, 0.3F).endVertex();
			//buffer.pos(x * 18 + volume.getLeft() * 18, 	y * 18 + volume.getRight() * 18, 0)	.color(0F, 0F, 0F, 0.3F).endVertex();
			//buffer.pos(x * 18 + volume.getLeft() * 18, 	y * 18, 0)							.color(0F, 0F, 0F, 0.3F).endVertex();
			//buffer.pos(x * 18, 							y * 18, 0)							.color(0F, 0F, 0F, 0.3F).endVertex();
		}
		
		if (!inventory.getItemStack().isEmpty())
		{
			Slot s = gInv.getSlotUnderMouse();
			if (s != null && s.getSlotIndex() >= 9 && s.getSlotIndex() < 36 && s instanceof ManagedSlot)
			{
				Pair<Byte, Byte> volume = inventory.getItemStack().getItem() instanceof IWeightProvider ? ((IWeightProvider)inventory.getItemStack().getItem()).provideVolume(inventory.getItemStack()) : PlayerInventoryHelper.defaultVolume;
				int slotId = s.getSlotIndex();
				int x = (slotId - 9) % 9;
				int y = (slotId - 9) / 9;
				boolean canAccept = PlayerInventoryHelper.canSlotAccept(inventory.getItemStack(), Optional.empty(), player, s.getSlotIndex());
				putOutlinedRectangle(buffer, x * 18 + 1, y * 18 + 1, volume.getLeft() * 18 - 2, volume.getRight() * 18 - 2, canAccept ? 0.5F : 0.75F, canAccept ? 0.75F : 0.5F, 0.5F, 1F, canAccept ? 0 : 1, canAccept ? 1 : 0, 0, 1F);
			}
		}
		
		tec.draw();
		buffer.setTranslation(0, 0, 0);
		GlStateManager.enableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
	}
	
	public static void putRectangle(VertexBuffer buffer, int x, int y, int w, int h, float... color)
	{
		buffer.pos(x, 		y + h, 0)	.color(color[0], color[1], color[2], color[3]).endVertex();
		buffer.pos(x + w, 	y + h, 0)	.color(color[0], color[1], color[2], color[3]).endVertex();
		buffer.pos(x + w, 	y, 0)		.color(color[0], color[1], color[2], color[3]).endVertex();
		buffer.pos(x, 		y, 0)		.color(color[0], color[1], color[2], color[3]).endVertex();
	}
	
	public static void putOutlinedRectangle(VertexBuffer buffer, int x, int y, int w, int h, float... color)
	{
		putRectangle(buffer, x, y, w, h, color);
		float[] colorOutline = new float[4];
		System.arraycopy(color, 4, colorOutline, 0, 4);
		putRectangle(buffer, x - 1, y - 1, 1, h + 2, colorOutline);
		putRectangle(buffer, x + w, y - 1, 1, h + 2, colorOutline);
		putRectangle(buffer, x, y - 1, w, 1, colorOutline);
		putRectangle(buffer, x, y + h, w, 1, colorOutline);
	}
}
