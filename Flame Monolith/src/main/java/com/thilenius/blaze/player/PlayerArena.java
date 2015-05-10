package com.thilenius.blaze.player;

import com.thilenius.blaze.BlazeWorld;

/**
 * Created by Alec on 10/24/14.
 */
public class PlayerArena {

    public static final int Size = 32;

    public BlazeWorld BlazeWorld;
    public BlazePlayer BlazePlayer;

    public PlayerArena(BlazeWorld world, BlazePlayer player) {
        BlazeWorld = world;
        BlazePlayer = player;
    }

}
