package com.thilenius.flame.entity;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Alec on 3/27/15.
 */
public class FlameSupportGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if ( ID == FlameSupportNameGui.GUI_ID )
            return new FlameSupportNameGui();

        return null;
    }
}
