package com.thilenius.blaze.redis;

import com.thilenius.blaze.Blaze;
import com.thilenius.utilities.types.Location3D;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Alec on 11/13/14.
 * A Model class for redis data related to a player
 */
public class RedisPlayer {

    public EntityPlayer MinecraftPlayer;

    private static final int TIMEOUT_SECONDS = 10 * 60;

    private String m_playerUserName;
    private String m_playerPrefix;

    public RedisPlayer(String username) {
        setUserName(username);
    }

    public static java.util.Set<java.lang.String> getAllUserNames() {
        return Blaze.RedisInstance.smembers("player_meta_all_players");
    }

    public static String getPlayerByAuthToken(String token) {
        return Blaze.RedisInstance.get("authToken_[" + token + "]_");
    }

    public String getUserName() {
        return m_playerUserName;
    }

    public void setUserName(String username) {
        m_playerUserName = username;
        m_playerPrefix = "player_[" + username + "]_";
    }

    public String getDisplayName() {
        return Blaze.RedisInstance.get(m_playerPrefix + "displayName");
    }

    public void setDisplayName(String name) {
        Blaze.RedisInstance.set(m_playerPrefix + "displayName", name);
    }

    public String getPassword() {
        return Blaze.RedisInstance.get(m_playerPrefix + "password");
    }

    public void setPassword(String password) {
        Blaze.RedisInstance.set(m_playerPrefix + "password", password);
    }

    public Location3D getSpawnLocation() {
        return new Location3D(Blaze.RedisInstance.get(m_playerPrefix + "spawnLocation"));
    }

    public void setSpawnLocation(Location3D location) {
        Blaze.RedisInstance.set(m_playerPrefix + "spawnLocation", location.toString());
    }

    public String getAuthToken() {
        String token = Blaze.RedisInstance.get(m_playerPrefix + "authToken");

        if (token == null || token.isEmpty()) {
            token = java.util.UUID.randomUUID().toString();
            Blaze.RedisInstance.set(m_playerPrefix + "authToken", token);
            Blaze.RedisInstance.set("authToken_[" + token + "]_", m_playerUserName);
        }

        // Set or update expiration time.
        Blaze.RedisInstance.expire(m_playerPrefix + "authToken", TIMEOUT_SECONDS);
        Blaze.RedisInstance.expire("authToken_[" + token + "]_", TIMEOUT_SECONDS);
        return token;
    }

}
