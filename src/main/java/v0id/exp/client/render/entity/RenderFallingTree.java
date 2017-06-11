package v0id.exp.client.render.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import v0id.exp.entity.EntityFallingTree;

import java.util.Map;

public class RenderFallingTree extends Render<EntityFallingTree>
{
	public RenderFallingTree(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}

	@Override
	public void doRender(EntityFallingTree entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate(x, y, z);       
        GlStateManager.rotate(180 - entity.fallAngle, 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        float prevFallProgress = (float) (entity.fallProgress / 1.1);
        GlStateManager.rotate(prevFallProgress + (entity.fallProgress - prevFallProgress) * partialTicks, 1, 0, 0);
		Tessellator tec = Tessellator.getInstance();
		BufferBuilder vb = tec.getBuffer();
		Minecraft.getMinecraft().renderEngine.bindTexture(this.getEntityTexture(entity));
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		
		for (Map.Entry<BlockPos, IBlockState> data : entity.data.entrySet())
		{
			BlockPos at = data.getKey().subtract(entity.getPosition());
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(data.getValue(), at, entity.world, vb);
		}
		
		tec.draw();
		
		GlStateManager.enableLighting();
        GlStateManager.popMatrix();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityFallingTree entity)
	{
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
	
	@Override
	public boolean shouldRender(EntityFallingTree livingEntity, ICamera camera, double camX, double camY, double camZ)
    {
		return true;
    }
}
