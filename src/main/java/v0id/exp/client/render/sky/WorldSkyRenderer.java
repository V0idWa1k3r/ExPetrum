package v0id.exp.client.render.sky;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IRenderHandler;

public class WorldSkyRenderer extends IRenderHandler
{
	private static final ResourceLocation SUN = new ResourceLocation("exp", "textures/skybox/sun.png");
	private static final ResourceLocation MOON = new ResourceLocation("exp", "textures/skybox/moon.png");
	public static WorldSkyRenderer instance;
	public ResourceLocation[] daySkyboxes = new ResourceLocation[]{
		new ResourceLocation("exp", "textures/skybox/bluecloud_up.png"),
		new ResourceLocation("exp", "textures/skybox/bluecloud_dn.png"),
		new ResourceLocation("exp", "textures/skybox/bluecloud_ft.png"),
		new ResourceLocation("exp", "textures/skybox/bluecloud_bk.png"),
		new ResourceLocation("exp", "textures/skybox/bluecloud_lf.png"),
		new ResourceLocation("exp", "textures/skybox/bluecloud_rt.png"),
	};
	
	public ResourceLocation[] nightSkyboxes = new ResourceLocation[]{
			new ResourceLocation("exp", "textures/skybox/nightstars_up.png"),
			new ResourceLocation("exp", "textures/skybox/nightstars_dn.png"),
			new ResourceLocation("exp", "textures/skybox/nightstars_ft.png"),
			new ResourceLocation("exp", "textures/skybox/nightstars_bk.png"),
			new ResourceLocation("exp", "textures/skybox/nightstars_lf.png"),
			new ResourceLocation("exp", "textures/skybox/nightstars_rt.png"),
		};
	
	public static WorldSkyRenderer getInstance()
	{
		return instance == null ? instance = new WorldSkyRenderer() : instance;
	}
	
