package com.thilenius.flame.entity;

import com.thilenius.flame.utilities.types.Location3D;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Alec on 3/27/15.
 */
public class FlameSupportGuiHandler implements IGuiHandler {

    public static Location3D ActiveLocation;
    public static FlameTileEntity ActiveEntity;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Object returnItem = null;

        switch (ID) {
            case 0:
                if (ActiveLocation == null || ActiveEntity == null) {
                    System.out.println("You forgot to set the global objects needed by this GUI");
                }
                returnItem = new FlameSupportNameGui(ActiveLocation, ActiveEntity);
                break;
        }

        ActiveLocation = null;
        ActiveEntity = null;

        return returnItem;
    }
}
