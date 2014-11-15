package com.thilenius.blaze.player;

import com.thilenius.blaze.BlazeWorld;
import com.thilenius.blaze.redis.RedisArena;
import com.thilenius.utilities.types.Location2D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 10/24/14.
 */
public class PlayerArena {

    public static final int Size = 30;

    public RedisArena ArenaData;

    private BlazeWorld m_world;
    private BlazePlayer m_player;

    public PlayerArena(BlazeWorld world, BlazePlayer player) {
        m_world = world;
        m_player = player;
        ArenaData = new RedisArena(player.PlayerData.getDisplayName());
    }

    public void ResetArena() {

        Location2D location = ArenaData.getLocation();

        // Hard coded for now.
        // Clear everything in the column
        for (int x = location.X; x < location.X + Size; x++) {
            for (int z = location.Y; z < location.Y + Size; z++) {

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
                if ( (x == location.X || x == location.X + Size - 1) ||
                     (z == location.Y || z == location.Y + Size - 1) ) {
                    m_world.MinecraftWorld.setBlock(x, 10, z, Blocks.glass);
                }
            }
        }

    }

}
