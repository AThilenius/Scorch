package com.thilenius.blaze.assignment;

import com.thilenius.blaze.player.BlazePlayer;

/**
 * Created by Alec on 11/15/14.
 */
public abstract class BlazeAssignment {

    protected BlazePlayer m_player;

    public BlazeAssignment(BlazePlayer player) {
        m_player = player;
    }

    public abstract BlazeLevel[] getAllLevels();
}
