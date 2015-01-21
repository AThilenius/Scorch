package com.thilenius.blaze;

import com.google.protobuf.InvalidProtocolBufferException;
import com.thilenius.blaze.data.RemoteData;
import com.thilenius.blaze.data.UserQuery;
import com.thilenius.blaze.frontend.BlazeFrontEnd;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.flame.Flame;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSettings;
import org.apache.commons.codec.binary.Base64;

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
        UserQuery userQuery = new UserQuery(joinEvent.player.getGameProfile().getName());
        if (!RemoteDataConnection.query(userQuery)) {
            // Unknown player, boot them.
            System.out.println("Unknown player trying to join Blaze: " + joinEvent.player.getGameProfile().getName() +
                    ". Booting them.");
            joinEvent.setCanceled(true);
        } else {
            // Load arena (if needed)
            FrontEndServer.setDefaults(userQuery);
            joinEvent.player.setPositionAndUpdate(userQuery.ArenaLocation.X + 16, userQuery.ArenaLocation.Y + 2,
                    userQuery.ArenaLocation.Z + 5);
        }
    }

    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent leaveEvent) {

    }

    public String onFormatName (String username) {
        UserQuery userQuery = new UserQuery(username);
        if (!RemoteDataConnection.query(userQuery)) {
            return username;
        } else {
            return userQuery.FirstName + " " + userQuery.LastName.substring(0, 1);
        }
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
