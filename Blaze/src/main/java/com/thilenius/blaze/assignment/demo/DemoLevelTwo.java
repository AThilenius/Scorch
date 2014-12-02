package com.thilenius.blaze.assignment.demo;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.blaze.player.PlayerArena;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 11/15/14.
 */
public class DemoLevelTwo extends BlazeLevel {

    private BlazeSpark m_spark;
    private Location3D m_sparkSpawn;
    private Location3D m_levelCompleteLocation;

    @Override
    public void load(Location3D arenaLocation, int seed, int userLevelId) {
        super.load(arenaLocation, seed, userLevelId);

        // Spawn a single Spark
        m_sparkSpawn = new Location3D(getArenaLocation().X + (ArenaSize / 2), getArenaLocation().Y + 1,
                getArenaLocation().Z + (ArenaSize / 2));
        m_levelCompleteLocation = new Location3D(getArenaLocation().X + (ArenaSize / 2), getArenaLocation().Y + 1,
                getArenaLocation().Z + (ArenaSize / 2) - 1);

        m_spark = new BlazeSpark(m_sparkSpawn);
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

        if (m_spark.getLocation().equals(m_levelCompleteLocation)) {
            setPoints(5);
            drawBorder(1, 1, Blocks.emerald_block);
        }
    }

    @Override
    public BlazeSpark[] getSparks() {
        return new BlazeSpark[]{m_spark};
    }


}
