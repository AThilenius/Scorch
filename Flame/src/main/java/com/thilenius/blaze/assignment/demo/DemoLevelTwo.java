package com.thilenius.blaze.assignment.demo;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.blaze.player.PlayerArena;
import com.thilenius.blaze.spark.BlazeSpark;

/**
 * Created by Alec on 11/15/14.
 */
public class DemoLevelTwo extends BlazeLevel {

    private BlazePlayer m_player;
    private PlayerArena m_arena;

    public DemoLevelTwo(BlazePlayer player) {
        m_player = player;
        m_arena = player.Arena;
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Override
    public BlazeSpark[] getSparks() {
        return new BlazeSpark[0];
    }

}
