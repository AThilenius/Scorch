package com.thilenius.blaze.assignment.demo;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 12/2/14.
 */
public class FourMaze extends BlazeLevel {

    private BlazeSpark m_spark;
    private Location3D m_sparkSpawn;
    private Location3D[] m_mazeOffsets = new Location3D[] {
            new Location3D(-1, 0, 0),
            new Location3D(0, 0, 1),
            new Location3D(1, 0, 0),
            new Location3D(1, 0, 1),
            new Location3D(1, 0, -1),
            new Location3D(1, 0, -2),
            new Location3D(0, 0, -2),
            new Location3D(-1, 0, -2),
            new Location3D(-2, 0, -2),
            new Location3D(-3, 0, -2),
            new Location3D(-3, 0, -1),
            new Location3D(-3, 0, 0),
            new Location3D(-3, 0, 1),
            new Location3D(-3, 0, 2),
            new Location3D(-2, 0, 2),
            new Location3D(-1, 0, 2),
            new Location3D(0, 0, 2)
    };

    @Override
    public void load(Location3D arenaLocation, int seed, int userLevelId) {
        super.load(arenaLocation, seed, userLevelId);

        // Spawn a single Spark
        m_sparkSpawn = new Location3D(getArenaLocation().X + (ArenaSize / 2), getArenaLocation().Y + 1,
                getArenaLocation().Z + (ArenaSize / 2));

        m_spark = new BlazeSpark(m_sparkSpawn);

        // Draw Maze
        drawOffsetList(new Location3D(m_sparkSpawn.X, 1, m_sparkSpawn.Z), m_mazeOffsets, Blocks.glass);

        // Already done?
        if (getPoints() == 5) {
            drawBorder(1, 1, Blocks.emerald_block);
        }
    }

    @Override
    public void unload() {
        super.unload();
        m_spark = null;
    }

    @Override
    public void reload() {
        super.reload();

        // Spawn a new Spark
        m_spark = new BlazeSpark(m_sparkSpawn);
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
