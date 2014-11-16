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
    public BlazeLevel[] getAllLevels() {
        return new BlazeLevel[]{m_levelOne, m_levelTwo};
    }

}
