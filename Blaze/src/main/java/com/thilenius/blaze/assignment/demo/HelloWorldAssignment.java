package com.thilenius.blaze.assignment.demo;

import com.thilenius.blaze.assignment.BlazeAssignment;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 11/15/14.
 */
public class HelloWorldAssignment extends BlazeAssignment {

    private OneLoadLevel m_levelOne = new OneLoadLevel();
    private TwoMoveForward m_levelTwo = new TwoMoveForward();
    private ThreeTurnSpark m_levelThree = new ThreeTurnSpark();
    private FourMaze m_levelFour = new FourMaze();

    @Override
    public void load(Location3D arenaLocation) {
        super.load(arenaLocation);
        clear();
        drawFloor(0, Blocks.quartz_block);
        drawBorder(1, 1, Blocks.redstone_block);
    }

    @Override
    public void reload() {
        super.reload();
        clear();
        drawBorder(1, 1, Blocks.redstone_block);
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
