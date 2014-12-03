package com.thilenius.blaze.frontend;

import com.thilenius.blaze.frontend.protos.BFEProtos;

import java.nio.channels.SocketChannel;

/**
 * Created by Alec on 12/2/14.
 */
public class SparkRequest {

    public SocketChannel Socket;
    public BFEProtos.BFESparkCommand Request;

    public SparkRequest(SocketChannel socketChannel, BFEProtos.BFESparkCommand request) {
        Socket = socketChannel;
        Request = request;
    }

}