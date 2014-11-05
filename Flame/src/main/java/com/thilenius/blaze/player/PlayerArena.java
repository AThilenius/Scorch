package com.thilenius.blaze.player;

import com.thilenius.blaze.FlamePlayer;
import com.thilenius.blaze.FlameWorld;
import com.thilenius.utilities.types.Location2D;
import net.minecraft.init.Blocks;

/**
 * Created by Alec on 10/24/14.
 */
public class PlayerArena {

    public static final int Size = 30;
    public FlameWorld World;
    public FlamePlayer Player;
    public Location2D StartLocation;

    public PlayerArena(FlameWorld world, FlamePlayer player, Location2D location) {
        World = world;
        Player = player;
        StartLocation = location;
    }

    public void ResetArena() {
        // Hard coded for now.
        // Clear everything in the column
        for (int x = StartLocation.X; x < StartLocation.X + Size; x++) {
            for (int z = StartLocation.Y; z < StartLocation.Y + Size; z++) {
                for (int y = 1; y < World.MinecraftWorld.getHeight(); y++)
                if (World.MinecraftWorld.getBlock(x, y, z) != Blocks.air) {
                    World.MinecraftWorld.setBlockToAir(x, y, z);
                }
            }
        }

        Player.MinecraftPlayer.setPosition(StartLocation.X, 50, StartLocation.Y);
    }

}
