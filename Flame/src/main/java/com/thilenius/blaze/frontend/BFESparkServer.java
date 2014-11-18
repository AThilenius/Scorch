package com.thilenius.blaze.frontend;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.blaze.spark.BlazeSpark;
import com.thilenius.flame.SparkTileEntity;

import java.nio.channels.SocketChannel;
import java.util.HashSet;

/**
 * Created by Alec on 11/16/14.
 */
public class BFESparkServer {

    public static BFESparkServer DebugInstance;
    public HashSet<SparkTileEntity> KnownTileEntities = new HashSet<SparkTileEntity>();

    private BFESocketServer m_socketServer;
    private BFEAssignmentServer m_assignmentServer;

    public BFESparkServer(BFESocketServer socketServer, BFEAssignmentServer assignmentServer) {
        m_socketServer = socketServer;
        m_assignmentServer = assignmentServer;
        DebugInstance = this;
    }

    public void Handle(SocketChannel socketChannel, BFEProtos.BFESparkCommand request) {
        BFEProtos.BFESparkResponse response;
        // Get player
        BlazePlayer player = Blaze.World.getPlayerByAuthToken(request.getAuthToken());
        if (player == null) {
            response = BFEProtos.BFESparkResponse.newBuilder()
                    .setFailureReason("Invalid Auth Toekn.")
                    .build();
        } else {
            // Get Active Level
            BlazeLevel level = m_assignmentServer.getActiveLevelForPlayer(player);
            if (level == null) {
                response = BFEProtos.BFESparkResponse.newBuilder()
                        .setFailureReason("A level is not loaded. Load a level first.")
                        .build();
            } else {
                // Get Spark
                if (level.getSparks().length <= request.getSparkId()) {
                    response = BFEProtos.BFESparkResponse.newBuilder()
                            .setFailureReason("Invalid Spark ID.")
                            .build();
                } else {
                    // All good! Finally...
                    BlazeSpark spark = level.getSparks()[request.getSparkId()];
                    response = spark.Handle(request.getCommand());
                }
            }
        }

        BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt, response)
                .build();

        m_socketServer.send(socketChannel, message.toByteArray());
    }

}
