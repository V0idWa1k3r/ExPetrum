package v0id.exp.client.model.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import v0id.exp.client.model.EntityModelDynamic;

/**
 * Created by V0idWa1k3r on 20-Jun-17.
 */
public class ModelChicken extends EntityModelDynamic
{
    public static ModelChicken instance;

    @Override
    public void createModel()
    {
        this.textureHeight = 32;
        this.textureWidth = 64;
        ModelRenderer body = new ModelRenderer(this, "body");
        body.addBox(0, 0, 0, 4, 6, 10);
        body.setRotationPoint(-2, 11.5F, -5);
        ModelRenderer wingL = new ModelRenderer(this, "wingL").setTextureOffset(28, 0);
        wingL.addBox(0, -0.5F, 0, 4, 1, 9);
        wingL.setRotationPoint(0, 0, 0);
        body.addChild(wingL);
        ModelRenderer wingR = new ModelRenderer(this, "wingR").setTextureOffset(28, 0);
        wingR.addBox(0, -0.5F, 0, 4, 1, 9);
        wingR.setRotationPoint(4, 0, 0);
        body.addChild(wingR);
        ModelRenderer tail = new ModelRenderer(this, "tail").setTextureOffset(0, 16);
        tail.addBox(0, 0, 0, 2, 4, 6);
        tail.setRotationPoint(1, 0, 9);
        body.addChild(tail);
        ModelRenderer tail2 = new ModelRenderer(this, "tail2").setTextureOffset(16, 16);
        tail2.addBox(0, 0, 0, 1, 2, 5);
        tail2.setRotationPoint(0.5F, 0, 1);
        tail2.rotateAngleX = (float)Math.toRadians(25);
        tail.addChild(tail2);
        ModelRenderer legL = new ModelRenderer(this, "legL").setTextureOffset(28, 10);
        legL.addBox(0, 0, 0, 2, 4, 2);
        legL.setRotationPoint(-1, 5, 4);
        legL.rotateAngleX = 0.174533F;
        body.addChild(legL);
        ModelRenderer legL2 = new ModelRenderer(this, "legL2").setTextureOffset(36, 10);
        legL2.addBox(0, 0, 0, 1, 4, 1);
        legL2.setRotationPoint(0.5F, 3.5F, 0.5F);
        legL2.rotateAngleX = -0.174533F;
        legL.addChild(legL2);
        ModelRenderer legL3 = new ModelRenderer(this, "legL3").setTextureOffset(28, 16);
        legL3.addBox(0, 0, 0, 3, 1, 3);
        legL3.setRotationPoint(-1F, 3, -1F);
        legL2.addChild(legL3);
        ModelRenderer legR = new ModelRenderer(this, "legR").setTextureOffset(28, 10);
        legR.addBox(0, 0, 0, 2, 4, 2);
        legR.setRotationPoint(3, 5, 4);
        legR.rotateAngleX = 0.174533F;
        body.addChild(legR);
        ModelRenderer legR2 = new ModelRenderer(this, "legR2").setTextureOffset(36, 10);
        legR2.addBox(0, 0, 0, 1, 4, 1);
        legR2.setRotationPoint(0.5F, 3.5F, 0.5F);
        legR2.rotateAngleX = -0.174533F;
        legR.addChild(legR2);
        ModelRenderer legR3 = new ModelRenderer(this, "legR3").setTextureOffset(28, 16);
        legR3.addBox(0, 0, 0, 3, 1, 3);
        legR3.setRotationPoint(-1F, 3, -1F);
        legR2.addChild(legR3);
        ModelRenderer head = new ModelRenderer(this, "head").setTextureOffset(40, 16);
        head.addBox(-1.5F, 0, -4, 3, 4, 3);
        head.setRotationPoint(2F,-2,1.5F);
        body.addChild(head);
        ModelRenderer beak = new ModelRenderer(this, "beak").setTextureOffset(54, 0);
        beak.addBox(0, 0, 0, 2, 1, 2);
        beak.setRotationPoint(-1,1,-6);
        head.addChild(beak);
        ModelRenderer comb = new ModelRenderer(this, "comb").setTextureOffset(40, 10);
        comb.addBox(0, 0, 0, 1, 2, 4);
        comb.setRotationPoint(-0.5F,-2,-4);
        head.addChild(comb);
        ModelRenderer comb2 = new ModelRenderer(this, "comb2").setTextureOffset(50, 10);
        comb2.addBox(0, 0, 0, 2, 3, 1);
        comb2.setRotationPoint(-1F,2,-5);
        head.addChild(comb2);
        this.components.add(body);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.components.get(0).childModels.get(0).rotateAngleZ = 1.8F;
        this.components.get(0).childModels.get(1).rotateAngleZ = 1.2F;
        this.components.get(0).childModels.get(2).rotateAngleX =  MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount + (float)Math.toRadians(10F + Math.sin(Math.toRadians(entityIn.ticksExisted)) * 10F);
        this.components.get(0).childModels.get(5).rotateAngleX = headPitch * 0.017453292F;
        this.components.get(0).childModels.get(5).rotateAngleY = netHeadYaw * 0.017453292F;
        this.components.get(0).childModels.get(3).rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.components.get(0).childModels.get(4).rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    }
}
