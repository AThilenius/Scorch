package com.thilenius.blaze.frontend;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;
import com.thilenius.blaze.spark.BlazeSpark;

import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alec on 11/16/14.
 */
public class BFESparkServer {

    private BFESocketServer m_socketServer;
    private BFEAssignmentServer m_assignmentServer;
    private List<DifferedSparkRequest> m_defferedCommands = new LinkedList<DifferedSparkRequest>();

    public BFESparkServer(BFESocketServer socketServer, BFEAssignmentServer assignmentServer) {
        m_socketServer = socketServer;
        m_assignmentServer = assignmentServer;
    }

    public void Handle(SocketChannel socketChannel, BFEProtos.BFESparkCommand request) {
        // QUERY the DB for needed information
        try {
            BFEProtos.BFESparkResponse response;
            Statement statement = Blaze.SqlInstance.createStatement();

            // Fuck me that is an ugly QUERY... This could turn out to be a huge performance sink, may need
            // to pre-process this in Forge.
            ResultSet rs = statement.executeQuery(
                    "SELECT users.username\n" +
                    "FROM user_assignments\n" +
                    "  JOIN users\n" +
                    "    ON users.id = user_assignments.user_id\n" +
                    "WHERE user_assignments.authToken = \"" + request.getAuthToken() + "\"");
            rs.next();
            String username = rs.getString("username");
            rs.next();

            if (username == null || username.isEmpty()) {
                response = BFEProtos.BFESparkResponse.newBuilder()
                        .setFailureReason("Invalid AuthToken.")
                        .build();
            } else {
                // Get Active Level
                BlazeLevel level = m_assignmentServer.getActiveLevelForPlayer(username);
                if (level == null) {
                    response = BFEProtos.BFESparkResponse.newBuilder()
                            .setFailureReason("A level is not loaded. Load a level first.")
                            .build();
                } else {
                    // Get Spark
                    if (request.getSparkId() < 0 || level.getSparks().length <= request.getSparkId()) {
                        response = BFEProtos.BFESparkResponse.newBuilder()
                                .setFailureReason("Invalid Spark ID.")
                                .build();
                    } else {
                        BlazeSpark spark = level.getSparks()[request.getSparkId()];

                        // Check if the spark is currently animating
                        if (spark.TileEntity.isAnimating()) {
                            // Deffer the command
                            m_defferedCommands.add(new DifferedSparkRequest(spark, level, socketChannel, request));
                        } else {
                            // We can animate the Spark now
                            response = spark.handle(request.getCommand());

                            // Respond back to Client
                            BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                                    .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt, response)
                                    .build();
                            m_socketServer.send(socketChannel, message.toByteArray());

                            // Give the loaded level a chance to grade based on this move
                            level.grade(request);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            BFEProtos.BFESparkResponse response = BFEProtos.BFESparkResponse.newBuilder()
                    .setFailureReason("Blaze SQL error: " + e.getStackTrace().toString())
                    .build();
            m_socketServer.send(socketChannel,
                    BFEProtos.BFEMessage.newBuilder()
                            .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt, response)
                            .build()
                            .toByteArray());
        }
    }

    public void onTick() {
        for (Iterator<DifferedSparkRequest> iterator = m_defferedCommands.iterator(); iterator.hasNext();) {
            DifferedSparkRequest request = iterator.next();

            if (!request.Spark.TileEntity.isAnimating()) {
                // We can animate the Spark now
                BFEProtos.BFESparkResponse response = request.Spark.handle(request.Request.getCommand());

                // Respond back to Client
                BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                        .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt, response)
                        .build();
                m_socketServer.send(request.Socket, message.toByteArray());

                // Give the loaded level a chance to grade based on this move
                request.Level.grade(request.Request);
                iterator.remove();
            }
        }
    }
}
