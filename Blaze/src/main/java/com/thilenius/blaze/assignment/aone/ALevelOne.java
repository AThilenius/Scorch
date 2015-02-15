package com.thilenius.blaze.assignment.aone;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

// 5 : Load Level
// 5 : Move Forward/Backward
// 5 : Turn Left/Right
public class ALevelOne extends BlazeLevel {

    private BlazeSpark m_spark;
    private Location3D m_sparkSpawn;

    @Override
    public void load(AssignmentQuery assignmentQuery, int points) {
        super.load(assignmentQuery, points);

        // Spawn a single Spark
        m_sparkSpawn = new Location3D(getArenaLocation().X + (ArenaSize / 2), getArenaLocation().Y + 1,
                getArenaLocation().Z + (ArenaSize / 2));

        m_spark = new BlazeSpark(m_sparkSpawn);

        // 5 points for loading the level
        setPoints(5);

        // Already done?
        if (getPoints() == 15) {
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
        if (getPoints() == 15) {
            drawBorder(1, 1, Blocks.emerald_block);
        } else {
            drawBorder(1, 1, Blocks.redstone_block);
        }
    }

    @Override
    public void grade(BFEProtos.BFESparkCommand request) {
        super.grade(request);

        // Forward / Backward
        if (request.getCommand() == BFEProtos.BFESparkCommand.CommandType.MOVE_FORWARD ||
                request.getCommand() == BFEProtos.BFESparkCommand.CommandType.MOVE_BACKWARD) {
            int currentPoints = getPoints();
            if (currentPoints == 5) {
                setPoints(10);
            } else if (currentPoints == 10) {
                setPoints(15);
                drawBorder(1, 1, Blocks.emerald_block);
            }
        }

        // Left / Right
        if (request.getCommand() == BFEProtos.BFESparkCommand.CommandType.TURN_LEFT ||
                request.getCommand() == BFEProtos.BFESparkCommand.CommandType.TURN_RIGHT) {
            int currentPoints = getPoints();
            if (currentPoints == 5) {
                setPoints(10);
            } else if (currentPoints == 10) {
                setPoints(15);
                drawBorder(1, 1, Blocks.emerald_block);
            }
        }
    }

    @Override
    public BlazeSpark[] getSparks() {
        return new BlazeSpark[]{m_spark};
    }


}