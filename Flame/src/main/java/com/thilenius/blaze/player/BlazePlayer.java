package com.thilenius.blaze.player;

import com.thilenius.blaze.redis.RedisPlayer;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Alec on 11/6/14.
 */
public class BlazePlayer {

    public EntityPlayer MinecraftPlayer;
    public RedisPlayer PlayerData;

    public BlazePlayer(String username) {
        PlayerData = new RedisPlayer(username);
    }

    public void onMinecraftPlayerLogin (EntityPlayer minecraftPlayer) {
        System.out.println("Player Login: " + minecraftPlayer.getDisplayName());
        MinecraftPlayer = minecraftPlayer;
        setLocation(PlayerData.getSpawnLocation());
    }

    public void setLocation(Location3D location) {
        MinecraftPlayer.setPositionAndUpdate(location.X, location.Y, location.Z);
    }

    public void onMinecraftPlayerLogout () {
        MinecraftPlayer = null;
    }

}
