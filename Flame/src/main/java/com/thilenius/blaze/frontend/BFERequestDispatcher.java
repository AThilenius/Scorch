package com.thilenius.blaze.frontend;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;
import com.thilenius.blaze.frontend.tcp.SocketRequest;

/**
 * Created by Alec on 11/13/14.
 */
public class BFERequestDispatcher implements Runnable{

    public BFEAuthenticationServer AuthServer;
    public BFEAssignmentServer LevelServer;
    public BFESparkServer SparkServer;

    private BFESocketServer m_server;


    public BFERequestDispatcher(BFESocketServer socketServer) {
        m_server = socketServer;

        AuthServer = new BFEAuthenticationServer(socketServer);
        LevelServer = new BFEAssignmentServer(socketServer);
        SparkServer = new BFESparkServer(socketServer, LevelServer);
    }

    public void startServer() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("Request Dispatch starting.");

        // Build out the extension registry
        ExtensionRegistry extensionRegistry = ExtensionRegistry.newInstance();
        BFEProtos.registerAllExtensions(extensionRegistry);

        while (true) {
            // Poll the next buffer off the Queue
            SocketRequest request = m_server.readNext();

            // Thread shutdown
            if (request == null) {
                return;
            }

            try {
                BFEProtos.BFEMessage message = BFEProtos.BFEMessage.parseFrom(request.Payload, extensionRegistry);

                System.out.println("Got message: " + message.toString());

                System.out.println("Dispatching message... ");
                if (message.hasExtension(BFEProtos.BFEAuthRequest.bFEAuthRequestExt)) {
                    BFEProtos.BFEAuthRequest authRequest
                            = message.getExtension(BFEProtos.BFEAuthRequest.bFEAuthRequestExt);
                    System.out.println("Message dispatched to Authentication server.");
                    AuthServer.Handle(request.Channel, authRequest);
                } else if (message.hasExtension(BFEProtos.BFELoadLevelRequest.bFELoadLevelRequestExt)) {
                    BFEProtos.BFELoadLevelRequest loadRequest
                            = message.getExtension(BFEProtos.BFELoadLevelRequest.bFELoadLevelRequestExt);
                    System.out.println("Message dispatched to Assignment server.");
                    LevelServer.Handle(request.Channel, loadRequest);
                } else if (message.hasExtension(BFEProtos.BFESparkCommand.bFESparkCommandExt)) {
                    BFEProtos.BFESparkCommand sparkRequest
                            = message.getExtension(BFEProtos.BFESparkCommand.bFESparkCommandExt);
                    System.out.println("Message dispatched to Spark server.");
                    SparkServer.Handle(request.Channel, sparkRequest);
                } else {
                    System.out.println("Invalid packet type. Can not dispatch.");
                }
            } catch (InvalidProtocolBufferException e) {
                System.err.println("Failed to parse Protocol Buffer:");
                e.printStackTrace();
            }

        }
    }

}
