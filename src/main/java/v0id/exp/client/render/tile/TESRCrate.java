package v0id.exp.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.animation.FastTESR;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import v0id.api.exp.data.ExPBlocks;
import v0id.exp.client.render.RenderUtils;
import v0id.exp.tile.TileCrate;

import java.util.function.BiFunction;


public class TESRCrate extends FastTESR<TileCrate>
{
    @Override
    public void renderTileEntityFast(TileCrate te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        if (te.getWorld().getBlockState(te.getPos()).getBlock() == ExPBlocks.crate)
        {
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(te.getWorld().getBlockState(te.getPos()).getActualState(te.getWorld(), te.getPos()));
            Vector3f pos = new Vector3f((float) x + 0.5F, (float) y + 0.96875F, (float) z);
            Matrix4f transform = Matrix4f.setIdentity(new Matrix4f());
            EnumFacing facing = te.direction;
            if (facing == EnumFacing.NORTH)
            {
                transform = transform.rotate((float) Math.toRadians((te.lidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks) * -45F), new Vector3f(1, 0, 0));
                transform = transform.translate(new Vector3f(0, 0, 0.5F));
                transform = transform.scale(new Vector3f(1, 0.0625F, 1));
            }

            if (facing == EnumFacing.SOUTH)
            {
                transform = transform.translate(new Vector3f(0, 0, 1F));
                transform = transform.rotate((float) Math.toRadians((te.lidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks) * 45F), new Vector3f(1, 0, 0));
                transform = transform.rotate((float) Math.PI, new Vector3f(0, 1, 0));
                transform = transform.translate(new Vector3f(0, 0, 0.5F));
                transform = transform.scale(new Vector3f(1, 0.0625F, 1));
            }

            if (facing == EnumFacing.EAST)
            {
                transform = transform.translate(new Vector3f(0.5F, 0, 0.5F));
                transform = transform.rotate((float) Math.toRadians((te.lidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks) * -45F), new Vector3f(0, 0, 1));
                transform = transform.translate(new Vector3f(-0.5F, 0, 0));
                transform = transform.scale(new Vector3f(1, 0.0625F, 1));
            }

            if (facing == EnumFacing.WEST)
            {
                transform = transform.translate(new Vector3f(-0.5F, 0, 0.5F));
                transform = transform.rotate((float) Math.toRadians((te.lidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks) * 45F), new Vector3f(0, 0, 1));
                transform = transform.translate(new Vector3f(0.5F, 0, 0));
                transform = transform.scale(new Vector3f(1, 0.0625F, 1));
            }

            BiFunction<EnumFacing, TextureAtlasSprite, Vector4f> textureInterpolator = (facing1, textureAtlasSprite) -> facing1.getAxis() == EnumFacing.Axis.Y ? new Vector4f(textureAtlasSprite.getMinU(), textureAtlasSprite.getMinV(), textureAtlasSprite.getMaxU(), textureAtlasSprite.getMaxV()) : new Vector4f(textureAtlasSprite.getMinU(), textureAtlasSprite.getMinV() + (textureAtlasSprite.getMaxV() - textureAtlasSprite.getMinV()) * 0.9375F, textureAtlasSprite.getMaxU(), textureAtlasSprite.getMaxV());

            int i = te.getWorld().getCombinedLight(te.getPos(), 0);
            int j = i % 65536;
            int k = i / 65536;
            RenderUtils.renderCube(buffer, transform, pos, new float[]{1, 1, 1, 1}, new int[]{k, j}, side -> sprite, textureInterpolator);
        }
    }
}
