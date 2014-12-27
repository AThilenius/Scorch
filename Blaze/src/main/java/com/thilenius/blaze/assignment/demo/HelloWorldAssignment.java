package com.thilenius.blaze.assignment.demo;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.assignment.BlazeAssignment;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.flame.Flame;
import com.thilenius.flame.jumbotron.JumboTileEntity;
import com.thilenius.flame.spark.SparkTileEntity;
import com.thilenius.utilities.types.Location2D;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Alec on 11/15/14.
 */
public class HelloWorldAssignment extends BlazeAssignment {

    private OneLoadLevel m_levelOne = new OneLoadLevel();
    private TwoMoveForward m_levelTwo = new TwoMoveForward();
    private ThreeTurnSpark m_levelThree = new ThreeTurnSpark();
    private FourMaze m_levelFour = new FourMaze();

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
