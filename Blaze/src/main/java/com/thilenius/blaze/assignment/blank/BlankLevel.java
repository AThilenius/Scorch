package com.thilenius.blaze.assignment.blank;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.spark.BlazeSpark;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 1/17/15.
 */
public class BlankLevel extends BlazeLevel {

    @Override
    public void load(AssignmentQuery assignmentQuery, int points) {
        super.load(assignmentQuery, points);

        // All that needs to be done for this assignment is to connect
        drawBorder(1, 1, Blocks.glass);
    }

    @Override
    public BlazeSpark[] getSparks() {
        return new BlazeSpark[]{};
    }

}