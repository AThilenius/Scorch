package com.thilenius.blaze.assignment;

import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.blaze.player.PlayerArena;
import com.thilenius.blaze.spark.BlazeSpark;

import java.util.ArrayList;

/**
 * Created by Alec on 11/15/14.
 */
public abstract class BlazeLevel {
    public abstract void load(int seed);
    public abstract void unload();
    public abstract BlazeSpark[] getSparks();
}
