package com.thilenius.blaze.frontend.http;

import com.sun.net.httpserver.HttpExchange;
import com.thilenius.blaze.frontend.IBFERequest;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Alec on 1/18/15.
 */
public class BFEHttpProtoRequest implements IBFERequest {

    private HttpExchange m_httpExchange;
    private BFEProtos.BFEMessage m_message;

    public BFEHttpProtoRequest(HttpExchange httpExchange, BFEProtos.BFEMessage message) {
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
        byte[] data = Base64.encodeBase64(response.toByteArray());

        try {
            m_httpExchange.sendResponseHeaders(200, data.length);
            OutputStream os = m_httpExchange.getResponseBody();
            os.write(data);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
