package com.thilenius.blaze.assignment.sample;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.spark.BlazeSpark;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 11/15/14.
 */
public class ALevelOne extends BlazeLevel {

    @Override
    public void load(AssignmentQuery assignmentQuery, int points) {
        super.load(assignmentQuery, points);
        drawBorder(1, 1, Blocks.emerald_block);
    }

    @Override
    public void reload(int points) {
        super.reload(points);
        drawBorder(1, 1, Blocks.emerald_block);
    }

    @Override
    public BlazeSpark[] getSparks() {
        return new BlazeSpark[]{};
    }

}