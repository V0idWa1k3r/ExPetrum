package v0id.api.exp.fx;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockRenderLayer;

import java.util.Set;

/**
 * Created by V0idWa1k3r on 11-Jun-17.
 */
public interface IParticleEngine
{
    void spawnParticle(IParticle particle);

    Set<IParticle> getParticlesFor(BlockRenderLayer layer);

    void renderTick(float partialTicks);

    void tick();

    default VertexFormat getDrawFormat()
    {
        return DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP;
    }

    IParticleFactory getFactoryFor(EnumParticle type);
}
