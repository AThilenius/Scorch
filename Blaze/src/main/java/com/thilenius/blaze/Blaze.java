package com.thilenius.blaze;

import com.thilenius.blaze.data.RemoteData;
import com.thilenius.blaze.frontend.BlazeFrontEnd;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.flame.Flame;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.server.MinecraftServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Alec on 10/24/14.
 */
public class Blaze {
    // TODO: I really hate that these are static...
    public static RemoteData RemoteDataConnection;
    public static BlazeWorld World;
    public static Flame FlameInstance;
    public static BlazeFrontEnd FrontEndServer;
    public static ExecutorService ThreadPool = Executors.newFixedThreadPool(128);

    private static List<Runnable> m_gameLoopMarshalQueue = new LinkedList<Runnable>();

    public static void marshalToGameLoop(Runnable runnable) {
        synchronized (m_gameLoopMarshalQueue) {
            m_gameLoopMarshalQueue.add(runnable);
        }
    }

    public Blaze (Flame flame) {
        FlameInstance = flame;
        World = new BlazeWorld(MinecraftServer.getServer().worldServers[0]);
        FrontEndServer = new BlazeFrontEnd();
        FrontEndServer.startServer();
        RemoteDataConnection = new RemoteData();
        RemoteDataConnection.connect();
    }

    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent joinEvent) {
        World.OnPlayerJoinWorld(joinEvent.player);
    }

    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent leaveEvent) {

    }

    public String onFormatName (String username) {
       // BlazePlayer player = World.BlazePlayersByUsername.get(username);

//        if (player != null) {
//            // Lookup name in database
//            return "";
//        } else {
//            return username;
//        }

        //TODO: Fix this
        return "TODO Blaze.Java 58";
    }

    public static void onTick() {
        if (FrontEndServer != null) {
            FrontEndServer.onTick();
        }

        // Run marshaled tasks
        synchronized (m_gameLoopMarshalQueue) {
            for (Runnable runnable : m_gameLoopMarshalQueue) {
                runnable.run();
            }
            m_gameLoopMarshalQueue.clear();
        }
    }
}
