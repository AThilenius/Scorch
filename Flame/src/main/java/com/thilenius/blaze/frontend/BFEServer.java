package com.thilenius.blaze.frontend;

import com.thilenius.blaze.frontend.tcp.BFESocketServer;

/**
 * Created by Alec on 11/4/14.
 * The Blaze Front End Server
 */
public class BFEServer {

    private BFESocketServer m_socketServer;
    private BFERequestDispatcher m_reqDispatch;

    public void startServer() {
        m_socketServer = new BFESocketServer(5529);
        m_socketServer.startServer();

        m_reqDispatch = new BFERequestDispatcher(m_socketServer);
        // Tick based
    }

    public void onTick() {
        m_reqDispatch.onTick();
    }
}
