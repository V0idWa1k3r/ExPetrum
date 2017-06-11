package v0id.exp.client.fx;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import v0id.api.exp.fx.EnumParticle;
import v0id.api.exp.fx.IParticle;

/**
 * Created by V0idWa1k3r on 11-Jun-17.
 */
public class Particle implements IParticle
{
    public World worldIn;
    public float x, y, z;
    public EnumParticle type;
    public int lifetime;
    public float scale;
    public float r, g, b, a;
    public float mx, my, mz;
    public boolean noclip, nogravity, nolmap;
    public short lmapx, lmapy;

    @Override
    public BlockRenderLayer getRenderTarget()
    {
        return this.type.getLayer();
    }

    @Override
    public void onTick()
    {
        --this.lifetime;
    }

    @Override
    public void draw(BufferBuilder buffer, float partialTicks)
    {

    }

    @Override
    public boolean isInvalid()
    {
        return this.lifetime <= 0;
    }

    @Override
    public EnumParticle getType()
    {
        return this.type;
    }
}
