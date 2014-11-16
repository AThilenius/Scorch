package com.thilenius.flame;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SparkRenderer extends TileEntitySpecialRenderer {

	private ModelSparkSmall model = new ModelSparkSmall();

	public SparkRenderer() {
		
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float deltaTime) {
		
		SparkTileEntity spark = (SparkTileEntity) te;
		
        //spark.displace += deltaTime * 0.01F;
        
//        if (spark.displace > 1.0f) {
//			spark.getWorldObj().setBlock(spark.xCoord, spark.yCoord, spark.zCoord - 1, spark.getBlockType(), spark.getBlockMetadata(), 0);
//        	spark.getWorldObj().removeTileEntity(spark.xCoord, spark.yCoord, spark.zCoord);
//        	spark.getWorldObj().setBlockToAir(spark.xCoord, spark.yCoord, spark.zCoord);
//        	return;
//        }
		
        // Open model matrix
		GL11.glPushMatrix();
		
		// Translate
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.45F, (float) z + 0.5F - spark.displace);
		
		// Rotate
		GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
		
		// Texture
		ResourceLocation textures = (new ResourceLocation("flame", "textures/entity/spark")); 
		this.bindTexture(textures);
		
		// Draw Objects
		model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		
		// Close model matrix
		GL11.glPopMatrix();
	}

//	//Set the lighting stuff, so it changes it's brightness properly.       
//	private void adjustLightFixture(World world, int i, int j, int k, Block block) {
//		Tessellator tess = Tessellator.instance;
//		//float brightness = block.getBlockBrightness(world, i, j, k);
//		//As of MC 1.7+ block.getBlockBrightness() has become block.getLightValue():
//		float brightness = block.getLightValue(world, i, j, k);
//		int skyLight = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
//		int modulousModifier = skyLight % 65536;
//		int divModifier = skyLight / 65536;
//		tess.setColorOpaque_F(brightness, brightness, brightness);
//		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,  (float) modulousModifier,  divModifier);
//	}
	
}