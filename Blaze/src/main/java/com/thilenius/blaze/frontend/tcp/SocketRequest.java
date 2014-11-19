package com.thilenius.blaze.frontend.tcp;

import java.nio.channels.SocketChannel;

/**
 * Created by Alec on 11/14/14.
 */
public class SocketRequest {

    public byte[] Payload;
    public SocketChannel Channel;

    public SocketRequest(byte[] payload, SocketChannel socketChannel) {
        Payload = payload;
        Channel = socketChannel;
    }

}
