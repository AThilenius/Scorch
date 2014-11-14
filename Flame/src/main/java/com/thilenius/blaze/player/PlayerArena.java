package com.thilenius.blaze.player;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.BlazeWorld;
import com.thilenius.utilities.types.Location2D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 10/24/14.
 */
public class PlayerArena {

    public static final int Size = 30;

    private String m_redisPrefix;
    private BlazeWorld m_world;
    private BlazePlayer m_player;
    private Location2D m_location;

    public PlayerArena(BlazeWorld world, String username) {
        m_redisPrefix = "arena_[" + player.getUserName() + "]_";
        m_world = world;
        m_player = player;
        m_location = new Location2D(Blaze.RedisInstance.get(m_redisPrefix + "location"));
    }

    public Location2D getLocation() {
        return m_location;
    }

    public void ResetArena() {

        // Hard coded for now.
        // Clear everything in the column
        for (int x = m_location.X; x < m_location.X + Size; x++) {
            for (int z = m_location.Y; z < m_location.Y + Size; z++) {

                // Bedrock
                m_world.MinecraftWorld.setBlock(x, 0, z, Blocks.bedrock);

                // Stone
                for (int y = 1; y < 10; y++) {
                    m_world.MinecraftWorld.setBlock(x, y, z, Blocks.stone);
                }

                // Air
                for (int y = 10; y < m_world.MinecraftWorld.getHeight(); y++) {
                    if (m_world.MinecraftWorld.getBlock(x, y, z) != Blocks.air) {
                        m_world.MinecraftWorld.setBlockToAir(x, y, z);
                    }
                }

                // Border
                if ( (x == m_location.X || x == m_location.X + Size - 1) ||
                     (z == m_location.Y || z == m_location.Y + Size - 1) ) {
                    m_world.MinecraftWorld.setBlock(x, 10, z, Blocks.glass);
                }
            }
        }

        //Player.MinecraftPlayer.setPosition(StartLocation.X + 10, 50, StartLocation.Y + 10);
    }

}
