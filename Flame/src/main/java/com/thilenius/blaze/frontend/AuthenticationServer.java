package com.thilenius.blaze.frontend;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.frontend.protos.BFEProtos;

import java.util.UUID;

/**
 * Created by Alec on 11/13/14.
 * User Lookup: login_[ <playerName> ]
 */
public class AuthenticationServer {

    private static final String REDIS_PREFIX = "auth_";

    private BFESocketServer m_socketServer;

    public AuthenticationServer (BFESocketServer socketServer) {
        m_socketServer = socketServer;
    }

    public void Handle(BFEProtos.BFEAuthRequest authRequest) {
        // Look up login an password in REDIS

        return Blaze.RedisInstance.getSet(m_redisPrefix + "authToken", UUID.randomUUID().toString());
        Blaze.RedisInstance.get()
        Blaze.RedisInstance.expire()
        // Create an expiring auth-token
        // Return to user
        BFEProtos.BFEAuthResponse response = BFEProtos.BFEAuthResponse.newBuilder()
                .setAuthToken(UUID.randomUUID().toString())
                .build();

    }
}
