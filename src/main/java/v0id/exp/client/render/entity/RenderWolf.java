package v0id.exp.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import v0id.exp.entity.impl.Wolf;

import javax.annotation.Nullable;

/**
 * Created by V0idWa1k3r on 20-Jun-17.
 */
public class RenderWolf extends RenderLiving<Wolf>
{
    public RenderWolf(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn)
    {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
        this.shadowSize = 0.5F;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(Wolf entity)
    {
        return new ResourceLocation("exp", "textures/entities/wolf.png");
    }

    @Override
    protected void preRenderCallback(Wolf entitylivingbaseIn, float partialTickTime)
    {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(0, 2, 0);
        int colorHex = entitylivingbaseIn.getColorHEX();
        float r = ((colorHex & 0xFF0000) >> 16) / 255F;
        float g = ((colorHex & 0xFF00) >> 8) / 255F;
        float b = (colorHex & 0xFF) / 255F;
        GlStateManager.color(r, g, b, 1F);
    }

    @Override
    public void doRender(Wolf entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
