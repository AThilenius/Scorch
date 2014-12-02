package com.thilenius.blaze.assignment.demo;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 11/15/14.
 */
public class DemoLevelOne extends BlazeLevel {

    @Override
    public void load(Location3D arenaLocation, int seed, int userLevelId) {
        super.load(arenaLocation, seed, userLevelId);

        // All that needs to be done for this assignment is to connect
        setPoints(5);
        drawBorder(1, 1, Blocks.emerald_block);
    }

    @Override
    public BlazeSpark[] getSparks() {
        return new BlazeSpark[]{};
    }

}
