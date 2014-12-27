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
            Class.forName("org.sqlite.JDBC");
            SqlInstance = DriverManager.getConnection(
                    "jdbc:sqlite:/Users/Alec/Documents/Development/Forge/db/development.sqlite3");

//            System.out.println("Trying to connect to AWS RDS: MySQL");
//            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
//            SqlInstance = DriverManager.getConnection(
//                    "jdbc:mysql:forge-dev.cfqsj371kgit.us-west-1.rds.amazonaws.com:3306/forgedb",
//                    "admin",
//                    "forgeadmin");
            System.out.println("Connected to SQL Lite.");
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
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
