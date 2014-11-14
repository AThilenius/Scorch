package com.thilenius.blaze.frontend;

import com.google.protobuf.InvalidProtocolBufferException;
import com.thilenius.blaze.frontend.protos.BFEProtos;

/**
 * Created by Alec on 11/13/14.
 */
public class RequestDispatcher implements Runnable{

    public AuthenticationServer AuthServer;

    private BFESocketServer m_server;

    public RequestDispatcher(BFESocketServer socketServer) {
        m_server = socketServer;

        AuthServer = new AuthenticationServer(socketServer);
    }

    public void startServer() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {

            // Poll the next buffer off the Queue
            byte[] buffer = m_server.readNext();

            try {
                BFEProtos.BFEMessage message = BFEProtos.BFEMessage.parseFrom(buffer);

                if (message.hasExtension(BFEProtos.BFEAuthRequest.bFEAuthRequestExt)) {
                    BFEProtos.BFEAuthRequest authRequest = message.getExtension(BFEProtos.BFEAuthRequest.bFEAuthRequestExt);
                    AuthServer.Handle(authRequest);
                }

            } catch (InvalidProtocolBufferException e) {
                System.err.println("Failed to parse Protocol Buffer:");
                e.printStackTrace();
            }

        }
    }

}
