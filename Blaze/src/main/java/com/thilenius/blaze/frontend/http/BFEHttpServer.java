package com.thilenius.blaze.frontend.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.frontend.IBFERequest;
import com.thilenius.blaze.frontend.IBFERequestHandler;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.CharSet;

/**
 * Created by Alec on 11/19/14.
 */
public class BFEHttpServer implements Runnable {

    private HttpServer m_server = null;
    private IBFERequestHandler m_handler;
    private List<IBFERequest> m_receiveQueue = new LinkedList<IBFERequest>();

    public BFEHttpServer(IBFERequestHandler handler) {
        m_handler = handler;
    }

    public void startServer() {
        // Start primary HTTP server
        new Thread(this).start();

        // Also start the HTTP Request pump
        class HttpServerPump implements Runnable {
            public IBFERequestHandler m_handler;
            public HttpServerPump(IBFERequestHandler handler) {
                m_handler = handler;
            }

            @Override
            public void run() {
                while (true) {

                    // BLOCKING until there is at least one request
                    List<IBFERequest> pendingTcpRequests = getAllWaiting(true);
                    for (IBFERequest request : pendingTcpRequests) {
                        m_handler.handle(request);
                    }
                }
            }

        }
        new Thread(new HttpServerPump(m_handler)).start();
    }

    @Override
    public void run() {
        try {
            m_server = HttpServer.create(new InetSocketAddress(80), 0);
            m_server.createContext("/proto", new HttpProtoHandler());
            m_server.createContext("/json", new HttpJsonHandler());
            m_server.setExecutor(null);
            m_server.start();
            System.out.println("Blaze HTTP Proto/Json server listening on Port 80.");
        } catch (IOException e) {
            System.out.println("Failed to start server on port 80. Trying port 9886");

            try {
                m_server = HttpServer.create(new InetSocketAddress(9886), 0);
                m_server.createContext("/proto", new HttpProtoHandler());
                m_server.createContext("/json", new HttpJsonHandler());
                m_server.setExecutor(null);
                m_server.start();
                System.out.println("Blaze HTTP Proto/Json server listening on Port 9886.");
            } catch (IOException e1) {
                System.out.println("Failed to start server on port 80 and 9886: ");
                e1.printStackTrace();
            }
        }
    }

    private List<IBFERequest> getAllWaiting(boolean blocking) {
        List<IBFERequest> allWaiting = new LinkedList<IBFERequest>();
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

    class HttpProtoHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            // Decode from Base64
            byte[] data = Base64.decodeBase64(IOUtils.toByteArray(t.getRequestBody()));

            try {
                BFEProtos.BFEMessage message = BFEProtos.BFEMessage.parseFrom(data, m_handler.getExtensionRegistry());
                synchronized (m_receiveQueue) {
                    m_receiveQueue.add(new BFEHttpProtoRequest(t, message));
                    m_receiveQueue.notify();
                }
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }
    }

    class HttpJsonHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String json = org.apache.commons.io.IOUtils.toString(t.getRequestBody());
            System.out.println("Processing JSON request: "+ json);
            Message.Builder builder = BFEProtos.BFEMessage.newBuilder();

            try {
                JsonFormat.merge(json, m_handler.getExtensionRegistry(), builder);
                synchronized (m_receiveQueue) {
                    m_receiveQueue.add(new BFEHttpJsonRequest(t, (BFEProtos.BFEMessage) builder.build()));
                    m_receiveQueue.notify();
                }
            } catch (JsonFormat.ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
