package com.thilenius.blaze.redis;

import com.thilenius.blaze.Blaze;
import com.thilenius.utilities.types.Location2D;

/**
 * Created by Alec on 11/13/14.
 */
public class RedisArena {

    private String m_redisPrefix;

    public RedisArena(String username) {
        m_redisPrefix = "arena_[" + username + "]_";
    }

    public Location2D getLocation() {
        return new Location2D(Blaze.RedisInstance.get(m_redisPrefix + "location"));
    }

    public void setLocation(Location2D location) {
        Blaze.RedisInstance.set(m_redisPrefix + "location", location.toString());
    }
}
