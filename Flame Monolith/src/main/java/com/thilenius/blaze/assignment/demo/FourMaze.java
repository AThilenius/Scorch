package com.thilenius.blaze.assignment.demo;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Alec on 12/2/14.
 */
public class FourMaze extends BlazeLevel {

    private BlazeSpark m_spark;
    private Location3D m_sparkSpawn;
    private Location3D m_finishLocation;
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
    public void load(AssignmentQuery assignmentQuery, int points) {
        super.load(assignmentQuery, points);

        // Spawn a single Spark
        m_sparkSpawn = new Location3D(getArenaLocation().X + (ArenaSize / 2), getArenaLocation().Y + 1,
                getArenaLocation().Z + (ArenaSize / 2));
        m_finishLocation = new Location3D(getArenaLocation().X + (ArenaSize / 2) - 1, getArenaLocation().Y + 1,
                getArenaLocation().Z + (ArenaSize / 2) + 1);

        m_spark = new BlazeSpark(m_sparkSpawn);

        // Draw Maze
        drawOffsetList(new Location3D((ArenaSize / 2), 1, (ArenaSize / 2)), m_mazeOffsets, Blocks.glass);
        setBlock((ArenaSize / 2) - 1, 0, (ArenaSize / 2) + 1, Blocks.gold_block);

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

        // Return gold block back to Quartz
        setBlock(getArenaLocation().X + (ArenaSize / 2) - 1, getArenaLocation().Y,
                getArenaLocation().Z + (ArenaSize / 2) + 1, Blocks.quartz_block);
    }

    @Override
    public void reload(int points) {
        super.reload(points);

        // Spawn a new Spark
        m_spark = new BlazeSpark(m_sparkSpawn);

        // Draw Maze
        drawOffsetList(new Location3D((ArenaSize / 2), 1, (ArenaSize / 2)), m_mazeOffsets, Blocks.glass);
        setBlock((ArenaSize / 2) - 1, 0, (ArenaSize / 2) + 1, Blocks.gold_block);

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

        // Failure case
        if (m_spark.getBlockLocationFromCommand(request.getCommand()).equals(new Location3D(
                getArenaLocation().X + (ArenaSize / 2), getArenaLocation().Y,
                getArenaLocation().Z + (ArenaSize / 2) - 2))) {
            // Get and blow up TNT
//            TileEntity tnt = Blaze.World.MinecraftWorld.getTileEntity(
//                    getArenaLocation().X + (ArenaSize / 2), getArenaLocation().Y,
//                    getArenaLocation().Z + (ArenaSize / 2) - 2);
//            Blaze.World.MinecraftWorld.createExplosion(null, getArenaLocation().X + (ArenaSize / 2), getArenaLocation().Y,
//                    getArenaLocation().Z + (ArenaSize / 2) - 2, 10);
        }

        if (m_spark.getLocation().equals(m_finishLocation)) {
            setPoints(5);
            drawBorder(1, 1, Blocks.emerald_block);
        }
    }

    @Override
    public BlazeSpark[] getSparks() {
        return new BlazeSpark[]{m_spark};
    }
}
