package com.thilenius.blaze.assignment.demo;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 12/2/14.
 */
public class ThreeTurnSpark extends BlazeLevel {

    private BlazeSpark m_spark;
    private Location3D m_sparkSpawn;

    @Override
    public void load(AssignmentQuery assignmentQuery, int points) {
        super.load(assignmentQuery, points);

        // Spawn a single Spark
        m_sparkSpawn = new Location3D(getArenaLocation().X + (ArenaSize / 2), getArenaLocation().Y + 1,
                getArenaLocation().Z + (ArenaSize / 2));

        m_spark = new BlazeSpark(m_sparkSpawn);

        // Already done?
        if (getPoints() == 5) {
            drawBorder(1, 1, Blocks.emerald_block);
        } else {
            drawBorder(1, 1, Blocks.redstone_block);
        }
    }

    @Override
    public void unload() {
        super.unload();
        m_spark = null;
    }

    @Override
    public void reload(int points) {
        super.reload(points);

        // Spawn a new Spark
        m_spark = new BlazeSpark(m_sparkSpawn);

        // Already done?
        if (getPoints() == 5) {
            drawBorder(1, 1, Blocks.emerald_block);
        } else {
            drawBorder(1, 1, Blocks.redstone_block);
        }
    }

    @Override
    public void grade(BFEProtos.BFESparkCommand request) {
        super.grade(request);

        if (request.getCommand() == BFEProtos.BFESparkCommand.CommandType.TURN_LEFT ||
            request.getCommand() == BFEProtos.BFESparkCommand.CommandType.TURN_RIGHT) {
            setPoints(5);
            drawBorder(1, 1, Blocks.emerald_block);
        }
    }

    @Override
    public BlazeSpark[] getSparks() {
        return new BlazeSpark[]{m_spark};
    }
}
