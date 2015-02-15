package com.thilenius.blaze.assignment.aone;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.init.Blocks;

// Load text file, pass it to the function from Level 2
// 20 : Complete
public class CLevelThree extends BlazeLevel {

    private BlazeSpark m_spark;
    private Location3D m_sparkSpawn;

    @Override
    public void load(AssignmentQuery assignmentQuery, int points) {
        super.load(assignmentQuery, points);

        // Spawn a single Spark
        m_sparkSpawn = new Location3D(getArenaLocation().X + (ArenaSize / 2), getArenaLocation().Y + 1,
                getArenaLocation().Z + (ArenaSize / 2));

        m_spark = new BlazeSpark(m_sparkSpawn);

        // Always done
        drawBorder(1, 1, Blocks.emerald_block);
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

        // Always done
        drawBorder(1, 1, Blocks.emerald_block);
    }

    @Override
    public BlazeSpark[] getSparks() {
        return new BlazeSpark[]{m_spark};
    }
}
