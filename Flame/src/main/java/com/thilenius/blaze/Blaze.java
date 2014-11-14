package com.thilenius.blaze;

import com.thilenius.blaze.frontend.BFEServer;
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
    public static BFEServer FrontEndServer;

    public Blaze (Flame flame) {
        RedisInstance = new Jedis("localhost");
        FlameInstance = flame;
        World = new BlazeWorld(MinecraftServer.getServer().worldServers[0]);
        FrontEndServer = new BFEServer();
        FrontEndServer.startServer();
    }

    public void OnPlayerJoin(PlayerEvent.PlayerLoggedInEvent joinEvent) {
        World.OnPlayerJoinWorld(joinEvent.player);
    }

    public void OnPlayerLeave(PlayerEvent.PlayerLoggedOutEvent leaveEvent) {

    }

}
