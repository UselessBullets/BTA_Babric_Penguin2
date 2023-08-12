package useless.penguinmod.model;

import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.core.util.helper.MathHelper;

public class ModelPenguin extends ModelBase {
    public Cube head;
    public Cube body;
    public Cube rightLeg;
    public Cube leftLeg;
    public Cube rightWing;
    public Cube leftWing;
    public Cube beak;
    public Cube hair;

    public ModelPenguin() {
        byte byte0 = 20;
        this.head = new Cube(0, 0);
        this.head.addBox(-3.0F, -5.0F, -3.0F, 6, 5, 6, 0.0F);
        this.head.setRotationPoint(0,-3.0F + byte0,0);
        this.beak = new Cube(0, 26);
        this.beak.addBox(-1.5F, -1.5F, -4.0F, 3, 1, 2, 0.0F);
        this.beak.setRotationPoint(0,-3.0F + byte0,0);
        this.hair = new Cube(30, 0);
        this.hair.addBox(-3.0F, -7.0F, -3.0F, 6, 2, 6, 0.0F);
        this.hair.setRotationPoint(0,-3.0F + byte0,0);
        this.body = new Cube(0, 11);
        this.body.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 0.0F + byte0, 0.0F);
        //this.body.setRotationAngle( (float)Math.toRadians(-90), 0, 0);
        this.rightLeg = new Cube(0, 23);
        this.rightLeg.addBox(-0.5F, 0.0F, -1.0F, 3, 1, 2);
        this.rightLeg.setRotationPoint(-1.5F, 3.0F + byte0, -1.0F);
        this.rightLeg.setRotationAngle(0, (float)Math.toRadians(180), 0);
        this.leftLeg = new Cube(0, 23);
        this.leftLeg.addBox(-0.5F, 0.0F, -1.0F, 3, 1, 2);
        this.leftLeg.setRotationPoint(1.5F, 3.0F + byte0, -1.0F);
        this.rightWing = new Cube(54, 0);
        this.rightWing.addBox(-0.5F, 0.0F, -2.0F, 1, 4, 4);
        this.rightWing.setRotationPoint(-3.0F, -2.0F + byte0, 0.0F);
        this.leftWing = new Cube(54, 0);
        this.leftWing.addBox(-0.5F, 0.0F, -2.0F, 1, 4, 4);
        this.leftWing.setRotationPoint(3.0F, -2.0F + byte0, 0.0F);
    }

    public void render(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
        this.head.render(scale);
        this.beak.render(scale);
        this.hair.render(scale);
        this.body.render(scale);
        this.rightLeg.render(scale);
        this.leftLeg.render(scale);
        this.rightWing.render(scale);
        this.leftWing.render(scale);
    }

    public void setRotationAngles(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
        this.head.rotateAngleX = -(headPitch / 57.29578F);
        this.head.rotateAngleY = headYaw / 57.29578F;
        this.beak.rotateAngleX = this.head.rotateAngleX;
        this.beak.rotateAngleY = this.head.rotateAngleY;
        this.hair.rotateAngleX = this.head.rotateAngleX;
        this.hair.rotateAngleY = this.head.rotateAngleY;
        //this.body.rotateAngleX = 1.570796F;
        this.rightLeg.rotateAngleZ = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbYaw;
        this.leftLeg.rotateAngleZ = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbYaw;
        this.rightWing.rotateAngleZ = ticksExisted + (float) Math.toRadians(10F);
        this.leftWing.rotateAngleZ = -ticksExisted - (float) Math.toRadians(10F);
    }
}