package com.thilenius.blaze.frontend;

import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;

import java.nio.channels.SocketChannel;

/**
 * Created by Alec on 1/16/15.
 */
public interface IBFERequest {
    public BFEProtos.BFEMessage getRequest();
    public void sendResponse(BFEProtos.BFEMessage response);
}
