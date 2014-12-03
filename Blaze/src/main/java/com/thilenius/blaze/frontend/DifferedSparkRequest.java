package com.thilenius.blaze.frontend;

import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.spark.BlazeSpark;

import java.nio.channels.SocketChannel;

/**
 * Created by Alec on 12/2/14.
 */
public class DifferedSparkRequest {

    public BlazeSpark Spark;
    public BlazeLevel Level;
    public SocketChannel Socket;
    public BFEProtos.BFESparkCommand Request;

    public DifferedSparkRequest(BlazeSpark spark, BlazeLevel level, SocketChannel socketChannel, BFEProtos.BFESparkCommand request) {
        Spark = spark;
        Level = level;
        Socket = socketChannel;
        Request = request;
    }

}