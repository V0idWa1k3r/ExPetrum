package v0id.exp.client.render.tile;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.FastTESR;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.client.render.RenderUtils;
import v0id.exp.tile.TileShaft;

public class TESRShaft extends FastTESR<TileShaft>
{
    public static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
        new ResourceLocation("exp", "blocks/wood/plank/generic"),
        new ResourceLocation("exp", "blocks/crucible"),
        new ResourceLocation("exp", "blocks/bronze_plates"),
        new ResourceLocation("exp", "blocks/iron_plates"),
        new ResourceLocation("exp", "blocks/steel_plates"),
        new ResourceLocation("exp", "blocks/hsla_plates")
    };

    @Override
    public void renderTileEntityFast(TileShaft te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        EnumFacing.Axis axis = te.getWorld().getBlockState(te.getPos()).getValue(BlockRotatedPillar.AXIS);
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TEXTURES[te.material.ordinal()].toString());
        Matrix4f transform = Matrix4f.setIdentity(new Matrix4f());
        Vector3f pos = new Vector3f((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
        int i = te.getWorld().getCombinedLight(te.getPos(), 0);
        int j = i % 65536;
        int k = i / 65536;
        long current = IExPWorld.of(te.getWorld()).today().getTime();
        float rotation = current - te.lastStepped <= 20 ? (float)Math.toRadians((te.getWorld().getWorldTime() % 45F) * 8 + 8 * partialTicks) : (float)Math.toRadians((te.lastStepped % 45F) * 8);
        if (axis == EnumFacing.Axis.X)
        {
            transform = transform.scale(new Vector3f(1, 0.375F, 0.375F));
            transform = transform.rotate(rotation, new Vector3f(1, 0, 0));
        }

        if (axis == EnumFacing.Axis.Z)
        {
            transform = transform.scale(new Vector3f(0.375F, 0.375F, 1));
            transform = transform.rotate(rotation, new Vector3f(0, 0, 1));
        }

        if (axis == EnumFacing.Axis.Y)
        {
            transform = transform.scale(new Vector3f(0.375F, 1, 0.375F));
            transform = transform.rotate(rotation, new Vector3f(0, 1, 0));
        }

        RenderUtils.renderCube(buffer, transform, pos, new float[]{ 1, 1, 1, 1 }, new int[]{ k, j }, facing -> sprite);
    }
}