	public void renderSkybox(ResourceLocation[] skyboxes, float renderdistancemul)
	{
		Minecraft mc = Minecraft.getMinecraft();
		float renderDistance = mc.gameSettings.renderDistanceChunks * 16 * renderdistancemul;
		Tessellator tec = Tessellator.getInstance();
		mc.renderEngine.bindTexture(skyboxes[1]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(renderDistance, renderDistance, renderDistance).tex(1, 0).endVertex();
		tec.getBuffer().pos(-renderDistance, renderDistance, renderDistance).tex(1, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, renderDistance, -renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(renderDistance, renderDistance, -renderDistance).tex(0, 0).endVertex();
		tec.draw();
		mc.renderEngine.bindTexture(skyboxes[3]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(-renderDistance, renderDistance, -renderDistance).tex(1, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, renderDistance, renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, -renderDistance, renderDistance).tex(0, 0).endVertex();
		tec.getBuffer().pos(-renderDistance, -renderDistance, -renderDistance).tex(1, 0).endVertex();
		tec.draw();
		mc.renderEngine.bindTexture(skyboxes[2]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(renderDistance, renderDistance, renderDistance).tex(1, 1).endVertex();
		tec.getBuffer().pos(renderDistance, renderDistance, -renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, -renderDistance).tex(0, 0).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, renderDistance).tex(1, 0).endVertex();
		tec.draw();
		mc.renderEngine.bindTexture(skyboxes[4]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(renderDistance, renderDistance, -renderDistance).tex(1, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, renderDistance, -renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, -renderDistance, -renderDistance).tex(0, 0).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, -renderDistance).tex(1, 0).endVertex();
		tec.draw();
		mc.renderEngine.bindTexture(skyboxes[5]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(-renderDistance, renderDistance, renderDistance).tex(1, 1).endVertex();
		tec.getBuffer().pos(renderDistance, renderDistance, renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, renderDistance).tex(0, 0).endVertex();
		tec.getBuffer().pos(-renderDistance, -renderDistance, renderDistance).tex(1, 0).endVertex();
		tec.draw();
		mc.renderEngine.bindTexture(skyboxes[0]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(-renderDistance, -renderDistance, renderDistance).tex(1, 0).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, renderDistance).tex(1, 1).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, -renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, -renderDistance, -renderDistance).tex(0, 0).endVertex();
		tec.draw();
	}
	
	public void renderDaySkybox(ResourceLocation[] skyboxes, float renderdistancemul)
	{
		Minecraft mc = Minecraft.getMinecraft();
		float renderDistance = mc.gameSettings.renderDistanceChunks * 16 * renderdistancemul;
		Tessellator tec = Tessellator.getInstance();
		mc.renderEngine.bindTexture(skyboxes[0]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(renderDistance, renderDistance, renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, renderDistance, renderDistance).tex(1, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, renderDistance, -renderDistance).tex(1, 0).endVertex();
		tec.getBuffer().pos(renderDistance, renderDistance, -renderDistance).tex(0, 0).endVertex();
		tec.draw();
		mc.renderEngine.bindTexture(skyboxes[2]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(-renderDistance, renderDistance, -renderDistance).tex(1, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, renderDistance, renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, -renderDistance, renderDistance).tex(0, 0).endVertex();
		tec.getBuffer().pos(-renderDistance, -renderDistance, -renderDistance).tex(1, 0).endVertex();
		tec.draw();
		mc.renderEngine.bindTexture(skyboxes[3]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(renderDistance, renderDistance, renderDistance).tex(1, 1).endVertex();
		tec.getBuffer().pos(renderDistance, renderDistance, -renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, -renderDistance).tex(0, 0).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, renderDistance).tex(1, 0).endVertex();
		tec.draw();
		mc.renderEngine.bindTexture(skyboxes[4]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(renderDistance, renderDistance, -renderDistance).tex(1, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, renderDistance, -renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, -renderDistance, -renderDistance).tex(0, 0).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, -renderDistance).tex(1, 0).endVertex();
		tec.draw();
		mc.renderEngine.bindTexture(skyboxes[5]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(-renderDistance, renderDistance, renderDistance).tex(1, 1).endVertex();
		tec.getBuffer().pos(renderDistance, renderDistance, renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, renderDistance).tex(0, 0).endVertex();
		tec.getBuffer().pos(-renderDistance, -renderDistance, renderDistance).tex(1, 0).endVertex();
		tec.draw();
		mc.renderEngine.bindTexture(skyboxes[1]);
		tec.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tec.getBuffer().pos(-renderDistance, -renderDistance, renderDistance).tex(1, 0).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, renderDistance).tex(0, 0).endVertex();
		tec.getBuffer().pos(renderDistance, -renderDistance, -renderDistance).tex(0, 1).endVertex();
		tec.getBuffer().pos(-renderDistance, -renderDistance, -renderDistance).tex(1, 1).endVertex();
		tec.draw();
	}
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) 
	{
		float renderDistance = mc.gameSettings.renderDistanceChunks * 16;
		float color = world.provider.getSunBrightness(partialTicks);
		color = (color - 0.2f) / 0.8f;
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.01f);
		
		GlStateManager.pushMatrix();
		float f16 = 1.0F - world.getRainStrength(partialTicks);
        GlStateManager.color(1.0F, 1.0F, 1.0F, f16);
        GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		mc.renderEngine.bindTexture(SUN);
		float f17 = 30.0F;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(-f17), renderDistance * 1.5f, (double)(-f17)).tex(0.0D, 0.0D).endVertex();
        vertexbuffer.pos((double)f17, renderDistance * 1.5f, (double)(-f17)).tex(0.5D, 0.0D).endVertex();
        vertexbuffer.pos((double)f17, renderDistance * 1.5f, (double)f17).tex(0.5D, 0.5D).endVertex();
        vertexbuffer.pos((double)(-f17), renderDistance* 1.5f, (double)f17).tex(0.0D, 0.5D).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.rotate(16, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)world.getWorldTime() / 64923F * 360.0F, 1.0F, 0.0F, 0.0F);
        
        float sunIndex = world.getWorldTime() % 24000;
        if (sunIndex > 2000 && sunIndex < 10000)
        {
        	sunIndex = 0;
        }
        else
        {
        	if (sunIndex >= 22000 || sunIndex <= 2000)
        	{
        		sunIndex = sunIndex >= 22000 ? (24000 - sunIndex) / 4000 : 0.5F - (2000 - sunIndex) / 4000;
        	}
        	else
        	{
        		if (sunIndex >= 10000 && sunIndex <= 14000)
        		{
        			sunIndex = 1F - (14000 - sunIndex) / 4000;
        		}
        		else
        		{
        			sunIndex = 1;
        		}
        	}
        }
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, Math.min(f16, sunIndex));
        f17 = 10.0F;
        mc.renderEngine.bindTexture(MOON);
        int i = world.getMoonPhase();
        int k = i % 4;
        int i1 = i / 4 % 2;
        float f22 = (float)(k + 0) / 4.0F;
        float f23 = (float)(i1 + 0) / 4.0F;
        float f24 = (float)(k + 1) / 4.0F;
        float f14 = (float)(i1 + 1) / 4.0F;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(-f17), -renderDistance * 1.5f, (double)f17).tex((double)f24, (double)f14).endVertex();
        vertexbuffer.pos((double)f17, -renderDistance * 1.5f, (double)f17).tex((double)f22, (double)f14).endVertex();
        vertexbuffer.pos((double)f17, -renderDistance * 1.5f, (double)(-f17)).tex((double)f22, (double)f23).endVertex();
        vertexbuffer.pos((double)(-f17), -renderDistance * 1.5f, (double)(-f17)).tex((double)f24, (double)f23).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        
        if (world.getWorldTime() % 24000L < 12000L)
        {
        	GL11.glColor4f(1, 1, 1, 0);
        }
        else
        {
        	GL11.glColor4f(1, 1, 1, 1 - color);
        }
        
		GlStateManager.pushMatrix();
		GlStateManager.rotate(((float)Minecraft.getMinecraft().world.getWorldTime() + partialTicks) / 100, 0, 1, 0);
		this.renderSkybox(this.nightSkyboxes, 1.1f);
		GL11.glColor4f(color, color, color, 1);
		this.renderDaySkybox(this.daySkyboxes, 1.0f);
		GlStateManager.popMatrix();
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
}
