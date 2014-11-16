package com.thilenius.blaze;

import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.blaze.player.PlayerArena;
import com.thilenius.blaze.redis.RedisPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Created by Alec on 11/6/14.
 */
public class BlazeWorld {

    public World MinecraftWorld;

    public HashMap<BlazePlayer, PlayerArena> ArenasByPlayer = new HashMap<BlazePlayer, PlayerArena>();
    public HashMap<EntityPlayer, BlazePlayer> BlazePlayersByMinecraftPlayers = new HashMap<EntityPlayer, BlazePlayer>();
    public HashMap<String, BlazePlayer> BlazePlayersByUsername = new HashMap<String, BlazePlayer>();

    public BlazeWorld (World world) {
        MinecraftWorld = world;

        for (String playerName : RedisPlayer.getAllUserNames()) {
            BlazePlayer player = new BlazePlayer(playerName);
            PlayerArena arena = new PlayerArena(this, player);
            player.Arena = arena;
            BlazePlayersByUsername.put(playerName, player);
            ArenasByPlayer.put(player, arena);
        }
    }

    public void OnPlayerJoinWorld(EntityPlayer minecraftPlayer) {
        System.out.println("Player connected: " + minecraftPlayer.getGameProfile().getName());

        // Find the blaze player with this ID
        for (BlazePlayer player : ArenasByPlayer.keySet()) {
            String bUserName = player.PlayerData.getUserName();
            String mUserName = minecraftPlayer.getGameProfile().getName();
            if (bUserName.equals(mUserName)) {
                player.onMinecraftPlayerLogin(minecraftPlayer);
                BlazePlayersByMinecraftPlayers.put(minecraftPlayer, player);
                return;
            }
        }
    }

    public BlazePlayer getPlayerByAuthToken(String token) {
        String username = RedisPlayer.getPlayerByAuthToken(token);
        if (username == null || username.isEmpty()) {
            return null;
        }
        return BlazePlayersByUsername.get(username);
    }

}
