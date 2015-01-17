package com.thilenius.blaze.frontend.protos.handlers;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.frontend.BFERequest;
import com.thilenius.blaze.frontend.DifferedSparkRequest;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.spark.BlazeSpark;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alec on 11/16/14.
 */
public class BFESparkHandler extends BFEProtoHandler {

    private BFEAssignmentHandler m_assignmentServer;
    private List<DifferedSparkRequest> m_differedCommands = new LinkedList<DifferedSparkRequest>();

    public BFESparkHandler(BFEAssignmentHandler assignmentServer) {
        m_assignmentServer = assignmentServer;
    }

    public void onTick() {
        for (Iterator<DifferedSparkRequest> iterator = m_differedCommands.iterator(); iterator.hasNext();) {
            DifferedSparkRequest request = iterator.next();

            if (!request.Spark.TileEntity.isAnimating()) {
                // We can animate the Spark now
                BFEProtos.BFESparkResponse response = request.Spark.handle(request.Request.getCommand());

                // Respond back to Client
                BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                        .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt, response)
                        .build();
                request.Socket.send(request.Channel, message.toByteArray());

                // Give the loaded level a chance to grade based on this move
                request.Level.grade(request.Request);
                iterator.remove();
            }
        }
    }

    @Override
    public boolean Handle(BFERequest request) {
        if (!request.Message.hasExtension(BFEProtos.BFESparkCommand.bFESparkCommandExt)) {
            return false;
        }

        // HACK: For now, all on the game-loop thread
        class SparkTask implements Runnable {
            private BFERequest m_request;
            public SparkTask(BFERequest taskRequest) { m_request = taskRequest; }

            @Override
            public void run() {
                // QUERY the DB for needed information
                try {
                    BFEProtos.BFESparkResponse response;
                    BFEProtos.BFESparkCommand sparkRequest
                            = m_request.Message.getExtension(BFEProtos.BFESparkCommand.bFESparkCommandExt);
                    Statement statement = Blaze.RemoteDataConnection.m_sqlInstance.createStatement();

                    // Fuck me that is an ugly QUERY... This could turn out to be a huge performance sink, may need
                    // to pre-process this in Forge.
                    ResultSet rs = statement.executeQuery(
                            "SELECT users.username\n" +
                                    "FROM user_assignments\n" +
                                    "  JOIN users\n" +
                                    "    ON users.id = user_assignments.user_id\n" +
                                    "WHERE user_assignments.authToken = \"" + sparkRequest.getAuthToken() + "\"");
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
                            if (sparkRequest.getSparkId() < 0 ||
                                    level.getSparks().length <= sparkRequest.getSparkId()) {
                                response = BFEProtos.BFESparkResponse.newBuilder()
                                        .setFailureReason("Invalid Spark ID.")
                                        .build();
                            } else {
                                BlazeSpark spark = level.getSparks()[sparkRequest.getSparkId()];

                                // Check if the spark is currently animating
                                if (spark.TileEntity.isAnimating()) {
                                    // Deffer the command
                                    m_differedCommands.add(new DifferedSparkRequest(spark, level,
                                            m_request.SocketServer, m_request.Channel, sparkRequest));
                                } else {
                                    // We can animate the Spark now
                                    response = spark.handle(sparkRequest.getCommand());

                                    // Respond back to Client
                                    BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                                            .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt, response)
                                            .build();
                                    m_request.SocketServer.send(m_request.Channel, message.toByteArray());

                                    // Give the loaded level a chance to grade based on this move
                                    level.grade(sparkRequest);
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    BFEProtos.BFESparkResponse response = BFEProtos.BFESparkResponse.newBuilder()
                            .setFailureReason("Blaze SQL error: " + e.getStackTrace().toString())
                            .build();
                    m_request.SocketServer.send(m_request.Channel,
                            BFEProtos.BFEMessage.newBuilder()
                                    .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt, response)
                                    .build()
                                    .toByteArray());
                }
            }
        }

        Blaze.marshalToGameLoop(new SparkTask(request));
        return true;
    }
}
