package com.thilenius.flame.spark;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSparkSmall extends ModelBase
{
  //fields
    ModelRenderer Chest;
    ModelRenderer LeftArm;
    ModelRenderer RightArm;
    ModelRenderer Head;
    ModelRenderer LeftLeg;
    ModelRenderer RightLeg;
  
  public ModelSparkSmall()
  {
    textureWidth = 32;
    textureHeight = 32;
    setTextureOffset("LeftArm.LeftHand", 0, 24);
    setTextureOffset("RightArm.RightHand", 0, 24);
    setTextureOffset("LeftLeg.LeftFoot", 0, 19);
    setTextureOffset("RightLeg.RightFoot", 0, 19);
    
      Chest = new ModelRenderer(this, 0, 10);
      Chest.addBox(-3F, 0F, -2F, 6, 6, 3);
      Chest.setRotationPoint(0F, 0F, 0F);
      Chest.setTextureSize(32, 32);
      Chest.mirror = true;
      setRotation(Chest, 0F, 0F, 0F);
    LeftArm = new ModelRenderer(this, "LeftArm");
    LeftArm.setRotationPoint(3F, 1F, 0F);
    setRotation(LeftArm, 0F, 0F, 0F);
    LeftArm.mirror = true;
      LeftArm.addBox("LeftHand", 0F, 0F, -1F, 1, 4, 2);
    RightArm = new ModelRenderer(this, "RightArm");
    RightArm.setRotationPoint(-3F, 1F, 0F);
    setRotation(RightArm, 0F, 0F, 0F);
    RightArm.mirror = true;
      RightArm.addBox("RightHand", -1F, 0F, -1F, 1, 4, 2);
      Head = new ModelRenderer(this, 0, 0);
      Head.addBox(-4F, -5F, -3F, 8, 5, 5);
      Head.setRotationPoint(0F, 0F, 0F);
      Head.setTextureSize(32, 32);
      Head.mirror = true;
      setRotation(Head, 0F, 0F, 0F);
    LeftLeg = new ModelRenderer(this, "LeftLeg");
    LeftLeg.setRotationPoint(1F, 6F, 0F);
    setRotation(LeftLeg, 0F, 0F, 0F);
    LeftLeg.mirror = true;
      LeftLeg.addBox("LeftFoot", 0F, 0F, -3F, 2, 1, 4);
    RightLeg = new ModelRenderer(this, "RightLeg");
    RightLeg.setRotationPoint(-1F, 6F, 0F);
    setRotation(RightLeg, 0F, 0F, 0F);
    RightLeg.mirror = true;
      RightLeg.addBox("RightFoot", -2F, 0F, -3F, 2, 1, 4);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(entity, f, f1, f2, f3, f4, f5);
    Chest.render(f5);
    LeftArm.render(f5);
    RightArm.render(f5);
    Head.render(f5);
    LeftLeg.render(f5);
    RightLeg.render(f5);
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
