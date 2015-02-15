package com.thilenius.blaze.assignment.aone;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.spark.BlazeSpark;
import net.minecraft.init.Blocks;

// Write a function that takes in a space delimited string, Ex. Forward Forward Backward TurnLeft TurnRight
// 15 : Complete
public class BLevelTwo extends BlazeLevel {

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
