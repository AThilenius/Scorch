package com.thilenius.blaze.assignment.aone;

import com.thilenius.blaze.assignment.BlazeAssignment;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 11/15/14.
 */
public class ScorchAlphaAssignment extends BlazeAssignment {

    private ALevelZero m_levelZero = new ALevelZero();
    private BLevelOne m_levelOne = new BLevelOne();
    private CLevelTwo m_levelTwo = new CLevelTwo();

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
            case 0: return m_levelZero;
            case 1: return m_levelOne;
            case 2: return m_levelTwo;
            default: return null;
        }
    }

}
