package com.thilenius.blaze.frontend;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;
import com.thilenius.blaze.frontend.tcp.SocketRequest;

import java.util.List;

/**
 * Created by Alec on 11/13/14.
 */
public class BFERequestDispatcher {

    public BFEAssignmentServer LevelServer;
    public BFESparkServer SparkServer;

    private BFESocketServer m_server;
    private ExtensionRegistry m_extensionRegistry;

    public BFERequestDispatcher(BFESocketServer socketServer) {
        m_server = socketServer;
        m_extensionRegistry = ExtensionRegistry.newInstance();
        BFEProtos.registerAllExtensions(m_extensionRegistry);

        LevelServer = new BFEAssignmentServer(socketServer);
        SparkServer = new BFESparkServer(socketServer, LevelServer);
    }

    public void onTick() {
        // Allow any blocked Sparks to free
        SparkServer.onTick();

        List<SocketRequest> pendingRequests = m_server.getAllWaiting();

        while (!pendingRequests.isEmpty()) {
            try {
                SocketRequest request = pendingRequests.remove(0);
                BFEProtos.BFEMessage message = BFEProtos.BFEMessage.parseFrom(request.Payload, m_extensionRegistry);

                System.out.println("Processing Proto: " + message.toString());
                if (message.hasExtension(BFEProtos.BFELoadLevelRequest.bFELoadLevelRequestExt)) {
                    BFEProtos.BFELoadLevelRequest loadRequest
                            = message.getExtension(BFEProtos.BFELoadLevelRequest.bFELoadLevelRequestExt);
                    LevelServer.Handle(request.Channel, loadRequest);
                } else if (message.hasExtension(BFEProtos.BFESparkCommand.bFESparkCommandExt)) {
                    BFEProtos.BFESparkCommand sparkRequest
                            = message.getExtension(BFEProtos.BFESparkCommand.bFESparkCommandExt);
                    SparkServer.Handle(request.Channel, sparkRequest);
                } else {
                    System.out.println("Invalid packet type. Can not dispatch!");
                }
            } catch (InvalidProtocolBufferException e) {
                System.err.println("Failed to parse Protocol Buffer:");
                e.printStackTrace();
            }
        }
    }
}
