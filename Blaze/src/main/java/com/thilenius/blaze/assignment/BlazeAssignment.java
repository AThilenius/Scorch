package com.thilenius.blaze.assignment;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by Alec on 11/15/14.
 */
public abstract class BlazeAssignment {
    private Location3D m_arenaLocation;

    public abstract BlazeLevel getLevel(int number);

    public void load(Location3D arenaLocation) {
        m_arenaLocation = arenaLocation;
    }

    public void unload() {

    }

    public void reload() {

    }

    public Location3D getArenaLocation() {
        return m_arenaLocation;
    }

    protected void setBlock(int x, int y, int z, Block block) {
        if (x < 0 || y < 0 || z < 0 ||
                x >= BlazeLevel.ArenaSize || y >= BlazeLevel.ArenaSize || z >= BlazeLevel.ArenaSize ) {
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
        for (int x = m_arenaLocation.X + 1; x < m_arenaLocation.X + BlazeLevel.ArenaSize - 2; x++) {
            for (int y = m_arenaLocation.Y + 1; y < m_arenaLocation.Y + BlazeLevel.ArenaSize - 2; y++) {
                for (int z = m_arenaLocation.Z + 1; z < m_arenaLocation.Z + BlazeLevel.ArenaSize - 2; z++) {
                    if (Blaze.World.MinecraftWorld.getBlock(x, y, z).getMaterial() != Material.air) {
                        Blaze.World.MinecraftWorld.setBlockToAir(x, y, z);
                    }
                }
            }
        }
    }

    protected void drawFloor(int yLevel, Block block) {
        if (yLevel < 0 || yLevel >= BlazeLevel.ArenaSize) {
            return;
        }

        yLevel += m_arenaLocation.Y;
        for (int x = m_arenaLocation.X; x < m_arenaLocation.X + BlazeLevel.ArenaSize; x++) {
            for (int z = m_arenaLocation.Z; z < m_arenaLocation.Z + BlazeLevel.ArenaSize; z++) {
                if (Blaze.World.MinecraftWorld.getBlock(x, yLevel, z) != block) {
                    Blaze.World.MinecraftWorld.setBlock(x, yLevel, z, block);
                }
            }
        }

        block.setLightOpacity(0);
    }

    protected void drawBorder(int yLevel, int height, Block block) {
        if (yLevel < 0 || yLevel >= BlazeLevel.ArenaSize || yLevel + height >= BlazeLevel.ArenaSize) {
            return;
        }

        yLevel += m_arenaLocation.Y;
        for (int x = m_arenaLocation.X; x < m_arenaLocation.X + BlazeLevel.ArenaSize; x++) {
            // X direction
            for (int y = yLevel; y < yLevel + height; y++) {
                if (Blaze.World.MinecraftWorld.getBlock(x, yLevel, m_arenaLocation.Z) != block) {
                    Blaze.World.MinecraftWorld.setBlock(x, yLevel, m_arenaLocation.Z, block);
                }
                if (Blaze.World.MinecraftWorld.getBlock(x, yLevel, m_arenaLocation.Z + BlazeLevel.ArenaSize - 1) != block) {
                    Blaze.World.MinecraftWorld.setBlock(x, yLevel, m_arenaLocation.Z + BlazeLevel.ArenaSize - 1, block);
                }
            }
        }

        for (int z = m_arenaLocation.Z; z < m_arenaLocation.Z + BlazeLevel.ArenaSize; z++) {
            // Z direction
            for (int y = yLevel; y < yLevel + height; y++) {
                if (Blaze.World.MinecraftWorld.getBlock(m_arenaLocation.X, yLevel, z) != block) {
                    Blaze.World.MinecraftWorld.setBlock(m_arenaLocation.X, yLevel, z, block);
                }
                if (Blaze.World.MinecraftWorld.getBlock(m_arenaLocation.X + BlazeLevel.ArenaSize - 1, yLevel, z) != block) {
                    Blaze.World.MinecraftWorld.setBlock(m_arenaLocation.X + BlazeLevel.ArenaSize - 1, yLevel, z, block);
                }
            }
        }
    }

}
