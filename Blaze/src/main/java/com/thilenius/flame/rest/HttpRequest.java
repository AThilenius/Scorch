package com.thilenius.flame.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;
import com.thilenius.flame.Flame;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Alec on 3/30/15.
 */
public class HttpRequest {
    private Statement m_requestStatement;
    private HttpExchange m_httpExchange;

    public HttpRequest(Statement statement, HttpExchange httpExchange) {
        m_requestStatement = statement;
        m_httpExchange = httpExchange;
    }

    public Statement getStatement() {
        return m_requestStatement;
    }

    public void respond(StatementResponse response) {
        try {
            byte[] data = Flame.Globals.JsonObjectMapper.writeValueAsBytes(response);

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

        } catch (JsonProcessingException e) {
            System.out.println("Failed to send JSON response to client:");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed to send JSON response to client:");
            e.printStackTrace();
        }
    }
}
