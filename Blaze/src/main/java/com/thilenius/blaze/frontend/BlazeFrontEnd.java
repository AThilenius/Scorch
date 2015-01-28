package com.thilenius.blaze.frontend;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.thilenius.blaze.data.UserQuery;
import com.thilenius.blaze.frontend.http.BFEHttpServer;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.protos.handlers.BFEAssignmentHandler;
import com.thilenius.blaze.frontend.protos.handlers.BFEInfoQueryHandler;
import com.thilenius.blaze.frontend.protos.handlers.BFEProtoHandler;
import com.thilenius.blaze.frontend.protos.handlers.BFESparkHandler;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;
import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alec on 11/4/14.
 * The Blaze Front End Server
 */
public class BlazeFrontEnd implements IBFERequestHandler {

    // Handlers
    public BFEAssignmentHandler AssignmentHandler = new BFEAssignmentHandler();
    public BFESparkHandler SparkHandler = new BFESparkHandler(AssignmentHandler);
    public BFEInfoQueryHandler InfoQueryHandler = new BFEInfoQueryHandler();
    private List<BFEProtoHandler> m_handlers = new ArrayList<BFEProtoHandler>();

    private ExtensionRegistry m_extensionRegistry;
    private BFESocketServer m_socketServer;
    private BFEHttpServer m_httpServer;

    public void startServer() {
        m_handlers.add(AssignmentHandler);
        m_handlers.add(SparkHandler);
        m_handlers.add(InfoQueryHandler);

        m_extensionRegistry = ExtensionRegistry.newInstance();
        BFEProtos.registerAllExtensions(m_extensionRegistry);

        m_socketServer = new BFESocketServer(5529, this);
        m_socketServer.startServer();

        m_httpServer = new BFEHttpServer(this);
        m_httpServer.startServer();
    }

    public void setDefaults(UserQuery userQuery) {
        AssignmentHandler.setDefault(userQuery);
    }

    // Needed by the spark server for now
    public void onTick() {
        SparkHandler.onTick();
    }

    // Asynchronous (That is, will be run in the thread pool)
    @Override
    public void handle(IBFERequest request) {

        // Send it to each handler and see if they can do something with it
        for (BFEProtoHandler handler : m_handlers) {
            if (handler.Handle(request)) {
                return;
            }
        }

    }

    @Override
    public ExtensionRegistry getExtensionRegistry() {
        return m_extensionRegistry;
    }
}
