package com.thilenius.blaze.frontend.http;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import com.sun.net.httpserver.HttpExchange;
import com.thilenius.blaze.frontend.IBFERequest;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Alec on 1/20/15.
 */
public class BFEHttpJsonRequest  implements IBFERequest {

    private HttpExchange m_httpExchange;
    private BFEProtos.BFEMessage m_message;

    public BFEHttpJsonRequest(HttpExchange httpExchange, BFEProtos.BFEMessage message) {
        m_httpExchange = httpExchange;
        m_message = message;
    }

    @Override
    public BFEProtos.BFEMessage getRequest() {
        return m_message;
    }

    @Override
    public void sendResponse(BFEProtos.BFEMessage response) {
        // Encode to Base64
        byte[] data = JsonFormat.printToString(response).getBytes();

        try {
            m_httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            if (m_httpExchange.getLocalAddress().getPort() == 80) {
                // Production
                m_httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "https://scorchforge.com");
            } else {
                // Development
                m_httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            }

            m_httpExchange.sendResponseHeaders(200, data.length);
            OutputStream os = m_httpExchange.getResponseBody();
            os.write(data);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}