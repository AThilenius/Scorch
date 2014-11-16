package com.thilenius.blaze.assignment.demo;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.blaze.player.PlayerArena;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.utilities.types.Location2D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 11/15/14.
 */
public class DemoLevelOne extends BlazeLevel {

    private BlazePlayer m_player;
    private PlayerArena m_arena;

    public DemoLevelOne(BlazePlayer player) {
        m_player = player;
        m_arena = player.Arena;
    }

    @Override
    public void load() {
        Location2D location = m_arena.ArenaData.getLocation();

        // Hard coded for now.
        // Clear everything in the column
        for (int x = location.X; x < location.X + m_arena.Size; x++) {
            for (int z = location.Y; z < location.Y + m_arena.Size; z++) {

                // Bedrock
                m_arena.MinecraftWorld.MinecraftWorld.setBlock(x, 0, z, Blocks.bedrock);

                // Stone
                for (int y = 1; y < 3; y++) {
                    m_arena.MinecraftWorld.MinecraftWorld.setBlock(x, y, z, Blocks.gold_block);
                }

                // Air
                for (int y = 3; y < m_arena.MinecraftWorld.MinecraftWorld.getHeight(); y++) {
                    if (m_arena.MinecraftWorld.MinecraftWorld.getBlock(x, y, z) != Blocks.air) {
                        m_arena.MinecraftWorld.MinecraftWorld.setBlockToAir(x, y, z);
                    }
                }

                // Border
                if ( (x == location.X || x == location.X + m_arena.Size - 1) ||
                        (z == location.Y || z == location.Y + m_arena.Size - 1) ) {
                    m_arena.MinecraftWorld.MinecraftWorld.setBlock(x, 3, z, Blocks.glass);
                }
            }
        }
    }

    @Override
    public void unload() {

    }

    @Override
    public BlazeSpark[] getSparks() {
        return new BlazeSpark[0];
    }

}
