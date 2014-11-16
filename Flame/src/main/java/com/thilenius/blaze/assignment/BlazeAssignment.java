package com.thilenius.blaze.assignment;

import com.thilenius.blaze.player.BlazePlayer;

/**
 * Created by Alec on 11/15/14.
 */
public abstract class BlazeAssignment {

    protected BlazePlayer m_player;
    private BlazeLevel m_activeLevel;

    public BlazeAssignment(BlazePlayer player) {
        m_player = player;
    }

    public abstract int getLevelsCount();
    protected abstract BlazeLevel getLevel(int number);


    public BlazeLevel loadLevel (int number) {
        if (number >= getLevelsCount()) {
            return null;
        }

        if (m_activeLevel != null) {
            m_activeLevel.unload();
        }

        m_activeLevel = getLevel(number);
        m_activeLevel.load();
        return m_activeLevel;
    }

    public BlazeLevel getActiveLevel() {
        return m_activeLevel;
    }

    public void unload() {
        if (m_activeLevel != null) {
            m_activeLevel.unload();
        }
    }
}
