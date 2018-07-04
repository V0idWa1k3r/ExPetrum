package v0id.exp.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import v0id.exp.entity.EntityAnimal;

import javax.annotation.Nullable;

/**
 * Created by V0idWa1k3r on 20-Jun-17.
 */
public class RenderAnimal extends RenderLiving<EntityAnimal>
{
    public RenderAnimal(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn)
    {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
        this.shadowSize = 0.5F;
        this.addLayer(new LayerColoredComponents(this));
        this.addLayer(new LayerGenderFeatures(this));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityAnimal entity)
    {
        return entity.getMainTexture(Minecraft.getMinecraft().getRenderPartialTicks());
    }

    @Override
    protected void preRenderCallback(EntityAnimal entitylivingbaseIn, float partialTickTime)
    {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        float s = entitylivingbaseIn.getRenderSize();
        GlStateManager.scale(s, s, s);
    }

    @Override
    public void doRender(EntityAnimal entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

    }

    public static class LayerGenderFeatures implements LayerRenderer<EntityAnimal>
    {
        public final RenderAnimal renderer;
        public LayerGenderFeatures(RenderAnimal parent)
        {
            super();
            this.renderer = parent;
        }

        @Override
        public void doRenderLayer(EntityAnimal entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
        {
            if (entitylivingbaseIn.getGenderSpecificTextures(partialTicks) != null)
            {
                this.renderer.bindTexture(entitylivingbaseIn.getGenderSpecificTextures(partialTicks));
                this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }

        @Override
        public boolean shouldCombineTextures()
        {
            return true;
        }
    }

    public static class LayerColoredComponents implements LayerRenderer<EntityAnimal>
    {
        public final RenderAnimal renderer;
        public LayerColoredComponents(RenderAnimal parent)
        {
            super();
            this.renderer = parent;
        }

        @Override
        public void doRenderLayer(EntityAnimal entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
        {
            if (entitylivingbaseIn.getFeatureColors(partialTicks) != null && entitylivingbaseIn.getColorableFeaturesTexture(partialTicks) != null)
            {
                float[] colors = entitylivingbaseIn.getFeatureColors(partialTicks);
                GlStateManager.color(colors[0], colors[1], colors[2], colors.length > 3 ? colors[3] : 1F);
                this.renderer.bindTexture(entitylivingbaseIn.getColorableFeaturesTexture(partialTicks));
                this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.color(1,1,1,1F);
            }
        }

        @Override
        public boolean shouldCombineTextures()
        {
            return true;
        }
    }
}
