package com.thilenius.blaze.frontend;

import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;

import java.nio.channels.SocketChannel;

/**
 * Created by Alec on 1/16/15.
 */
public class BFERequest {
    public BFESocketServer SocketServer;
    public SocketChannel Channel;
    public BFEProtos.BFEMessage Message;

    public BFERequest(BFESocketServer socketServer, SocketChannel channel, BFEProtos.BFEMessage message) {
        SocketServer = socketServer;
        Channel = channel;
        Message = message;
    }
}
