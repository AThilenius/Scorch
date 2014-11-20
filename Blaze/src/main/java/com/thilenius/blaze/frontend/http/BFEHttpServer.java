package com.thilenius.blaze.frontend.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;
import com.thilenius.blaze.frontend.tcp.SocketRequest;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Created by Alec on 11/19/14.
 */
public class BFEHttpServer implements Runnable {

    private HttpServer m_server = null;
    private int m_port;
    private Object m_exchangeLock = new Object();
    private HttpRequest m_request;
    private List<HttpRequest> m_recieveQueue = new LinkedList<HttpRequest>();

    public BFEHttpServer(int port) {
        m_port = port;
    }

    public void startServer() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            m_server = HttpServer.create(new InetSocketAddress(m_port), 0);
            m_server.createContext("/enqueue", new HttpEnqueueHandler());
            m_server.setExecutor(null);
            m_server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<HttpRequest> getAllPendingRequests() {
        List<HttpRequest> retList = new LinkedList<HttpRequest>();

        synchronized(m_recieveQueue) {
            while(!m_recieveQueue.isEmpty()) {
                retList.add(m_recieveQueue.remove(0));
            }
        }

        return retList;
    }

    class HttpEnqueueHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            synchronized (m_recieveQueue) {
                m_recieveQueue.add(new HttpRequest(IOUtils.toByteArray(t.getRequestBody()), t));
            }
        }
    }
}
