package v0id.exp.client.render.tile;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.model.animation.FastTESR;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import v0id.core.client.RenderUtils;
import v0id.core.client.model.WavefrontObject;
import v0id.exp.item.ItemGrindstone;
import v0id.exp.tile.TileQuern;

public class TESRQuern extends FastTESR<TileQuern>
{
    public static TextureAtlasSprite quernTopTexture;
    public static WavefrontObject quernTopModel;

    @Override
    public void renderTileEntityFast(TileQuern te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        if (te.inventory.getStackInSlot(0).getItem() instanceof ItemGrindstone && !te.inventory.getStackInSlot(0).isEmpty())
        {
            Vector3f pos = new Vector3f((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
            Matrix4f transform = Matrix4f.setIdentity(new Matrix4f());
            float rotation = (float) Math.toRadians(te.rotationIndex == 0 ? 0 : te.rotationIndex * 4 - 4 * partialTicks);
            transform = transform.scale(new Vector3f(0.5F, 0.5F, 0.5F));
            transform = transform.rotate((float) Math.PI, new Vector3f(1, 0, 0));
            transform = transform.rotate(rotation, new Vector3f(0, 1, 0));
            int i = te.getWorld().getCombinedLight(te.getPos(), 0);
            int j = i % 65536;
            int k = i / 65536;
            RenderUtils.renderObj(buffer, quernTopModel, pos, transform, new float[]{1, 1, 1, 1}, new int[]{k, j}, () -> quernTopTexture);
        }
    }
}
