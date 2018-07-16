package v0id.exp.client.render.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.animation.FastTESR;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import v0id.api.exp.data.ExPBlocks;
import v0id.exp.client.render.RenderUtils;
import v0id.api.exp.client.model.WavefrontObject;
import v0id.exp.tile.TileWindmill;

public class TESRWindmill extends FastTESR<TileWindmill>
{
    public static WavefrontObject model;

    @Override
    public void renderTileEntityFast(TileWindmill te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        if (te.getWorld().getBlockState(te.getPos()).getBlock() == ExPBlocks.windmill)
        {
            EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockHorizontal.FACING);
            Vector3f pos = new Vector3f((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
            Matrix4f transform = Matrix4f.setIdentity(new Matrix4f());
            transform = transform.scale(new Vector3f(0.5F, 0.5F, 0.5F));
            float f = (float) Math.toRadians(te.rotation + (te.rotation - te.prevRotation) * partialTicks);
            if (facing.getAxis() == EnumFacing.Axis.Z)
            {
                transform = transform.rotate((float) Math.toRadians(90), new Vector3f(0, 1, 0));
            }

            transform = transform.rotate(f, new Vector3f(1, 0, 0));
            RenderUtils.renderObj(buffer, model, pos, transform, new float[]{1, 1, 1, 1}, new int[]{240, 0}, () -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("exp:blocks/windmill"));
        }
    }
}
