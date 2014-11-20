package com.thilenius.blaze.frontend.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Alec on 11/19/14.
 */
public class HttpRequest {
    public byte[] Payload;
    private HttpExchange m_exchange;

    public HttpRequest(byte[] payload, HttpExchange exchange) {
        Payload = payload;
        m_exchange = exchange;
    }

    public boolean Respond(byte[] data) {
        try {
            m_exchange.sendResponseHeaders(200, data.length);
            OutputStream os = m_exchange.getResponseBody();
            os.write(data);
            os.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
