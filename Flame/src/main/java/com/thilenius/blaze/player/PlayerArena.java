package com.thilenius.blaze.player;

import com.thilenius.blaze.BlazeWorld;
import com.thilenius.blaze.redis.RedisArena;

/**
 * Created by Alec on 10/24/14.
 */
public class PlayerArena {

    public static final int Size = 32;

    public RedisArena ArenaData;

    public BlazeWorld MinecraftWorld;
    public BlazePlayer BlazePlayer;

    public PlayerArena(BlazeWorld world, BlazePlayer player) {
        MinecraftWorld = world;
        BlazePlayer = player;
        ArenaData = new RedisArena(player.PlayerData.getDisplayName());
    }

}
