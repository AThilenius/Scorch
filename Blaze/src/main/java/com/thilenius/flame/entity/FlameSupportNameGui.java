package com.thilenius.flame.entity;

import com.thilenius.flame.spark.SparkTileEntity;
import com.thilenius.flame.utilities.types.Location3D;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Alec on 3/27/15.
 */
public class FlameSupportNameGui extends GuiScreen {

    // Sizes
    private static int WINDOW_WIDTH = 180;
    private static int WINDOW_HEIGHT = 160;

    private static int TEXT_BOX_X = 20;
    private static int TEXT_BOX_Y = 20;

    // GUI Components
    private GuiTextField m_nameTextBox;
    private GuiButton m_okButton;
    private GuiButton m_cancelButton;

    private Location3D m_activeLocation;
    private FlameTileEntity m_activeEntity;

    public FlameSupportNameGui(Location3D activeLocation, FlameTileEntity activeEntity) {
        m_activeLocation = activeLocation;
        m_activeEntity = activeEntity;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        int posX = (width - WINDOW_WIDTH) / 2;
        int posY = (height - WINDOW_HEIGHT) / 2;

        // Constructors
        m_nameTextBox = new GuiTextField(fontRendererObj,
                posX + 20,      posY + 30,     140,    20);
        m_cancelButton = new GuiButton(1,
                posX + 20,      posY + 60,     60,     20, "Cancel");
        m_okButton = new GuiButton(0,
                posX + 100,     posY + 60,     60,     20, "Okay");

        // Attributes
        m_nameTextBox.setFocused(true);
        m_nameTextBox.setMaxStringLength(100);

        // MISX
        buttonList.add(m_cancelButton);
        buttonList.add(m_okButton);
    }

    @Override
    public void drawScreen(int x, int y, float f)
    {
        ResourceLocation texture = (new ResourceLocation("flame:textures/gui/Background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);

        // Draw GUI background image
        int guiX = (width - WINDOW_WIDTH) / 2;
        int guiY = (height - WINDOW_HEIGHT) / 2;
        this.drawTexturedModalRect(guiX, guiY, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Label
        drawString(fontRendererObj, "Name", guiX + 20, guiY + 15, 0xFFFFFF);

        // Text Box
        m_nameTextBox.drawTextBox();
        super.drawScreen(x, y, f);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                System.out.println("Creating FlameTileEntity");
                TileEntity tileEntity = this.mc.theWorld.getTileEntity(m_activeLocation.X, m_activeLocation.Y,
                        m_activeLocation.Z);
                if (tileEntity == null || !(tileEntity instanceof FlameTileEntity)) {
                    System.out.println("Failed to find FlameTileEntity to send message to");
                    break;
                }

                FlameTileEntity flameTileEntity = (FlameTileEntity) tileEntity;
                flameTileEntity.setName(m_nameTextBox.getText());
                flameTileEntity.setPlayerName(mc.thePlayer.getGameProfile().getName());
                flameTileEntity.postNamedInit();
                this.mc.thePlayer.closeScreen();
                break;
            case 1:
                System.out.println("Attempting to cancel FlameTileEntity placement");

                // HACK: This 'should' remove the tile entity... I hope
                SparkTileEntity spark = null;
                for (Object obj : this.mc.theWorld.loadedTileEntityList) {
                    if (obj instanceof TileEntity) {
                        TileEntity loadedTileEntity = (TileEntity) obj;
                        if (loadedTileEntity.xCoord == m_activeLocation.X &&
                                loadedTileEntity.yCoord == m_activeLocation.Y &&
                                loadedTileEntity.zCoord == m_activeLocation.Z) {
                            spark = (SparkTileEntity) loadedTileEntity;
                            break;
                        }
                    }
                }

                this.mc.theWorld.setBlockToAir(m_activeLocation.X, m_activeLocation.Y, m_activeLocation.Z);
                this.mc.thePlayer.closeScreen();
                break;
            default:
                System.out.println("Failed to handle message for button with id " + button.id);
        }
    }

    @Override
    public void keyTyped(char c, int i){
        super.keyTyped(c, i);
        m_nameTextBox.textboxKeyTyped(c, i);
    }

    @Override
    public void mouseClicked(int x, int y, int k){
        super.mouseClicked(x, y, k);
        m_nameTextBox.mouseClicked(x, y, k);
    }

}
