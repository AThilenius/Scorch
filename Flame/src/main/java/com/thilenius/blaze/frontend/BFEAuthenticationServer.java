package com.thilenius.blaze.frontend;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;
import com.thilenius.blaze.player.BlazePlayer;

import java.nio.channels.SocketChannel;

/**
 * Created by Alec on 11/13/14.
 * User Lookup: login_[ <playerName> ]
 */
public class BFEAuthenticationServer {

    private static final String REDIS_PREFIX = "auth_";

    private BFESocketServer m_socketServer;

    public BFEAuthenticationServer(BFESocketServer socketServer) {
        m_socketServer = socketServer;
    }

    public void Handle(SocketChannel socketChannel, BFEProtos.BFEAuthRequest authRequest) {

        // Look up login an password in REDIS
        BlazePlayer player = Blaze.World.BlazePlayersByUsername.get(authRequest.getUsername());
        BFEProtos.BFEAuthResponse response = null;

        if (player != null && authRequest.getPassword().equals(player.PlayerData.getPassword())) {
            // Return the players Auth token (will also generate a new one if needed)
            response = BFEProtos.BFEAuthResponse.newBuilder()
                    .setAuthToken(player.PlayerData.getAuthToken())
                    .build();

            System.out.println("User authenticated, returning token: " + player.PlayerData.getAuthToken());
        } else {
            response = BFEProtos.BFEAuthResponse.newBuilder()
                    .setAuthToken("INVALID USERNAME OR PASSWORD")
                    .build();

            System.out.println("User failed authentication");
        }

        BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                .setExtension(BFEProtos.BFEAuthResponse.bFEAuthResponseExt, response)
                .build();

        m_socketServer.send(socketChannel, message.toByteArray());
    }
}
