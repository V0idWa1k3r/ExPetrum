package v0id.exp.client.render.sky;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.util.Helpers;
import v0id.exp.world.ExPWorld;

public class WorldWeatherRenderer extends IRenderHandler
{
	private static WorldWeatherRenderer instance;

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc)
	{
		if (((ExPWorld)IExPWorld.of(world)).rainTicksRemaining <= 0)
		{
			return;
		}
		
		GlStateManager.depthMask(true);
		Vec3d offset = IExPWorld.of(world).getWindDirection();
		offset = offset.scale(IExPWorld.of(world).getWindStrength() / 3);
		float wStr = IExPWorld.of(world).getWindStrength();
		Random rand = new Random();
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(516, 0.1F);
		mc.renderEngine.bindTexture(ExPTextures.WEATHER);
		Entity entity = mc.getRenderViewEntity();
		double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        BlockPos pos = entity.getPosition();
		BufferBuilder vb = Tessellator.getInstance().getBuffer();
		vb.setTranslation(-d0, -d1, -d2);
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		for (int dx = -8; dx <= 8; ++dx)
		{
			for (int dz = -8; dz <= 8; ++dz)
			{
				rand.setSeed(MathHelper.getCoordinateRandom(x + dx, 0, z + dz));
				int py = world.getPrecipitationHeight(pos.add(dx, 0, dz)).getY();
				py = Math.max(py, y - 8);
				double offsetYTex = rand.nextDouble() + (float)(world.getWorldTime() % 10) / 10 + partialTicks / 10;
				BlockPos renderedAt = new BlockPos(x + dx, py, z + dz);
				double offsetXTex = Helpers.getTemperatureAt(world, renderedAt) < 0 ? 0.5 : 0;
				float rOffX = rand.nextFloat() / 10;
				float rOffZ = rand.nextFloat() / 10;
				
				if (offsetXTex == 0.5)
				{
					float texOf = Math.max(2, 55 - wStr);
					offsetYTex = rand.nextDouble() + (float)(world.getWorldTime() % texOf) / texOf + partialTicks / texOf;
				}
				
				int j3 = world.getCombinedLight(renderedAt, 0);
                int k3 = j3 >> 16 & 65535;
                int l3 = j3 & 65535;
				vb.pos(rOffX + x + 1 + dx - offset.x, py + 16, rOffZ + z + dz + 0.5 - offset.z).tex(offsetXTex, offsetYTex + 4).color(1, 1, 1, 0.5F).lightmap(k3, l3).endVertex();
				vb.pos(rOffX + x + dx - offset.x, py + 16, rOffZ + z + dz + 0.5 - offset.z).tex(offsetXTex + 0.5, offsetYTex + 4).color(1, 1, 1, 0.5F).lightmap(k3, l3).endVertex();
				vb.pos(rOffX + x + dx, py, rOffZ + z + dz + 0.5).tex(offsetXTex + 0.5, offsetYTex).color(1, 1, 1, 0.5F).lightmap(k3, l3).endVertex();
				vb.pos(rOffX + x + 1 + dx, py, rOffZ + z + dz + 0.5).tex(offsetXTex, offsetYTex).color(1, 1, 1, 0.5F).lightmap(k3, l3).endVertex();
				vb.pos(rOffX + x + 0.5 + dx - offset.x, py + 16, rOffZ + z + dz + 1 - offset.z).tex(offsetXTex, offsetYTex + 4).color(1, 1, 1, 0.5F).lightmap(k3, l3).endVertex();
				vb.pos(rOffX + x + 0.5 + dx - offset.x, py + 16, rOffZ + z + dz - offset.z).tex(offsetXTex + 0.5, offsetYTex + 4).color(1, 1, 1, 0.5F).lightmap(k3, l3).endVertex();
				vb.pos(rOffX + x + 0.5 + dx , py, rOffZ + z + dz).tex(offsetXTex + 0.5, offsetYTex).color(1, 1, 1, 0.5F).lightmap(k3, l3).endVertex();
				vb.pos(rOffX + x + 0.5 + dx, py, z + rOffZ + dz + 1).tex(offsetXTex, offsetYTex).color(1, 1, 1, 0.5F).lightmap(k3, l3).endVertex();
			}
		}
		
		Tessellator.getInstance().draw();
		vb.setTranslation(0, 0, 0);
		GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.depthMask(false);
	}
	
	public static WorldWeatherRenderer getInstance()
	{
		return instance == null ? instance = new WorldWeatherRenderer() : instance;
	}
}
