package com.thilenius.blaze.frontend;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.data.UserQuery;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.protos.handlers.BFEAssignmentHandler;
import com.thilenius.blaze.frontend.protos.handlers.BFEInfoQueryHandler;
import com.thilenius.blaze.frontend.protos.handlers.BFEProtoHandler;
import com.thilenius.blaze.frontend.protos.handlers.BFESparkHandler;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;
import com.thilenius.blaze.frontend.tcp.SocketRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alec on 11/4/14.
 * The Blaze Front End Server
 */
public class BlazeFrontEnd implements Runnable {

    // HACK: These should be independent.
    public BFEAssignmentHandler AssignmentHandler = new BFEAssignmentHandler();
    public BFESparkHandler SparkHandler = new BFESparkHandler(AssignmentHandler);
    public BFEInfoQueryHandler InfoQueryHandler = new BFEInfoQueryHandler();

    private ExtensionRegistry m_extensionRegistry;
    private BFESocketServer m_socketServer;
    private List<BFEProtoHandler> m_handlers = new ArrayList<BFEProtoHandler>();

    public void startServer() {
        m_handlers.add(AssignmentHandler);
        m_handlers.add(SparkHandler);
        m_handlers.add(InfoQueryHandler);

        m_extensionRegistry = ExtensionRegistry.newInstance();
        BFEProtos.registerAllExtensions(m_extensionRegistry);

        m_socketServer = new BFESocketServer(5529);
        m_socketServer.startServer();

        // Start up TCP request pump
        new Thread(this).start();
    }

    public void onTick() {
        // Nothing for now
    }

    public void setDefaults(UserQuery userQuery) {
        AssignmentHandler.setDefault(userQuery);
    }


    @Override
    public void run() {
        while(true) {

            // Get all pending (binary) requests BLOCKING
            List<SocketRequest> allWaiting = m_socketServer.getAllWaiting(true);

            // De-Serialize them, wrap in a BFERequest and handle them
            for (SocketRequest binaryReq : allWaiting) {
                try {
                    BFEProtos.BFEMessage message = BFEProtos.BFEMessage.parseFrom(binaryReq.Payload, m_extensionRegistry);
                    BFERequest request = new BFERequest(m_socketServer, binaryReq.Channel, message);

                    // Handle the request
                    for (BFEProtoHandler handler : m_handlers) {
                        if (handler.Handle(request)) {
                            // Done, continue handling others
                            break;
                        }
                    }

                } catch (InvalidProtocolBufferException e) {
                    System.err.println("Failed to parse Protocol Buffer:");
                    e.printStackTrace();
                }
            }

        }
    }

    public void registerHandler(BFEProtoHandler handler) {
        if (m_handlers.contains(handler)) {
            System.out.println("Cannot add duplicate handler!");
        } else {
            m_handlers.add(handler);
        }
    }

}
