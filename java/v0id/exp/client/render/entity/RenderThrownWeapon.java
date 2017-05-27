package v0id.exp.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import v0id.exp.entity.EntityThrownWeapon;

public class RenderThrownWeapon extends Render<EntityThrownWeapon>
{
	public final RenderItem renderer;
	
	public RenderThrownWeapon(RenderManager renderManager)
	{
		super(renderManager);
		this.renderer = Minecraft.getMinecraft().getRenderItem();
	}

	@Override
	public void doRender(EntityThrownWeapon entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x, (float)y + (entity.isInGround() ? 0.5 : 0), (float)z);
		GlStateManager.rotate(90 + entity.rotationYaw, 0, 1, 0);
		GlStateManager.rotate(160, 0, 0, 1);
		
		this.renderer.renderItem(entity.getRenderStack(), TransformType.GROUND);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityThrownWeapon entity)
	{
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
