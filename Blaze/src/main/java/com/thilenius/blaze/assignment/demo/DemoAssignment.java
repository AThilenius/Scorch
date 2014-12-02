package com.thilenius.blaze.assignment.demo;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.assignment.BlazeAssignment;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 11/15/14.
 */
public class DemoAssignment extends BlazeAssignment {

    private DemoLevelOne m_levelOne;
    private DemoLevelTwo m_levelTwo;

    public DemoAssignment() {
        m_levelOne = new DemoLevelOne();
        m_levelTwo = new DemoLevelTwo();
    }

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
            default: return null;
        }
    }

}
