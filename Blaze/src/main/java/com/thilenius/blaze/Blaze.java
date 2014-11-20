package com.thilenius.blaze;

import com.thilenius.blaze.frontend.BlazeFrontEnd;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.flame.Flame;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.server.MinecraftServer;
import redis.clients.jedis.Jedis;

/**
 * Created by Alec on 10/24/14.
 */
public class Blaze {

    public static Jedis RedisInstance;
    public static BlazeWorld World;
    public static Flame FlameInstance;
    public static BlazeFrontEnd FrontEndServer;

    public Blaze (Flame flame) {
        RedisInstance = new Jedis("localhost");
        FlameInstance = flame;
        World = new BlazeWorld(MinecraftServer.getServer().worldServers[0]);
        FrontEndServer = new BlazeFrontEnd();
        FrontEndServer.startServer();
    }

    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent joinEvent) {
        World.OnPlayerJoinWorld(joinEvent.player);
    }

    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent leaveEvent) {

    }

    public String onFormatName (String username) {
        BlazePlayer player = World.BlazePlayersByUsername.get(username);

        if (player != null) {
            return player.PlayerData.getDisplayName();
        } else {
            return username;
        }
    }

    public static void onTick() {
        if (FrontEndServer != null) {
            FrontEndServer.onTick();
        }
    }
}
