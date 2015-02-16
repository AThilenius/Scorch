package com.thilenius.blaze.assignment.aone;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

// Write a function that takes in a space delimited string, Ex. Forward Forward Backward TurnLeft TurnRight
// 15 : Complete
public class BLevelOne extends BlazeLevel {

    private BlazeSpark m_spark;
    private Location3D m_sparkSpawn;
    private int m_currentSequenceOffset;
    // Forward Forward Backward TurnLeft TurnLeft TurnRight Forward Forward
    private BFEProtos.BFESparkCommand.CommandType[] m_validSequence = new BFEProtos.BFESparkCommand.CommandType[] {
            BFEProtos.BFESparkCommand.CommandType.MOVE_FORWARD,
            BFEProtos.BFESparkCommand.CommandType.MOVE_FORWARD,
            BFEProtos.BFESparkCommand.CommandType.MOVE_BACKWARD,
            BFEProtos.BFESparkCommand.CommandType.TURN_LEFT,
            BFEProtos.BFESparkCommand.CommandType.TURN_LEFT,
            BFEProtos.BFESparkCommand.CommandType.TURN_RIGHT,
            BFEProtos.BFESparkCommand.CommandType.MOVE_FORWARD,
            BFEProtos.BFESparkCommand.CommandType.MOVE_FORWARD
    };

    @Override
    public void load(AssignmentQuery assignmentQuery, int points) {
        super.load(assignmentQuery, points);

        // Spawn a single Spark
        m_sparkSpawn = new Location3D(getArenaLocation().X + (ArenaSize / 2), getArenaLocation().Y + 1,
                getArenaLocation().Z + (ArenaSize / 2));

        m_spark = new BlazeSpark(m_sparkSpawn);

        // Reset Sequence
        m_currentSequenceOffset = 0;

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

        // Reset Sequence
        m_currentSequenceOffset = 0;

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
        if (m_currentSequenceOffset >= 0 && request.getCommand() == m_validSequence[m_currentSequenceOffset]) {

            // Done?
            if (m_currentSequenceOffset == 7) {
                setPoints(15);
                drawBorder(1, 1, Blocks.emerald_block);
            }

            m_currentSequenceOffset++;
        } else {
            // Wrong move
            m_currentSequenceOffset = -1;
        }
    }

    @Override
    public BlazeSpark[] getSparks() {
        return new BlazeSpark[]{m_spark};
    }


}
