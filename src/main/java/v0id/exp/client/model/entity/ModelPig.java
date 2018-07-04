package v0id.exp.client.model.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import v0id.exp.client.model.EntityModelDynamic;

/**
 * Created by V0idWa1k3r on 20-Jun-17.
 */
public class ModelPig extends EntityModelDynamic
{
    public static ModelPig instance;

    @Override
    public void createModel()
    {
        this.textureHeight = 32;
        this.textureWidth = 64;
        ModelRenderer body = new ModelRenderer(this, "body");
        body.addBox(0, 4, 0, 8, 8, 12);
        body.setRotationPoint(-4, 8, -6);
        ModelRenderer legfl = new ModelRenderer(this, "legfl").setTextureOffset(40, 0);
        legfl.addBox(0, 0, 0, 2, 4, 2);
        legfl.setRotationPoint(0, 12, 0);
        body.addChild(legfl);
        ModelRenderer legfr = new ModelRenderer(this, "legfr").setTextureOffset(40, 0);
        legfr.addBox(0, 0, 0, 2, 4, 2);
        legfr.setRotationPoint(6, 12, 0);
        body.addChild(legfr);
        ModelRenderer legbl = new ModelRenderer(this, "legfl").setTextureOffset(40, 0);
        legbl.addBox(0, 0, 0, 2, 4, 2);
        legbl.setRotationPoint(0, 12, 10);
        body.addChild(legbl);
        ModelRenderer legbr = new ModelRenderer(this, "legfr").setTextureOffset(40, 0);
        legbr.addBox(0, 0, 0, 2, 4, 2);
        legbr.setRotationPoint(6, 12, 10);
        body.addChild(legbr);
        ModelRenderer head = new ModelRenderer(this, "head").setTextureOffset(0, 20);
        head.addBox(-2F, -1, -4, 5, 5, 5);
        head.setRotationPoint(3.5F, 4, 0);
        body.addChild(head);
        ModelRenderer head1 = new ModelRenderer(this, "head1").setTextureOffset(20, 20);
        head1.addBox(0F, 0, 0, 3, 3, 2);
        head1.setRotationPoint(-1, 1, -6);
        head.addChild(head1);
        ModelRenderer tuskl = new ModelRenderer(this, "tuskl").setTextureOffset(48, 0);
        tuskl.addBox(0, 0, 0, 1, 2, 1);
        tuskl.setRotationPoint(-0.5F, 2, -0.5F);
        head1.addChild(tuskl);
        ModelRenderer tuskr = new ModelRenderer(this, "tuskl").setTextureOffset(48, 0);
        tuskr.addBox(0, 0, 0, 1, 2, 1);
        tuskr.setRotationPoint(2.5F, 2, -0.5F);
        head1.addChild(tuskr);
        ModelRenderer udder = new ModelRenderer(this, "udder").setTextureOffset(40, 10);
        udder.addBox(0, 0, 0, 6, 1, 6);
        udder.setRotationPoint(1, 12, 5);
        body.addChild(udder);
        this.components.add(body);
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
    }
}
