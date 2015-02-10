package com.thilenius.flame.spark;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSparkSmall extends ModelBase
{
    // Fields
    ModelRenderer Body;
    ModelRenderer Head;
    ModelRenderer LeftTrack;
    ModelRenderer RightTrack;

    public ModelSparkSmall()
    {
        textureWidth = 64;
        textureHeight = 128;
        setTextureOffset("Head.Shape1", 0, 0);
        setTextureOffset("Body.Shape2", 0, 24);
        setTextureOffset("LeftTrack.Shape4", 0, 61);
        setTextureOffset("RightTrack.Shape3", 0, 39);

        Body = new ModelRenderer(this, "Body");
        Body.setRotationPoint(0F, 0F, 0F);
        setRotation(Body, 0F, 0F, 0F);
        Body.mirror = true;
        Body.addBox("Shape2", -4F, 0F, -3F, 8, 5, 6);

        Head = new ModelRenderer(this, "Head");
        Head.setRotationPoint(0F, 0F, 0F);
        setRotation(Head, 0F, 0F, 0F);
        Head.mirror = true;
        Head.addBox("Shape1", -8F, -10F, -5F, 16, 10, 10);

        LeftTrack = new ModelRenderer(this, "LeftTrack");
        LeftTrack.setRotationPoint(4F, 5F, 0F);
        setRotation(LeftTrack, 0F, 0F, 0F);
        LeftTrack.mirror = true;
        LeftTrack.addBox("Shape4", 0F, -3F, -9F, 4, 4, 14);
        RightTrack = new ModelRenderer(this, "RightTrack");
        RightTrack.setRotationPoint(-6F, 5F, 0F);
        setRotation(RightTrack, 0F, 0F, 0F);
        RightTrack.mirror = true;
        RightTrack.addBox("Shape3", -2F, -3F, -9F, 4, 4, 14);
    }

  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(entity, f, f1, f2, f3, f4, f5);
    Body.render(f5);
    LeftTrack.render(f5);
    RightTrack.render(f5);
    Head.render(f5);
  }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}


