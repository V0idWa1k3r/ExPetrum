package v0id.exp.client.render.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import v0id.api.exp.data.ExPWeaponAttacks;
import v0id.api.exp.player.IExPPlayer;

public class SpecialAttackRenderer
{
	public static boolean render(float partialTicks)
	{
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
		}
		
		return false;
	}
}
