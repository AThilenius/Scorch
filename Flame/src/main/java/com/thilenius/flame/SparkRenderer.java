package com.thilenius.flame;

import com.thilenius.blaze.frontend.BFESparkServer;
import com.thilenius.utilities.types.LocationF3D;
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

import java.util.HashSet;

public class SparkRenderer extends TileEntitySpecialRenderer {

	private ModelSparkSmall model = new ModelSparkSmall();

	public SparkRenderer() {
		
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float deltaTime) {

        HashSet<SparkTileEntity> known = BFESparkServer.DebugInstance.KnownTileEntities;

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