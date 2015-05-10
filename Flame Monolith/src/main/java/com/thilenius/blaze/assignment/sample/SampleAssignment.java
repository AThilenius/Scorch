package com.thilenius.blaze.assignment.sample;

import com.thilenius.blaze.assignment.BlazeAssignment;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 11/15/14.
 */
public class SampleAssignment extends BlazeAssignment {

    private ALevelOne m_levelOne = new ALevelOne();
    private BLevelTwo m_levelTwo = new BLevelTwo();
    private CLevelThree m_levelThree = new CLevelThree();
    private DLevelFour m_levelFour = new DLevelFour();

    @Override
    public void load(Location3D arenaLocation, String displayName) {
        super.load(arenaLocation, displayName);
        clear();
        drawFloor(0, Blocks.quartz_block);
    }

    @Override
    public void reload() {
        super.reload();
        clear();
    }

    @Override
    public BlazeLevel getLevel(int number) {
        switch (number)
        {
            case 0: return m_levelOne;
            case 1: return m_levelTwo;
            case 2: return m_levelThree;
            case 3: return m_levelFour;
            default: return null;
        }
    }

}
