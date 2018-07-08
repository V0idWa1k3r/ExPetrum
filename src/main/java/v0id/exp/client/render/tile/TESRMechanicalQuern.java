package v0id.exp.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.animation.FastTESR;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import v0id.core.client.RenderUtils;
import v0id.exp.tile.TileMechanicalQuern;

public class TESRMechanicalQuern extends FastTESR<TileMechanicalQuern>
{

    @Override
    public void renderTileEntityFast(TileMechanicalQuern te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        TextureAtlasSprite top = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("exp:blocks/quern");
        TextureAtlasSprite side = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("exp:blocks/quern_side");
        Vector3f pos = new Vector3f((float)x + 0.5F, (float)y + 0.75F, (float)z + 0.5F);
        Matrix4f transform = Matrix4f.setIdentity(new Matrix4f());
        transform = transform.scale(new Vector3f(1, 0.5F, 1));
        transform = transform.rotate((float)Math.toRadians(te.rotationIndex * 8), new Vector3f(0, 1, 0));
        int i = te.getWorld().getCombinedLight(te.getPos(), 0);
        int j = i % 65536;
        int k = i / 65536;
        RenderUtils.renderCube(buffer, transform, pos, new float[]{ 1, 1, 1, 1 }, new int[]{ k, j }, face -> face == EnumFacing.UP ? top : side);
    }
}
