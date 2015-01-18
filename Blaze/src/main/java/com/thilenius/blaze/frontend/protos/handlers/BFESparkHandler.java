package com.thilenius.blaze.frontend.protos.handlers;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.data.InfoQuery;
import com.thilenius.blaze.frontend.BFERequest;
import com.thilenius.blaze.frontend.DifferedSparkRequest;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.spark.BlazeSpark;

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

        class SparkTask implements Runnable {
            private BFERequest m_request;
            public SparkTask(BFERequest taskRequest) { m_request = taskRequest; }

            @Override
            public void run() {
                BFEProtos.BFESparkResponse response;
                BFEProtos.BFESparkCommand sparkRequest
                        = m_request.Message.getExtension(BFEProtos.BFESparkCommand.bFESparkCommandExt);
                InfoQuery infoQuery = new InfoQuery(sparkRequest.getAuthToken());
                if (!Blaze.RemoteDataConnection.query(infoQuery)) {
                    response = BFEProtos.BFESparkResponse.newBuilder()
                            .setFailureReason("Invalid AuthToken.")
                            .build();
                } else {

                    // The rest must be marshaled to the main thread (until I make all this thread safe)
                    class SparkTaskMain implements Runnable {
                        private BFEProtos.BFESparkCommand m_sparkRequest;
                        private InfoQuery m_infoQuery;
                        public SparkTaskMain(BFEProtos.BFESparkCommand taskSparkRequest, InfoQuery taskInfoQuery) {
                            m_sparkRequest = taskSparkRequest;
                            m_infoQuery = taskInfoQuery;
                        }
                        @Override
                        public void run() {
                            BFEProtos.BFESparkResponse response;

                            // Get Active Level
                            BlazeLevel level = m_assignmentServer.getActiveLevelForPlayer(m_infoQuery.Username);
                            if (level == null) {
                                response = BFEProtos.BFESparkResponse.newBuilder()
                                        .setFailureReason("A level is not loaded. Load a level first.")
                                        .build();
                            } else {
                                // Get Spark
                                if (m_sparkRequest.getSparkId() < 0 ||
                                        level.getSparks().length <= m_sparkRequest.getSparkId()) {
                                    response = BFEProtos.BFESparkResponse.newBuilder()
                                            .setFailureReason("Invalid Spark ID.")
                                            .build();
                                } else {
                                    BlazeSpark spark = level.getSparks()[m_sparkRequest.getSparkId()];

                                    // Check if the spark is currently animating
                                    if (spark.TileEntity.isAnimating()) {
                                        // Deffer the command
                                        m_differedCommands.add(new DifferedSparkRequest(spark, level,
                                                m_request.SocketServer, m_request.Channel, m_sparkRequest));
                                    } else {
                                        // We can animate the Spark now
                                        response = spark.handle(m_sparkRequest.getCommand());

                                        // Respond back to Client
                                        BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                                                .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt,
                                                        response)
                                                .build();
                                        m_request.SocketServer.send(m_request.Channel, message.toByteArray());

                                        // Give the loaded level a chance to grade based on this move
                                        level.grade(m_sparkRequest);
                                    }
                                }
                            }
                        }
                    }

                    Blaze.marshalToGameLoop(new SparkTaskMain(sparkRequest, infoQuery));
                }
            }
        }

        Blaze.ThreadPool.execute(new SparkTask(request));
        return true;
    }
}
