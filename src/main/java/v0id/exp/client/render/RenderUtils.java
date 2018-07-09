package v0id.exp.client.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import v0id.api.exp.client.model.WavefrontObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class RenderUtils
{
    /*
     * Normally I would have done all this in a vertex shader...
     */
    public static Vector4f mulVecByMatrix(Vector4f vec, Matrix4f mat)
    {
        float x = vec.getX() * mat.m00 + vec.getY() * mat.m10 + vec.getZ() * mat.m20 + vec.getW() * mat.m30;
        float y = vec.getX() * mat.m01 + vec.getY() * mat.m11 + vec.getZ() * mat.m21 + vec.getW() * mat.m31;
        float z = vec.getX() * mat.m02 + vec.getY() * mat.m12 + vec.getZ() * mat.m22 + vec.getW() * mat.m32;
        float w = vec.getX() * mat.m03 + vec.getY() * mat.m13 + vec.getZ() * mat.m23 + vec.getW() * mat.m33;
        return new Vector4f(x, y, z, w);
    }

    public static void renderCube(BufferBuilder buffer, Matrix4f transform, Vector3f pos, float[] color, int[] lightmap, Function<EnumFacing, TextureAtlasSprite> textureGetter)
    {
        TextureAtlasSprite top = textureGetter.apply(EnumFacing.UP);
        TextureAtlasSprite down = textureGetter.apply(EnumFacing.DOWN);
        TextureAtlasSprite west = textureGetter.apply(EnumFacing.WEST);
        TextureAtlasSprite east = textureGetter.apply(EnumFacing.EAST);
        TextureAtlasSprite north = textureGetter.apply(EnumFacing.NORTH);
        TextureAtlasSprite south = textureGetter.apply(EnumFacing.SOUTH);
        Vector4f top_front_left = mulVecByMatrix(new Vector4f(-0.5F, 0.5F, -0.5F, 1), transform);
        Vector4f top_front_right = mulVecByMatrix(new Vector4f(0.5F, 0.5F, -0.5F, 1), transform);
        Vector4f top_back_right = mulVecByMatrix(new Vector4f(0.5F, 0.5F, 0.5F, 1), transform);
        Vector4f top_back_left = mulVecByMatrix(new Vector4f(-0.5F, 0.5F, 0.5F, 1), transform);
        Vector4f btm_front_left = mulVecByMatrix(new Vector4f(-0.5F, -0.5F, -0.5F, 1), transform);
        Vector4f btm_front_right = mulVecByMatrix(new Vector4f(0.5F, -0.5F, -0.5F, 1), transform);
        Vector4f btm_back_right = mulVecByMatrix(new Vector4f(0.5F, -0.5F, 0.5F, 1), transform);
        Vector4f btm_back_left = mulVecByMatrix(new Vector4f(-0.5F, -0.5F, 0.5F, 1), transform);
        buffer.pos(pos.getX() + top_front_right.getX(), pos.getY() + top_front_right.getY(), pos.getZ() + top_front_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(top.getMinU(), top.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + top_front_left.getX(), pos.getY() + top_front_left.getY(), pos.getZ() + top_front_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(top.getMaxU(), top.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + top_back_left.getX(), pos.getY() + top_back_left.getY(), pos.getZ() + top_back_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(top.getMaxU(), top.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + top_back_right.getX(), pos.getY() + top_back_right.getY(), pos.getZ() + top_back_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(top.getMinU(), top.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_front_right.getX(), pos.getY() + btm_front_right.getY(), pos.getZ() + btm_front_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(down.getMaxU(), down.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_front_left.getX(), pos.getY() + btm_front_left.getY(), pos.getZ() + btm_front_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(down.getMinU(), down.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_back_left.getX(), pos.getY() + btm_back_left.getY(), pos.getZ() + btm_back_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(down.getMinU(), down.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_back_right.getX(), pos.getY() + btm_back_right.getY(), pos.getZ() + btm_back_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(down.getMaxU(), down.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + top_front_left.getX(), pos.getY() + top_front_left.getY(), pos.getZ() + top_front_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(north.getMinU(), north.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + top_front_right.getX(), pos.getY() + top_front_right.getY(), pos.getZ() + top_front_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(north.getMaxU(), north.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_front_right.getX(), pos.getY() + btm_front_right.getY(), pos.getZ() + btm_front_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(north.getMaxU(), north.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_front_left.getX(), pos.getY() + btm_front_left.getY(), pos.getZ() + btm_front_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(north.getMinU(), north.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + top_back_left.getX(), pos.getY() + top_back_left.getY(), pos.getZ() + top_back_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(south.getMaxU(), south.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + top_back_right.getX(), pos.getY() + top_back_right.getY(), pos.getZ() + top_back_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(south.getMinU(), south.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_back_right.getX(), pos.getY() + btm_back_right.getY(), pos.getZ() + btm_back_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(south.getMinU(), south.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_back_left.getX(), pos.getY() + btm_back_left.getY(), pos.getZ() + btm_back_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(south.getMaxU(), south.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + top_front_left.getX(), pos.getY() + top_front_left.getY(), pos.getZ() + top_front_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(west.getMinU(), west.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + top_back_left.getX(), pos.getY() + top_back_left.getY(), pos.getZ() + top_back_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(west.getMaxU(), west.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_back_left.getX(), pos.getY() + btm_back_left.getY(), pos.getZ() + btm_back_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(west.getMaxU(), west.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_front_left.getX(), pos.getY() + btm_front_left.getY(), pos.getZ() + btm_front_left.getZ()).color(color[0], color[1], color[2], color[3]).tex(west.getMinU(), west.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + top_front_right.getX(), pos.getY() + top_front_right.getY(), pos.getZ() + top_front_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(east.getMaxU(), east.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + top_back_right.getX(), pos.getY() + top_back_right.getY(), pos.getZ() + top_back_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(east.getMinU(), east.getMinV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_back_right.getX(), pos.getY() + btm_back_right.getY(), pos.getZ() + btm_back_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(east.getMinU(), east.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
        buffer.pos(pos.getX() + btm_front_right.getX(), pos.getY() + btm_front_right.getY(), pos.getZ() + btm_front_right.getZ()).color(color[0], color[1], color[2], color[3]).tex(east.getMaxU(), east.getMaxV()).lightmap(lightmap[0], lightmap[1]).endVertex();
    }

    public static void renderObj(BufferBuilder buffer, WavefrontObject model, Vector3f pos, Matrix4f transform, float[] color, int[] lightmap, Supplier<TextureAtlasSprite> textureGetter)
    {
        TextureAtlasSprite tex = textureGetter.get();
        for (WavefrontObject.Vertex vertex : model.getVertices())
        {
            Vector4f loc = mulVecByMatrix(new Vector4f(vertex.position.getX(), vertex.position.getY(), vertex.position.getZ(), 1), transform);
            float minU = tex.getMinU();
            float maxU = tex.getMaxU();
            float minV = tex.getMinV();
            float maxV = tex.getMaxV();
            float diffU = (maxU - minU) % 1;
            float diffV = (maxV - minV) % 1;
            float u = minU + vertex.uvset.getX() * diffU;
            float v = minV + (1 - vertex.uvset.getY()) * diffV;
            buffer.pos(pos.getX() + loc.getX(), pos.getY() + loc.getY(), pos.getZ() + loc.getZ()).color(color[0], color[1], color[2], color[3]).tex(u, v).lightmap(lightmap[0], lightmap[1]).endVertex();
        }
    }
}
