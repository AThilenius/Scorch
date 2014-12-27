package com.thilenius.blaze.player;

import com.thilenius.utilities.types.Location3D;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Alec on 11/6/14.
 */
public class BlazePlayer {

    public EntityPlayer MinecraftPlayer;
    public PlayerArena Arena;

    public BlazePlayer(String username) {

    }

    public void onMinecraftPlayerLogin (EntityPlayer minecraftPlayer) {
        System.out.println("Player Login: " + minecraftPlayer.getDisplayName());
        MinecraftPlayer = minecraftPlayer;
    }

    public void setLocation(Location3D location) {
        //TODO: Fix spawn location from SQL
        MinecraftPlayer.setPositionAndUpdate(location.X, location.Y, location.Z);
    }

    public void onMinecraftPlayerLogout () {
        MinecraftPlayer = null;
    }

}
