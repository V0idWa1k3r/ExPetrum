package v0id.exp.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.FastTESR;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import v0id.api.exp.world.IExPWorld;
import v0id.core.client.RenderUtils;
import v0id.core.client.model.WavefrontObject;
import v0id.exp.item.ItemGeneric;
import v0id.exp.tile.TileSpinningWheel;

public class TESRSpinningWheel extends FastTESR<TileSpinningWheel>
{
    public static WavefrontObject model;

    @Override
    public void renderTileEntityFast(TileSpinningWheel te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getTextureMap().registerSprite(new ResourceLocation("exp", "blocks/anvil"));
        TextureAtlasSprite wood = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getTextureMap().registerSprite(new ResourceLocation("exp", "blocks/wood/plank/generic"));
        Vector3f pos = new Vector3f((float)x + 0.17F, (float)y + 0.8F, (float)z + 0.28F);
        long current = IExPWorld.of(te.getWorld()).today().getTime();
        long last = te.lastActivated;
        long diff = current - last;
        float rot = diff <= 40 ? (float)Math.toRadians(-diff * 9F) : 0;
        Matrix4f transform = Matrix4f.setIdentity(new Matrix4f());
        transform = transform.scale(new Vector3f(0.6F, 0.6F, 0.6F));
        transform = transform.rotate(rot, new Vector3f(1, 0, 0));
        transform = transform.translate(new Vector3f(0, 0, -0.5F));
        int i = te.getWorld().getCombinedLight(te.getPos(), 0);
        int j = i % 65536;
        int k = i / 65536;
        RenderUtils.renderObj(buffer, model, pos, transform, new float[]{ 1, 1, 1, 1 }, new int[]{ k, j }, () -> wood);
        if (!te.inventory.getStackInSlot(0).isEmpty() && te.inventory.getStackInSlot(0).getMetadata() == ItemGeneric.EnumGenericType.CARDED_WOOL.ordinal())
        {
            pos = new Vector3f((float) x + 0.47F, (float) y + 0.6F, (float) z + 0.53F);
            transform = Matrix4f.setIdentity(new Matrix4f());
            transform = transform.scale(new Vector3f(0.1F, 0.15F, 0.1F));
            RenderUtils.renderCube(buffer, transform, pos, new float[]{0.6F, 0.6F, 0.6F, 1}, new int[]{k, j}, side -> sprite);
        }

        float progress = te.inventory.getStackInSlot(0).getMetadata() == ItemGeneric.EnumGenericType.WOOL_YARN.ordinal() ? 1 : te.progress;
        if (progress > 0)
        {
            float actualProgress = (progress * 10 - (1 - Math.min(40, diff) / 40F)) / 10;
            pos = new Vector3f((float) x + 0.47F, (float) y + 0.74F, (float) z + 0.72F);
            transform = Matrix4f.setIdentity(new Matrix4f());
            transform = transform.scale(new Vector3f(0.2F * actualProgress, 0.2F, 0.2F));
            RenderUtils.renderCube(buffer, transform, pos, new float[]{1, 1, 1, 1}, new int[]{k, j}, side -> sprite);
        }
    }
}
