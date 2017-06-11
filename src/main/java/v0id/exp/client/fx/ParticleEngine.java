package v0id.exp.client.fx;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.BlockRenderLayer;
import org.lwjgl.opengl.GL11;
import v0id.api.exp.fx.EnumParticle;
import v0id.api.exp.fx.IParticle;
import v0id.api.exp.fx.IParticleEngine;
import v0id.api.exp.fx.IParticleFactory;

import java.util.Map;
import java.util.Set;

/**
 * Created by V0idWa1k3r on 11-Jun-17.
 */
public class ParticleEngine implements IParticleEngine
{
    public static final Map<BlockRenderLayer, Set<IParticle>> particles = Maps.newConcurrentMap();
    public static final Map<EnumParticle, IParticleFactory> factories = Maps.newEnumMap(EnumParticle.class);

    static
    {
        particles.put(BlockRenderLayer.CUTOUT, Sets.newConcurrentHashSet());
        particles.put(BlockRenderLayer.SOLID, Sets.newConcurrentHashSet());
        particles.put(BlockRenderLayer.TRANSLUCENT, Sets.newConcurrentHashSet());
    }

    public static void onWorldChanged()
    {
        particles.values().forEach(Set::clear);
    }

    @Override
    public void spawnParticle(IParticle particle)
    {
        particles.get(particle.getRenderTarget() == BlockRenderLayer.CUTOUT_MIPPED ? BlockRenderLayer.CUTOUT : particle.getRenderTarget()).add(particle);
    }

    @Override
    public Set<IParticle> getParticlesFor(BlockRenderLayer layer)
    {
        return particles.get(layer);
    }

    @Override
    public void renderTick(float partialTicks)
    {
        BufferBuilder bb = Tessellator.getInstance().getBuffer();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        bb.begin(GL11.GL_QUADS, this.getDrawFormat());
        particles.get(BlockRenderLayer.SOLID).forEach(p -> p.draw(bb, partialTicks));
        Tessellator.getInstance().draw();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        bb.begin(GL11.GL_QUADS, this.getDrawFormat());
        particles.get(BlockRenderLayer.CUTOUT).forEach(p -> p.draw(bb, partialTicks));
        Tessellator.getInstance().draw();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        bb.begin(GL11.GL_QUADS, this.getDrawFormat());
        particles.get(BlockRenderLayer.TRANSLUCENT).forEach(p -> p.draw(bb, partialTicks));
        Tessellator.getInstance().draw();
    }

    @Override
    public void tick()
    {
        particles.values().forEach(s ->
        {
            s.forEach(IParticle::onTick);
            s.removeIf(IParticle::isInvalid);
        });
    }

    @Override
    public IParticleFactory getFactoryFor(EnumParticle type)
    {
        return factories.get(type);
    }
}
