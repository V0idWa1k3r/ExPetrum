package v0id.exp.client.model.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import v0id.exp.client.model.EntityModelDynamic;
import v0id.exp.entity.impl.Sheep;

/**
 * Created by V0idWa1k3r on 20-Jun-17.
 */
public class ModelSheep extends EntityModelDynamic
{
    public static ModelSheep instance;

    @Override
    public void createModel()
    {
        this.textureHeight = 64;
        this.textureWidth = 64;
        ModelRenderer body = new ModelRenderer(this, "body");
        body.addBox(0, 4, 0, 8, 8, 12);
        body.setRotationPoint(-4, 4, -6);
        ModelRenderer legfl = new ModelRenderer(this, "legfl").setTextureOffset(40, 0);
        legfl.addBox(0, 0, 0, 2, 8, 2);
        legfl.setRotationPoint(0, 12, 0);
        body.addChild(legfl);
        ModelRenderer legfr = new ModelRenderer(this, "legfr").setTextureOffset(40, 0);
        legfr.addBox(0, 0, 0, 2, 8, 2);
        legfr.setRotationPoint(6, 12, 0);
        body.addChild(legfr);
        ModelRenderer legbl = new ModelRenderer(this, "legfl").setTextureOffset(40, 0);
        legbl.addBox(0, 0, 0, 2, 8, 2);
        legbl.setRotationPoint(0, 12, 10);
        body.addChild(legbl);
        ModelRenderer legbr = new ModelRenderer(this, "legfr").setTextureOffset(40, 0);
        legbr.addBox(0, 0, 0, 2, 8, 2);
        legbr.setRotationPoint(6, 12, 10);
        body.addChild(legbr);
        ModelRenderer head = new ModelRenderer(this, "head").setTextureOffset(0, 20);
        head.addBox(-2F, -1, -4, 5, 5, 5);
        head.setRotationPoint(3.5F, 4, 0);
        body.addChild(head);
        ModelRenderer head1 = new ModelRenderer(this, "head1").setTextureOffset(20, 20);
        head1.addBox(0F, 0, 0, 3, 3, 6);
        head1.setRotationPoint(-1, 1, -7);
        head.addChild(head1);
        ModelRenderer horns = new ModelRenderer(this, "horns").setTextureOffset(40, 10);
        horns.addBox(0F, 0, 0, 6, 6, 6);
        horns.setRotationPoint(-2.5F, -1, -4.5F);
        head.addChild(horns);
        ModelRenderer wool = new ModelRenderer(this, "wool").setTextureOffset(0, 30);
        wool.addBox(0F, 3, 0, 10, 10, 14);
        wool.setRotationPoint(-5, 4.5F, -7);
        this.components.add(body);
        this.components.add(wool);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.components.get(0).childModels.get(0).rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.components.get(0).childModels.get(1).rotateAngleX = -MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.components.get(0).childModels.get(2).rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.components.get(0).childModels.get(3).rotateAngleX = -MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.components.get(0).childModels.get(4).rotateAngleX = headPitch * 0.017453292F;
        this.components.get(0).childModels.get(4).rotateAngleY = netHeadYaw * 0.017453292F;
        this.components.get(0).childModels.get(4).childModels.get(0).rotateAngleX = 0.3F;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.components.get(0).render(scale);
        Sheep s = (Sheep)entityIn;
        if (s.getDataManager().get(Sheep.PARAM_WOOL_TICKS) <= 0)
        {
            this.components.get(1).render(scale);
        }
    }
}
