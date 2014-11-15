package com.thilenius.blaze.redis;

import com.thilenius.blaze.Blaze;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * Created by Alec on 11/13/14.
 * A Model class for redis data related to a player
 */
public class RedisPlayer {

    public EntityPlayer MinecraftPlayer;

    private static final int TIMEOUT_SECONDS = 10 * 60;

    private String m_playerUserName;
    private String m_redisPrefix;

    public RedisPlayer(String username) {
        setUserName(username);
    }

    public static java.util.Set<java.lang.String> getAllUserNames() {
        return Blaze.RedisInstance.smembers("player_meta_all_players");
    }

    public String getUserName() {
        return m_playerUserName;
    }

    public void setUserName(String username) {
        m_playerUserName = username;
        m_redisPrefix = "player_[" + username + "]_";
    }

    public String getDisplayName() {
        return Blaze.RedisInstance.get(m_redisPrefix + "displayName");
    }

    public void setDisplayName(String name) {
        Blaze.RedisInstance.set(m_redisPrefix + "displayName", name);
    }

    public String getPassword() {
        return Blaze.RedisInstance.get(m_redisPrefix + "password");
    }

    public void setPassword(String password) {
        Blaze.RedisInstance.set(m_redisPrefix + "password", password);
    }

    public Location3D getSpawnLocation() {
        return new Location3D(Blaze.RedisInstance.get(m_redisPrefix + "spawnLocation"));
    }

    public void setSpawnLocation(Location3D location) {
        Blaze.RedisInstance.set(m_redisPrefix + "spawnLocation", location.toString());
    }

    public String getAuthToken() {
        String token = Blaze.RedisInstance.get(m_redisPrefix + "authToken");

        if (token == null || token.isEmpty()) {
            token = java.util.UUID.randomUUID().toString();
            Blaze.RedisInstance.set(m_redisPrefix + "authToken", token);
        }

        // Set or update expiration time.
        Blaze.RedisInstance.expire(m_redisPrefix + "authToken", TIMEOUT_SECONDS);
        return token;
    }

}
