package v0id.api.exp.fx;

import net.minecraft.world.World;

/**
 * Created by V0idWa1k3r on 11-Jun-17.
 */
@FunctionalInterface
public interface IParticleFactory
{
    /**
     * Creates a particle object for given data values.
     * @param w - the world the particle is in
     * @param positionMotion - float array[6] that contains in the following order: x, y, z, motionX, motionY, motionZ.
     * @param color - float array[4] that contains in the following order: red, green, blue, alpha;
     * @param flags - bitmask of particle properties left to right: noclip, nogravity, nolightmap.
     * @param lifetime - the amount of ticks this particle will exist.
     * @param scale - the scale of this particle.
     * @param lmap - short array[2] that contains in the following order: lightmapx, lightmapy. Only has an effect if nolightmap flag is true.
     * @return: The particle object constructed from the data passed.
     */
    IParticle createParticle(World w, float[] positionMotion, float[] color, byte flags, int lifetime, float scale, short[] lmap);
}
