package v0id.exp.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.model.animation.FastTESR;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import v0id.core.client.RenderUtils;
import v0id.exp.tile.TileCrate;


public class TESRCrate extends FastTESR<TileCrate>
{
    @Override
    public void renderTileEntityFast(TileCrate te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(te.getWorld().getBlockState(te.getPos()).getBlock().getActualState(te.getWorld().getBlockState(te.getPos()), te.getWorld(), te.getPos()));
        Vector3f pos = new Vector3f((float)x + 0.5F, (float)y + 0.96875F, (float)z);
        Matrix4f transform = Matrix4f.setIdentity(new Matrix4f());
        transform = transform.rotate((float)Math.toRadians((te.lidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks) * -45F), new Vector3f(1, 0, 0));
        transform = transform.translate(new Vector3f(0, 0, 0.5F));
        transform = transform.scale(new Vector3f(1, 0.0625F, 1));
        int i = te.getWorld().getCombinedLight(te.getPos(), 0);
        int j = i % 65536;
        int k = i / 65536;
        RenderUtils.renderCube(buffer, transform, pos, new float[]{ 1, 1, 1, 1 }, new int[]{ k, j }, side -> sprite);
    }
}
