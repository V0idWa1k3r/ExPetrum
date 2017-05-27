package v0id.exp.client.render.hud;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import v0id.api.exp.data.ExPWeaponAttacks;
import v0id.api.exp.player.IExPPlayer;

public class SpecialAttackRenderer
{
	private static final Random rand = new Random();
	
	public static boolean render(float partialTicks)
	{
		if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0)
		{
			return false;
		}
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		IExPPlayer data = IExPPlayer.of(player);
		if (data != null && data.getCurrentSpecialAttack() != null)
		{
			ItemStack is = player.getHeldItemMainhand().isEmpty() ? player.getHeldItemOffhand() : player.getHeldItemMainhand();
			if (data.getCurrentSpecialAttack().attackInstance == ExPWeaponAttacks.piercingDash)
			{
				int current = data.getCurrentSpecialAttack().executionTime;
				float animationIndex = ((float)current - partialTicks) / data.getCurrentSpecialAttack().attackInstance.getExecutionTime();
				float translateZIndex = 0;
				if (animationIndex >= 0.7)
				{
					translateZIndex = -(1 - animationIndex);
				}
				else
				{
					translateZIndex = -0.3F + (1 - animationIndex) / 3;
				}
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.09, -0.15, -0.25 + translateZIndex);
				GlStateManager.rotate(135, 0, 1, 0);
				GlStateManager.rotate(90, 1, 0, 0);
				Minecraft.getMinecraft().getRenderItem().renderItem(is, player, TransformType.GROUND, true);
				GlStateManager.popMatrix();
				return true;
			}
			
			if (data.getCurrentSpecialAttack().attackInstance == ExPWeaponAttacks.slash)
			{
				int current = data.getCurrentSpecialAttack().executionTime;
				float animationIndex = ((float)current - partialTicks) / data.getCurrentSpecialAttack().attackInstance.getExecutionTime();
				float rotateYIndex = 60 - animationIndex * 120;
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.09, -0.2, -0.25);
				GlStateManager.rotate(135 + rotateYIndex, 0, 1, 0);
				GlStateManager.rotate(90, 1, 0, 0);
				Minecraft.getMinecraft().getRenderItem().renderItem(is, player, TransformType.GROUND, true);
				GlStateManager.popMatrix();
				return true;
			}
			
			if (data.getCurrentSpecialAttack().attackInstance == ExPWeaponAttacks.downStrike)
			{
				int current = data.getCurrentSpecialAttack().executionTime;
				float animationIndex = ((float)current - partialTicks) / data.getCurrentSpecialAttack().attackInstance.getExecutionTime();
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, -0.1 - (1 - animationIndex), -0.3);
				GlStateManager.rotate(110, 0, 1, 0);
				GlStateManager.rotate(25, 1, 0, 0);
				GlStateManager.rotate(10 - (1 - animationIndex) * 180, 0, 0, 1);
				Minecraft.getMinecraft().getRenderItem().renderItem(is, player, TransformType.GROUND, true);
				GlStateManager.popMatrix();
				return true;
			}
			
			if (data.getCurrentSpecialAttack().attackInstance == ExPWeaponAttacks.spin)
			{
				int current = data.getCurrentSpecialAttack().executionTime;
				float animationIndex = ((float)current - partialTicks) / data.getCurrentSpecialAttack().attackInstance.getExecutionTime();
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.09, -0.2, -0.25);
				GlStateManager.rotate(240 - (1 - animationIndex) * 160, 0, 1, 0);
				GlStateManager.rotate(90, 1, 0, 0);
				Minecraft.getMinecraft().getRenderItem().renderItem(is, player, TransformType.GROUND, true);
				GlStateManager.popMatrix();
				return true;
			}
			
			if (data.getCurrentSpecialAttack().attackInstance == ExPWeaponAttacks.shieldSlam)
			{
				is = player.getActiveItemStack();
				if (is.isEmpty())
				{
					return false;
				}
				
				int current = data.getCurrentSpecialAttack().executionTime;
				float animationIndex = ((float)current * 2F - partialTicks) / data.getCurrentSpecialAttack().attackInstance.getExecutionTime();
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.56F, -0.52F, -0.72F - (1 - animationIndex));
				Minecraft.getMinecraft().getRenderItem().renderItem(is, player, TransformType.FIRST_PERSON_LEFT_HAND, true);
				GlStateManager.popMatrix();
				return true;
			}
			
			if (data.getCurrentSpecialAttack().attackInstance == ExPWeaponAttacks.behead)
			{
				int current = data.getCurrentSpecialAttack().executionTime;
				float animationIndex = ((float)current - partialTicks) / data.getCurrentSpecialAttack().attackInstance.getExecutionTime();
				float translateYIndex = 0;
				float translateXIndex = 0;
				float rotateXIndex = 0;
				if (animationIndex < 0.5F)
				{
					translateYIndex = animationIndex - 0.5F;
					translateXIndex = animationIndex - 0.5F;
					rotateXIndex = (0.5F - animationIndex) * 90;
				}
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.3 + translateXIndex, 0.1 + translateYIndex, -0.2);
				GlStateManager.rotate(130, 0, 1, 0);
				GlStateManager.rotate(180 + rotateXIndex, 1, 0, 0);
				GlStateManager.rotate(-90, 0, 0, 1);
				Minecraft.getMinecraft().getRenderItem().renderItem(is, player, TransformType.GROUND, true);
				GlStateManager.popMatrix();
				return true;
			}
			
			if (data.getCurrentSpecialAttack().attackInstance == ExPWeaponAttacks.stab)
			{
				int current = data.getCurrentSpecialAttack().executionTime;
				rand.setSeed(player.ticksExisted + current * 100000L);
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.3 + rand.nextFloat() * 0.6F, -0.25 + rand.nextFloat() * 0.1F, -0.25 - partialTicks * 0.3);
				GlStateManager.rotate(135, 0, 1, 0);
				GlStateManager.rotate(90, 1, 0, 0);
				Minecraft.getMinecraft().getRenderItem().renderItem(is, player, TransformType.GROUND, true);
				GlStateManager.popMatrix();
				return true;
			}
		}
		
		return false;
	}
}
