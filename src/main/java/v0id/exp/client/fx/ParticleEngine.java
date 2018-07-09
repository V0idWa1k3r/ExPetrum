package v0id.exp.client.fx;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.client.EnumParticle;
import v0id.api.exp.client.IParticle;
import v0id.api.exp.client.IParticleEngine;
import v0id.api.exp.client.IParticleFactory;

import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by V0idWa1k3r on 11-Jun-17.
 */
public class ParticleEngine implements IParticleEngine
{
    public static final Map<BlockRenderLayer, Set<IParticle>> particles = Maps.newConcurrentMap();
    public static final Map<EnumParticle, IParticleFactory> factories = Maps.newEnumMap(EnumParticle.class);
    public static final Random RANDOM = new Random();

    static
    {
        particles.put(BlockRenderLayer.CUTOUT, Sets.newConcurrentHashSet());
        particles.put(BlockRenderLayer.SOLID, Sets.newConcurrentHashSet());
        particles.put(BlockRenderLayer.TRANSLUCENT, Sets.newConcurrentHashSet());
        factories.put(EnumParticle.LEAF, ParticleEngine::spawnParticleLeaf);
    }

    public static void onWorldChanged()
    {
        particles.values().forEach(Set::clear);
    }

    @Override
    public void spawnParticle(IParticle particle)
    {
        Set<IParticle> set = particles.get(particle.getRenderTarget() == BlockRenderLayer.CUTOUT_MIPPED ? BlockRenderLayer.CUTOUT : particle.getRenderTarget());
        if (checkParticleSettings(set.size()))
        {
            if (allowParticleIn())
            {
                set.add(particle);
            }
        }
    }

    public static boolean allowParticleIn()
    {
        int particles = Minecraft.getMinecraft().gameSettings.particleSetting;
        return particles == 0 || (particles == 1 && RANDOM.nextBoolean());
    }

    public static boolean checkParticleSettings(int current)
    {
        int particles = Minecraft.getMinecraft().gameSettings.particleSetting;
        int max = particles == 0 ? 16000 : particles == 1 ? 4000 : 0;
        return current < max;
    }

    @Override
    public Set<IParticle> getParticlesFor(BlockRenderLayer layer)
    {
        return particles.get(layer);
    }

    @Override
    public void renderTick(float partialTicks)
    {
        Minecraft.getMinecraft().mcProfiler.startSection("expParticleDraw");
        Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.PARTICLES);
        BufferBuilder bb = Tessellator.getInstance().getBuffer();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        bb.begin(GL11.GL_QUADS, this.getDrawFormat());
        particles.get(BlockRenderLayer.SOLID).forEach(p -> p.draw(bb, partialTicks, ActiveRenderInfo.getRotationX(), ActiveRenderInfo.getRotationXZ(), ActiveRenderInfo.getRotationZ(), ActiveRenderInfo.getRotationYZ(), ActiveRenderInfo.getRotationXY()));
        Tessellator.getInstance().draw();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        bb.begin(GL11.GL_QUADS, this.getDrawFormat());
        particles.get(BlockRenderLayer.CUTOUT).forEach(p -> p.draw(bb, partialTicks, ActiveRenderInfo.getRotationX(), ActiveRenderInfo.getRotationXZ(), ActiveRenderInfo.getRotationZ(), ActiveRenderInfo.getRotationYZ(), ActiveRenderInfo.getRotationXY()));
        Tessellator.getInstance().draw();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        bb.begin(GL11.GL_QUADS, this.getDrawFormat());
        particles.get(BlockRenderLayer.TRANSLUCENT).forEach(p -> p.draw(bb, partialTicks, ActiveRenderInfo.getRotationX(), ActiveRenderInfo.getRotationXZ(), ActiveRenderInfo.getRotationZ(), ActiveRenderInfo.getRotationYZ(), ActiveRenderInfo.getRotationXY()));
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    public void tick()
    {
        Minecraft.getMinecraft().mcProfiler.startSection("expParticleTick");
        particles.values().forEach(s ->
        {
            s.forEach(IParticle::onTick);
            s.removeIf(IParticle::isInvalid);
        });

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    public IParticleFactory getFactoryFor(EnumParticle type)
    {
        return factories.get(type);
    }

    public static IParticle spawnParticleLeaf(World w, float[] positionMotion, float[] color, byte flags, int lifetime, float scale, short[] lmap)
    {
        return new ParticleLeaf(w, positionMotion[0], positionMotion[1], positionMotion[2], EnumParticle.LEAF, lifetime, scale, color[0], color[1], color[2], color[3]);
    }
}
