package v0id.exp.client.fx;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import v0id.api.exp.client.EnumParticle;
import v0id.api.exp.client.IParticle;

import java.awt.*;

/**
 * Created by V0idWa1k3r on 11-Jun-17.
 */
@SuppressWarnings("CanBeFinal")
public class Particle implements IParticle
{
    public final World worldIn;
    public float x, y, z;
    public float px, py, pz;
    public EnumParticle type;
    public int lifetime;
    public float scale;
    public float r, g, b, a;
    public float mx, my, mz;
    public boolean noclip, nogravity, nolmap;
    public short lmapx, lmapy;

    public Particle(World worldIn, float x, float y, float z, EnumParticle type, int lifetime, float scale, float r, float g, float b, float a, float mx, float my, float mz, boolean noclip, boolean nogravity, boolean nolmap, short lmapx, short lmapy)
    {
        this.worldIn = worldIn;
        this.x = this.px = x;
        this.y = this.py = y;
        this.z = this.pz = z;
        this.type = type;
        this.lifetime = lifetime;
        this.scale = scale;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.mx = mx;
        this.my = my;
        this.mz = mz;
        this.noclip = noclip;
        this.nogravity = nogravity;
        this.nolmap = nolmap;
        this.lmapx = lmapx;
        this.lmapy = lmapy;
    }

    @Override
    public BlockRenderLayer getRenderTarget()
    {
        return this.type.getLayer();
    }

    @Override
    public void onTick()
    {
        --this.lifetime;
        this.px = this.x;
        this.py = this.y;
        this.pz = this.z;
        if (this.noclip && this.nogravity)
        {
            this.x += this.mx;
            this.y += this.my;
            this.z += this.mz;
        }

        if (!this.nogravity)
        {
            this.mx *= 0.95;
            this.my *= 0.95;
            this.mz *= 0.95;
            if (this.isInsideBlock(new BlockPos(this.x + this.mx, this.y + this.my, this.z + this.mz)))
            {
                if (!this.noclip)
                {
                    this.my = 0;
                    this.mx *= 0.7;
                    this.mz *= 0.7;
                }
            }

            this.x += this.mx;
            this.y += this.my;
            this.z += this.mz;
        }
    }

    public boolean isInsideBlock(BlockPos pos)
    {
        return !worldIn.isAirBlock(pos);
    }

    @Override
    public void draw(BufferBuilder buffer, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        Rectangle renderRectangle = this.getType().getRenderRectangle();
        float minU = (float)renderRectangle.getX() / 128;
        float maxU = (float)(renderRectangle.getX() + renderRectangle.getWidth()) / 128;
        float minV = (float)renderRectangle.getY() / 128;
        float maxV = (float)(renderRectangle.getY() + renderRectangle.getHeight()) / 128;
        float renderX = (float)(this.px + (this.x - this.px) * (double)partialTicks - net.minecraft.client.particle.Particle.interpPosX);
        float renderY = (float)(this.py + (this.y - this.py) * (double)partialTicks - net.minecraft.client.particle.Particle.interpPosY);
        float renderZ = (float)(this.pz + (this.z - this.pz) * (double)partialTicks - net.minecraft.client.particle.Particle.interpPosZ);
        int renderLmapX;
        int renderLmapY;
        if (this.nolmap)
        {
            renderLmapX = this.lmapx;
            renderLmapY = this.lmapy;
        }
        else
        {
            int i = this.getBrightnessForRender(partialTicks);
            renderLmapX = i >> 16 & 65535;
            renderLmapY = i & 65535;
        }

        Vec3d[] vertexRenderMatrix = new Vec3d[] {new Vec3d((double)(-rotationX * this.scale - rotationXY * this.scale), (double)(-rotationZ * this.scale), (double)(-rotationYZ * this.scale - rotationXZ * this.scale)), new Vec3d((double)(-rotationX * this.scale + rotationXY * this.scale), (double)(rotationZ * this.scale), (double)(-rotationYZ * this.scale + rotationXZ * this.scale)), new Vec3d((double)(rotationX * this.scale + rotationXY * this.scale), (double)(rotationZ * this.scale), (double)(rotationYZ * this.scale + rotationXZ * this.scale)), new Vec3d((double)(rotationX * this.scale - rotationXY * this.scale), (double)(-rotationZ * this.scale), (double)(rotationYZ * this.scale - rotationXZ * this.scale))};
        buffer.pos(renderX + vertexRenderMatrix[0].x, renderY + vertexRenderMatrix[0].y, renderZ + vertexRenderMatrix[0].z).tex(maxU, maxV).color(this.r, this.g, this.b, this.a).lightmap(renderLmapX, renderLmapY).endVertex();
        buffer.pos(renderX + vertexRenderMatrix[1].x, renderY + vertexRenderMatrix[1].y, renderZ + vertexRenderMatrix[1].z).tex(maxU, minV).color(this.r, this.g, this.b, this.a).lightmap(renderLmapX, renderLmapY).endVertex();
        buffer.pos(renderX + vertexRenderMatrix[2].x, renderY + vertexRenderMatrix[2].y, renderZ + vertexRenderMatrix[2].z).tex(minU, minV).color(this.r, this.g, this.b, this.a).lightmap(renderLmapX, renderLmapY).endVertex();
        buffer.pos(renderX + vertexRenderMatrix[3].x, renderY + vertexRenderMatrix[3].y, renderZ + vertexRenderMatrix[3].z).tex(minU, maxV).color(this.r, this.g, this.b, this.a).lightmap(renderLmapX, renderLmapY).endVertex();
    }

    public int getBrightnessForRender(float partialTicks)
    {
        BlockPos blockpos = new BlockPos(this.x, this.y, this.z);
        return this.worldIn.isBlockLoaded(blockpos) ? this.worldIn.getCombinedLight(blockpos, 0) : 0;
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
