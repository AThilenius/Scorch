package com.thilenius.flame.entity;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Alec on 3/27/15.
 */
public class FlameSupportNameGui extends GuiScreen {

    public static final int GUI_ID = 0;

    public FlameSupportNameGui() {

    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        //draw your Gui here, only thing you need to change is the path
        ResourceLocation texture = (new ResourceLocation("/gui/trap.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (width - 176) / 2;
        int y = (height - 166) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, 176, 166);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
