package com.thilenius.flame.jumbotron;

import com.thilenius.flame.spark.ModelSparkSmall;
import com.thilenius.flame.spark.SparkTileEntity;
import com.thilenius.utilities.types.LocationF3D;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Alec on 12/26/14.
 */
public class JumboRenderer extends TileEntitySpecialRenderer {

    public JumboRenderer() {

    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float deltaTime) {
        JumboTileEntity jumbo = (JumboTileEntity) te;

        // Render Text (TEST)
        GL11.glPushMatrix();

        GL11.glTranslatef((float)x - 0.2f, (float)y + 1.8f, (float)z - 0.01f);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);

        String finalText = "";
        FontRenderer fontrenderer = this.func_147498_b();
        if (jumbo != null && jumbo.Text != null && jumbo.Size.X > 0) {
            finalText = jumbo.Text;

            // Truncate until it fits max width
            while (((float)fontrenderer.getStringWidth(finalText) * 0.2f) + 0.4f > jumbo.Size.X) {
                finalText = finalText.substring(0, finalText.length() - 1);
                float newSize = ((float)fontrenderer.getStringWidth(finalText) * 0.2f) + 0.4f;
                float jumboVal = jumbo.Size.X;
            }
        }


        // Scale down text
        GL11.glScalef(0.2f, 0.2f, 0.2f);

        fontrenderer.drawString(finalText, 0, 0, 0xFFFFFFFF);

        // End Render Text (TEST)
        GL11.glPopMatrix();
    }

}