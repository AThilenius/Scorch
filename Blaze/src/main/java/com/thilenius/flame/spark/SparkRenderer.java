package com.thilenius.flame.spark;

import com.thilenius.utilities.types.LocationF3D;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class SparkRenderer extends TileEntitySpecialRenderer {

	private ModelSparkSmall model = new ModelSparkSmall();

	public SparkRenderer() {
		
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float deltaTime) {
		SparkTileEntity spark = (SparkTileEntity) te;
        float rotation = spark.getRotation();
        LocationF3D offset = spark.getOffset();
		
        // Open model matrix
		GL11.glPushMatrix();
		
		// Translate
		GL11.glTranslatef((float) x + 0.5f + offset.X, (float) y + 0.45f + offset.Y, (float) z + 0.5f + offset.Z);
		
		// Rotate him upside down
		GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);

        // Rotate around the Y
        GL11.glRotatef(rotation, 0.0f, 1.0f, 0.0f);
		
		// Texture
		ResourceLocation textures = (new ResourceLocation("flame:textures/model/spark.png"));
		this.bindTexture(textures);
		
		// Draw Objects
		model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		
		// Close model matrix
		GL11.glPopMatrix();



        // Render Text (TEST)
        GL11.glPushMatrix();

        GL11.glTranslatef((float)x + 16.0f, (float)y + 5.0f, (float)z);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);

        // Rotate him upside down
        //GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);

        // Scale down text
        GL11.glScalef(0.3f, 0.3f, 0.3f);

        FontRenderer fontrenderer = this.func_147498_b();
        fontrenderer.drawString("Hello World", 0, 0, 0xFFFFFFFF);

        // End Render Text (TEST)
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