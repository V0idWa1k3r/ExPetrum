package v0id.api.exp.fx;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.BlockRenderLayer;

/**
 * Created by V0idWa1k3r on 11-Jun-17.
 */
public interface IParticle
{
    BlockRenderLayer getRenderTarget();

    void onTick();

    void draw(BufferBuilder buffer, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ);

    boolean isInvalid();

    EnumParticle getType();
}
