package com.thilenius.blaze.assignment.blank;

import com.thilenius.blaze.assignment.BlazeAssignment;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 1/17/15.
 */
public class BlankAssignment extends BlazeAssignment {

    private BlankLevel m_blankLevel = new BlankLevel();

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
            case 0: return m_blankLevel;
            default: return null;
        }
    }

}
