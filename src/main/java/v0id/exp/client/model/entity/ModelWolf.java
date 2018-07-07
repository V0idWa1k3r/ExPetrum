package v0id.exp.client.model.entity;

import com.google.common.collect.Maps;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import v0id.core.mca.MCAModelRenderer;
import v0id.core.mca.Utils;

import java.util.HashMap;

public class ModelWolf extends ModelBase
{
    public HashMap<String, MCAModelRenderer> parts = Maps.newHashMap();
    MCAModelRenderer body;
    MCAModelRenderer legRT;
    MCAModelRenderer body2;
    MCAModelRenderer legLT;
    MCAModelRenderer legFR;
    MCAModelRenderer legFL;
    MCAModelRenderer neck;
    MCAModelRenderer tail;
    MCAModelRenderer legRM;
    MCAModelRenderer legLM;
    MCAModelRenderer legFRB;
    MCAModelRenderer legFLB;
    MCAModelRenderer head;
    MCAModelRenderer legRB;
    MCAModelRenderer legLB;
    MCAModelRenderer head1;
    MCAModelRenderer earL;
    MCAModelRenderer earR;

    public ModelWolf()
    {
        textureWidth = 64;
        textureHeight = 32;

        body = new MCAModelRenderer(this, "body", 0, 0);
        body.mirror = false;
        body.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
        body.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        body.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        body.setTextureSize(64, 32);
        parts.put(body.boxName, body);

        legRT = new MCAModelRenderer(this, "LegRT", 0, 12);
        legRT.mirror = false;
        legRT.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 3);
        legRT.setInitialRotationPoint(3.0F, 0.0F, -10.0F);
        legRT.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(-0.1305262F, 0.0F, 0.0F, 0.9914449F)).transpose());
        legRT.setTextureSize(64, 32);
        parts.put(legRT.boxName, legRT);
        body.addChild(legRT);

        body2 = new MCAModelRenderer(this, "body2", 24, 0);
        body2.mirror = false;
        body2.addBox(-3.0F, -2.0F, -4.0F, 6, 5, 8);
        body2.setInitialRotationPoint(0.0F, 0.0F, -7.0F);
        body2.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        body2.setTextureSize(64, 32);
        parts.put(body2.boxName, body2);
        body.addChild(body2);

        legLT = new MCAModelRenderer(this, "LegLT", 0, 12);
        legLT.mirror = false;
        legLT.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 3);
        legLT.setInitialRotationPoint(-3.0F, 0.0F, -10.0F);
        legLT.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(-0.1305262F, 0.0F, 0.0F, 0.9914449F)).transpose());
        legLT.setTextureSize(64, 32);
        parts.put(legLT.boxName, legLT);
        body.addChild(legLT);

        legFR = new MCAModelRenderer(this, "LegFR", 10, 12);
        legFR.mirror = false;
        legFR.addBox(-0.5F, -4.0F, -1.0F, 1, 5, 2);
        legFR.setInitialRotationPoint(3.0F, -3.0F, 1.0F);
        legFR.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        legFR.setTextureSize(64, 32);
        parts.put(legFR.boxName, legFR);
        body.addChild(legFR);

        legFL = new MCAModelRenderer(this, "LegFL", 10, 12);
        legFL.mirror = false;
        legFL.addBox(-0.5F, -4.0F, -1.0F, 1, 5, 2);
        legFL.setInitialRotationPoint(-3.0F, -3.0F, 1.0F);
        legFL.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        legFL.setTextureSize(64, 32);
        parts.put(legFL.boxName, legFL);
        body.addChild(legFL);

        neck = new MCAModelRenderer(this, "Neck", 4, 5);
        neck.mirror = false;
        neck.addBox(-1.5F, -1.0F, -1.0F, 3, 3, 4);
        neck.setInitialRotationPoint(0.0F, 1.0F, 3.0F);
        neck.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(-0.3007058F, 0.0F, 0.0F, 0.95371693F)).transpose());
        neck.setTextureSize(64, 32);
        parts.put(neck.boxName, neck);
        body.addChild(neck);

        tail = new MCAModelRenderer(this, "Tail", 18, 0);
        tail.mirror = false;
        tail.addBox(0.0F, -5.0F, -1.0F, 1, 5, 1);
        tail.setInitialRotationPoint(0.0F, 2.0F, -11.0F);
        tail.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        tail.setTextureSize(64, 32);
        parts.put(tail.boxName, tail);
        body.addChild(tail);

        legRM = new MCAModelRenderer(this, "LegRM", 10, 12);
        legRM.mirror = false;
        legRM.addBox(-1.0F, -4.0F, -1.0F, 1, 5, 2);
        legRM.setInitialRotationPoint(0.5F, -4.0F, 0.0F);
        legRM.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(0.25881904F, 0.0F, 0.0F, 0.9659258F)).transpose());
        legRM.setTextureSize(64, 32);
        parts.put(legRM.boxName, legRM);
        legRT.addChild(legRM);

        legLM = new MCAModelRenderer(this, "LegLM", 10, 12);
        legLM.mirror = false;
        legLM.addBox(-1.0F, -4.0F, -1.0F, 1, 5, 2);
        legLM.setInitialRotationPoint(0.5F, -4.0F, 0.0F);
        legLM.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(0.25881904F, 0.0F, 0.0F, 0.9659258F)).transpose());
        legLM.setTextureSize(64, 32);
        parts.put(legLM.boxName, legLM);
        legLT.addChild(legLM);

        legFRB = new MCAModelRenderer(this, "LegFRB", 0, 19);
        legFRB.mirror = false;
        legFRB.addBox(-1.5F, -1.0F, -1.0F, 3, 1, 5);
        legFRB.setInitialRotationPoint(0.0F, -4.0F, 0.0F);
        legFRB.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        legFRB.setTextureSize(64, 32);
        parts.put(legFRB.boxName, legFRB);
        legFR.addChild(legFRB);

        legFLB = new MCAModelRenderer(this, "LegFLB", 0, 19);
        legFLB.mirror = false;
        legFLB.addBox(-1.5F, -1.0F, -1.0F, 3, 1, 5);
        legFLB.setInitialRotationPoint(0.0F, -4.0F, 0.0F);
        legFLB.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        legFLB.setTextureSize(64, 32);
        parts.put(legFLB.boxName, legFLB);
        legFL.addChild(legFLB);

        head = new MCAModelRenderer(this, "Head", 16, 13);
        head.mirror = false;
        head.addBox(-3.0F, -3.0F, 0.0F, 6, 6, 6);
        head.setInitialRotationPoint(0.0F, 2.0F, 2.0F);
        head.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(0.3007058F, 0.0F, 0.0F, 0.95371693F)).transpose());
        head.setTextureSize(64, 32);
        parts.put(head.boxName, head);
        neck.addChild(head);

        legRB = new MCAModelRenderer(this, "LegRB", 0, 19);
        legRB.mirror = false;
        legRB.addBox(-2.0F, 0.0F, -1.0F, 3, 1, 5);
        legRB.setInitialRotationPoint(0.0F, -4.0F, 0.0F);
        legRB.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(-0.1305262F, 0.0F, 0.0F, 0.9914449F)).transpose());
        legRB.setTextureSize(64, 32);
        parts.put(legRB.boxName, legRB);
        legRM.addChild(legRB);

        legLB = new MCAModelRenderer(this, "LegLB", 0, 19);
        legLB.mirror = false;
        legLB.addBox(-2.0F, 0.0F, -1.0F, 3, 1, 5);
        legLB.setInitialRotationPoint(0.0F, -4.0F, 0.0F);
        legLB.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(-0.1305262F, 0.0F, 0.0F, 0.9914449F)).transpose());
        legLB.setTextureSize(64, 32);
        parts.put(legLB.boxName, legLB);
        legLM.addChild(legLB);

        head1 = new MCAModelRenderer(this, "Head1", 40, 13);
        head1.mirror = false;
        head1.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 3);
        head1.setInitialRotationPoint(0.0F, -1.0F, 6.0F);
        head1.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        head1.setTextureSize(64, 32);
        parts.put(head1.boxName, head1);
        head.addChild(head1);

        earL = new MCAModelRenderer(this, "EarL", 0, 0);
        earL.mirror = false;
        earL.addBox(-1.0F, 0.0F, 0.0F, 2, 2, 1);
        earL.setInitialRotationPoint(2.0F, 2.0F, 1.0F);
        earL.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(-0.09143418F, -0.07085269F, -0.20780665F, 0.9713064F)).transpose());
        earL.setTextureSize(64, 32);
        parts.put(earL.boxName, earL);
        head.addChild(earL);

        earR = new MCAModelRenderer(this, "EarR", 0, 0);
        earR.mirror = false;
        earR.addBox(-1.0F, 0.0F, 0.0F, 2, 2, 1);
        earR.setInitialRotationPoint(-2.0F, 2.0F, 1.0F);
        earR.setInitialRotationMatrix((Matrix4f) Utils.createFromQuaternion(new Quaternion(-0.09541926F, 0.07532573F, 0.17393292F, 0.97722495F)).transpose());
        earR.setTextureSize(64, 32);
        parts.put(earR.boxName, earR);
        head.addChild(earR);

    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        body.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        this.legFL.resetRotationMatrix();
        this.legFL.getRotationMatrix().rotate(MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount, new Vector3f(1, 0, 0));
        this.legFR.resetRotationMatrix();
        this.legFR.getRotationMatrix().rotate(-MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount, new Vector3f(1, 0, 0));
        this.legLT.resetRotationMatrix();
        this.legLT.getRotationMatrix().rotate(MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount, new Vector3f(1, 0, 0));
        this.legRT.resetRotationMatrix();
        this.legRT.getRotationMatrix().rotate(-MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount, new Vector3f(1, 0, 0));
        this.head.resetRotationMatrix();
        this.head.getRotationMatrix().rotate(headPitch * 0.017453292F, new Vector3f(1, 0, 0));
        this.head.getRotationMatrix().rotate(netHeadYaw * 0.017453292F, new Vector3f(0, 1, 0));
        this.tail.resetRotationMatrix();
        this.tail.getRotationMatrix().rotate(MathHelper.cos(limbSwing * 0.6662F) * 4.2F * limbSwingAmount, new Vector3f(0, 0, 1));
    }

    public MCAModelRenderer getModelRendererFromName(String name)
    {
        return parts.get(name);
    }

}