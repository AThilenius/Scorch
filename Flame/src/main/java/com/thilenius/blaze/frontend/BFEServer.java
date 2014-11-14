package com.thilenius.blaze.frontend;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alec on 11/4/14.
 * The Blaze Front End Server
 */
public class BFEServer {

    private BFESocketServer m_socketServer;
    private RequestDispatcher m_reqDispatch;

    public void startServer() {
        m_socketServer = new BFESocketServer(5529);
        m_socketServer.startServer();

        m_reqDispatch = new RequestDispatcher(m_socketServer);
        m_reqDispatch.startServer();
    }
}
