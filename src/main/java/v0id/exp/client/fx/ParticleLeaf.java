package v0id.exp.client.fx;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import v0id.api.exp.fx.EnumParticle;
import v0id.api.exp.world.IExPWorld;

/**
 * Created by V0idWa1k3r on 11-Jun-17.
 */
public class ParticleLeaf extends Particle
{
    public ParticleLeaf(World worldIn, float x, float y, float z, EnumParticle type, int lifetime, float scale, float r, float g, float b, float a)
    {
        super(worldIn, x, y, z, type, lifetime, scale, r, g, b, a, 0, 0, 0, false, false, false, (short)0, (short)0);
    }

    @Override
    public void onTick()
    {
        --this.lifetime;
        this.px = this.x;
        this.py = this.y;
        this.pz = this.z;
        IExPWorld wrld = IExPWorld.of(this.worldIn);
        Vec3d dir = wrld.getWindDirection().scale(wrld.getWindStrength());
        this.mx = (float) (dir.x / 300);
        this.my = -this.worldIn.rand.nextFloat() / 10;
        this.mz = (float) (dir.z / 300);
        this.x += this.mx;
        this.y += this.my;
        this.z += this.mz;
    }
}
