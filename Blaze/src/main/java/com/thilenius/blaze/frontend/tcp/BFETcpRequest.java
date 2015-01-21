package com.thilenius.blaze.frontend.tcp;

import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.googlecode.protobuf.format.JsonFormat;
import com.thilenius.blaze.frontend.IBFERequest;
import com.thilenius.blaze.frontend.protos.BFEProtos;

import java.nio.channels.SocketChannel;

// Implementation of the BFERequest interface for a TCP/IP request
public class BFETcpRequest implements IBFERequest {

    private BFESocketServer m_socketServer;
    private SocketChannel m_channel;
    private BFEProtos.BFEMessage m_message;

    public BFETcpRequest(BFESocketServer socketServer, SocketChannel channel, BFEProtos.BFEMessage message) {
        m_socketServer = socketServer;
        m_channel = channel;
        m_message = message;
    }

    @Override
    public BFEProtos.BFEMessage getRequest() {
        return m_message;
    }

    @Override
    public void sendResponse(BFEProtos.BFEMessage response) {
        m_socketServer.send(m_channel, response.toByteArray());
    }

}
