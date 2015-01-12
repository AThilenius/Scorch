package com.thilenius.blaze;

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

/**
 * Created by Alec on 10/24/14.
 */
public class Blaze {
    public static Connection SqlInstance;
    public static BlazeWorld World;
    public static Flame FlameInstance;
    public static BlazeFrontEnd FrontEndServer;

    public Blaze (Flame flame) {
        FlameInstance = flame;
        World = new BlazeWorld(MinecraftServer.getServer().worldServers[0]);
        FrontEndServer = new BlazeFrontEnd();
        FrontEndServer.startServer();

        try
        {
            // First try to connect to production.
            try {
                System.out.println("Trying to connect DEVELOPMENT AWS RDS: MySQL");
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                SqlInstance = DriverManager.getConnection(
                                "jdbc:mysql://forge-dev.cfqsj371kgit.us-west-1.rds.amazonaws.com:3306/forgedb?" +
                                "user=admin&password=forgeadmin");
                System.out.println("Connected to DEVELOPMENT AWS RDS");
                return;
            } catch(SQLException e) {
                // Do nothing, fallback to Dev
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (InstantiationException e) {
                System.out.println(e.getMessage());
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("Failed to connect. Falling back to Development DB: SqlLite");
            Class.forName("org.sqlite.JDBC");
            SqlInstance = DriverManager.getConnection(
                    "jdbc:sqlite:/Users/Alec/Documents/Development/Forge/db/development.sqlite3");


            System.out.println("Connected to SQL Lite.");
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
    }
}
