package com.thilenius.flame.rest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.thilenius.flame.Flame;

public class RestHttpServer implements Runnable {

    private HttpServer m_server = null;
    private List<HttpRequest> m_receiveQueue = new LinkedList<HttpRequest>();

    public RestHttpServer() {
    }

    @Override
    public void run() {
        try {
            m_server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(80), 0);
            m_server.createContext("/statement", new HttpJsonHandler());
            m_server.setExecutor(Executors.newFixedThreadPool(128));
            m_server.start();
            System.out.println("Blaze HTTP Proto/Json server listening on Port 80.");
        } catch (IOException e) {
            System.out.println("Failed to start server on port 80. Trying port 9886...");

            try {
                m_server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(9886), 0);
                m_server.createContext("/statement", new HttpJsonHandler());
                m_server.setExecutor(Executors.newFixedThreadPool(128));
                m_server.start();
                System.out.println("Blaze HTTP Proto/Json server listening on Port 9886.");
            } catch (IOException e1) {
                System.out.println("Failed to start server on port 80 and 9886: ");
                e1.printStackTrace();
            }
        }
    }

    public List<HttpRequest> getAllWaiting(boolean blocking) {
        List<HttpRequest> allWaiting = new LinkedList<HttpRequest>();
        synchronized(m_receiveQueue) {
            try {

                if (blocking) {
                    // Wait for at least 1 packet
                    while (m_receiveQueue.isEmpty()) {
                        m_receiveQueue.wait();
                    }
                }

                // Pull all packets out of the queue
                while(!m_receiveQueue.isEmpty()) {
                    allWaiting.add(m_receiveQueue.remove(0));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return allWaiting;
    }

    class HttpJsonHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String json = org.apache.commons.io.IOUtils.toString(t.getRequestBody());
            Statement statement = Flame.Globals.JsonObjectMapper.readValue(json, Statement.class);
            HttpRequest request = new HttpRequest(statement, t);

            synchronized (m_receiveQueue) {
                m_receiveQueue.add(request);
                m_receiveQueue.notify();
            }
        }
    }

}
