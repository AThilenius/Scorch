package com.thilenius.blaze.assignment;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.blaze.player.PlayerArena;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alec on 11/15/14.
 */
public abstract class BlazeLevel {
    public static final int ArenaSize = 32;
    private Location3D m_arenaLocation;
    private Random m_random;
    private int m_userLevelId;

    public void load(Location3D arenaLocation, int seed, int userLevelId) {
        m_arenaLocation = arenaLocation;
        m_random = new Random((long)seed);
        m_userLevelId = userLevelId;
    }

    public void unload() {}
    public void reload() {}

    public Location3D getArenaLocation() {
        return m_arenaLocation;
    }

    public abstract BlazeSpark[] getSparks();

    // Called AFTER the spark has been moved
    public void grade(BFEProtos.BFESparkCommand request) { }

    protected int getRandomInt() {
        return m_random.nextInt();
    }

    protected void setPoints(int points) {
        String SQL =
                "UPDATE user_levels\n" +
                "SET points = CASE WHEN points < " + points + "\n" +
                "THEN " + points + " ELSE points END\n" +
                "WHERE id=" + m_userLevelId;
        try {
            Statement statement = Blaze.SqlInstance.createStatement();
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected int getPoints() {
        String SQL =
                "SELECT points\n" +
                "FROM user_levels\n" +
                "WHERE id=" + m_userLevelId;
        try {
            Statement statement = Blaze.SqlInstance.createStatement();
            ResultSet rs = statement.executeQuery(SQL);
            rs.next();
            int points = rs.getInt("points");
            rs.next();
            return points;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    protected void setBlock(int x, int y, int z, Block block) {
        if (x < 0 || y < 0 || z < 0 ||
            x >= ArenaSize || y >= ArenaSize || z >= ArenaSize ) {
            return;
        }

        x += m_arenaLocation.X;
        y += m_arenaLocation.Y;
        z += m_arenaLocation.Z;

        if (Blaze.World.MinecraftWorld.getBlock(x, y, z) != block) {
            Blaze.World.MinecraftWorld.setBlock(x, y, z, block);
        }
    }

    protected void clear() {
        for (int x = m_arenaLocation.X + 1; x < m_arenaLocation.X + ArenaSize - 2; x++) {
            for (int y = m_arenaLocation.Y + 1; y < m_arenaLocation.Y + ArenaSize - 2; y++) {
                for (int z = m_arenaLocation.Z + 1; z < m_arenaLocation.Z + ArenaSize - 2; z++) {
                    if (Blaze.World.MinecraftWorld.getBlock(x, y, z).getMaterial() != Material.air) {
                        Blaze.World.MinecraftWorld.setBlockToAir(x, y, z);
                    }
                }
            }
        }
    }

    protected void drawFloor(int yLevel, Block block) {
        if (yLevel < 0 || yLevel >= ArenaSize) {
            return;
        }

        yLevel += m_arenaLocation.Y;
        for (int x = m_arenaLocation.X; x < m_arenaLocation.X + ArenaSize; x++) {
            for (int z = m_arenaLocation.Z; z < m_arenaLocation.Z + ArenaSize; z++) {
                if (Blaze.World.MinecraftWorld.getBlock(x, yLevel, z) != block) {
                    Blaze.World.MinecraftWorld.setBlock(x, yLevel, z, block);
                }
            }
        }

        block.setLightOpacity(0);
    }

    protected void drawBorder(int yLevel, int height, Block block) {
        if (yLevel < 0 || yLevel >= ArenaSize || yLevel + height >= ArenaSize) {
            return;
        }

        yLevel += m_arenaLocation.Y;
        for (int x = m_arenaLocation.X; x < m_arenaLocation.X + ArenaSize; x++) {
            // X direction
            for (int y = yLevel; y < yLevel + height; y++) {
                if (Blaze.World.MinecraftWorld.getBlock(x, yLevel, m_arenaLocation.Z) != block) {
                    Blaze.World.MinecraftWorld.setBlock(x, yLevel, m_arenaLocation.Z, block);
                }
                if (Blaze.World.MinecraftWorld.getBlock(x, yLevel, m_arenaLocation.Z + ArenaSize - 1) != block) {
                    Blaze.World.MinecraftWorld.setBlock(x, yLevel, m_arenaLocation.Z + ArenaSize - 1, block);
                }
            }
        }

        for (int z = m_arenaLocation.Z; z < m_arenaLocation.Z + ArenaSize; z++) {
            // Z direction
            for (int y = yLevel; y < yLevel + height; y++) {
                if (Blaze.World.MinecraftWorld.getBlock(m_arenaLocation.X, yLevel, z) != block) {
                    Blaze.World.MinecraftWorld.setBlock(m_arenaLocation.X, yLevel, z, block);
                }
                if (Blaze.World.MinecraftWorld.getBlock(m_arenaLocation.X + ArenaSize - 1, yLevel, z) != block) {
                    Blaze.World.MinecraftWorld.setBlock(m_arenaLocation.X + ArenaSize - 1, yLevel, z, block);
                }
            }
        }
    }

    protected void drawOffsetList(Location3D startLocation, Location3D[] offsets, Block block) {
        for (Location3D location : offsets) {
            setBlock(startLocation.X + location.X, startLocation.Y + location.Y, startLocation.Z + location.Z, block);
        }
    }

}
