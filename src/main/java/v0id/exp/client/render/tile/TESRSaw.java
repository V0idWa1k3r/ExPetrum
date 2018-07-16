package v0id.exp.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.animation.FastTESR;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import v0id.api.exp.data.ExPBlocks;
import v0id.exp.client.render.RenderUtils;
import v0id.exp.tile.TileSaw;


public class TESRSaw extends FastTESR<TileSaw>
{
    @Override
    public void renderTileEntityFast(TileSaw te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        if (te.getWorld().getBlockState(te.getPos()).getBlock() == ExPBlocks.saw)
        {
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("exp:blocks/saw");
            Vector3f pos = new Vector3f((float) x + 0.5F, (float) y + 0.1F, (float) z + 0.5F);
            Matrix4f transform = Matrix4f.setIdentity(new Matrix4f());
            float f = (float) Math.toRadians(te.progress * 90);
            EnumFacing.Axis axis = te.getAxis();
            if (axis == EnumFacing.Axis.X)
            {
                transform = transform.rotate(1.5708F, new Vector3f(0, 1, 0));
            }

            transform = transform.scale(new Vector3f(0.0001F, 1, 1));
            transform = transform.rotate(f, new Vector3f(1, 0, 0));
            int i = te.getWorld().getCombinedLight(te.getPos(), 0);
            int j = i % 65536;
            int k = i / 65536;
            RenderUtils.renderCube(buffer, transform, pos, new float[]{1, 1, 1, 1}, new int[]{k, j}, side -> sprite);
        }
    }
}
