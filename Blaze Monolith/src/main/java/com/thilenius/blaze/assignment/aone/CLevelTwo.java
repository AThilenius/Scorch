package com.thilenius.blaze.assignment.aone;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

// Load text file, pass it to the function from Level 2
// 20 : Complete
public class CLevelTwo extends BlazeLevel {

    private BlazeSpark m_spark;
    private Location3D m_sparkSpawn;
    private int m_currentSequenceOffset;
    // Backward Backward TurnRight Forward TurnLeft Forward Forward TurnLeft Backward
    private BFEProtos.BFESparkCommand.CommandType[] m_validSequence = new BFEProtos.BFESparkCommand.CommandType[] {
            BFEProtos.BFESparkCommand.CommandType.MOVE_BACKWARD,
            BFEProtos.BFESparkCommand.CommandType.MOVE_BACKWARD,
            BFEProtos.BFESparkCommand.CommandType.TURN_RIGHT,
            BFEProtos.BFESparkCommand.CommandType.MOVE_FORWARD,
            BFEProtos.BFESparkCommand.CommandType.TURN_LEFT,
            BFEProtos.BFESparkCommand.CommandType.MOVE_FORWARD,
            BFEProtos.BFESparkCommand.CommandType.MOVE_FORWARD,
            BFEProtos.BFESparkCommand.CommandType.TURN_LEFT,
            BFEProtos.BFESparkCommand.CommandType.MOVE_BACKWARD
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
        if (getPoints() == 20) {
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
        if (getPoints() == 20) {
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
            if (m_currentSequenceOffset == 8) {
                setPoints(20);
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
