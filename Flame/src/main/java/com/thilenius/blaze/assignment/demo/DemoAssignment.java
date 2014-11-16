package com.thilenius.blaze.assignment.demo;

import com.thilenius.blaze.assignment.BlazeAssignment;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.player.BlazePlayer;

/**
 * Created by Alec on 11/15/14.
 */
public class DemoAssignment extends BlazeAssignment {

    private DemoLevelOne m_levelOne;
    private DemoLevelTwo m_levelTwo;

    public DemoAssignment(BlazePlayer player) {
        super(player);
        m_levelOne = new DemoLevelOne(player);
        m_levelTwo = new DemoLevelTwo(player);
    }

    @Override
    public int getLevelsCount() {
        return 2;
    }

    @Override
    protected BlazeLevel getLevel(int number) {
        switch (number)
        {
            case 0: return m_levelOne;
            case 1: return m_levelTwo;
            default: return null;
        }
    }

}
