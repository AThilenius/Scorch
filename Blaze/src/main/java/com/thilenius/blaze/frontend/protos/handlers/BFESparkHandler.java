package com.thilenius.blaze.frontend.protos.handlers;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.data.InfoQuery;
import com.thilenius.blaze.frontend.IBFERequest;
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
        synchronized (m_differedCommands) {

            // Using a iterator because I need to conditionally clear the list.
            for (Iterator<DifferedSparkRequest> iterator = m_differedCommands.iterator(); iterator.hasNext(); ) {
                DifferedSparkRequest request = iterator.next();

                if (!request.Spark.TileEntity.isAnimating()) {
                    // We can animate the Spark now
                    BFEProtos.BFESparkResponse response = request.Spark.handle(request.Command.getCommand());

                    // sendResponse back to Client
                    BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                            .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt, response)
                            .build();
                    request.Request.sendResponse(message);

                    // Give the loaded level a chance to grade based on this move
                    request.Level.grade(request.Command);
                    iterator.remove();
                }

                // Else, don't remove it from the list
            }
        }
    }

    @Override
    public boolean Handle(IBFERequest request) {
        if (!request.getRequest().hasExtension(BFEProtos.BFESparkCommand.bFESparkCommandExt)) {
            return false;
        }

        class SparkTask implements Runnable {
            private IBFERequest m_request;
            public SparkTask(IBFERequest taskRequest) { m_request = taskRequest; }

            @Override
            public void run() {
                BFEProtos.BFESparkCommand sparkRequest
                        = m_request.getRequest().getExtension(BFEProtos.BFESparkCommand.bFESparkCommandExt);
                InfoQuery infoQuery = new InfoQuery(sparkRequest.getAuthToken());
                if (!Blaze.RemoteDataConnection.query(infoQuery)) {

                    // Failure, respond
                    BFEProtos.BFESparkResponse response = BFEProtos.BFESparkResponse.newBuilder()
                            .setFailureReason("Invalid AuthToken.")
                            .build();
                    BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                            .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt, response)
                            .build();
                    m_request.sendResponse(message);

                } else {

                    // The rest must be marshaled to the main thread (until I make all this thread safe)
                    // God I fucking hate this language... this is so gross!
                    class SparkTaskMain implements Runnable {
                        private BFEProtos.BFESparkCommand m_sparkRequest;
                        private InfoQuery m_infoQuery;
                        public SparkTaskMain(BFEProtos.BFESparkCommand taskSparkRequest, InfoQuery taskInfoQuery) {
                            m_sparkRequest = taskSparkRequest;
                            m_infoQuery = taskInfoQuery;
                        }
                        @Override
                        public void run() {
                            // Get Active Level
                            BlazeLevel level = m_assignmentServer.getActiveLevelForPlayer(m_infoQuery.Username);
                            if (level == null) {
                                BFEProtos.BFESparkResponse response = BFEProtos.BFESparkResponse.newBuilder()
                                        .setFailureReason("A level is not loaded. Load a level first.")
                                        .build();
                                BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                                        .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt, response)
                                        .build();
                                m_request.sendResponse(message);
                            } else {
                                // Get Spark
                                if (m_sparkRequest.getSparkId() < 0 ||
                                        level.getSparks().length <= m_sparkRequest.getSparkId()) {
                                    BFEProtos.BFESparkResponse response = BFEProtos.BFESparkResponse.newBuilder()
                                            .setFailureReason("Invalid Spark ID.")
                                            .build();
                                    BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                                            .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt, response)
                                            .build();
                                    m_request.sendResponse(message);
                                } else {
                                    BlazeSpark spark = level.getSparks()[m_sparkRequest.getSparkId()];

                                    // Check if the spark is currently animating
                                    if (spark.TileEntity.isAnimating()) {
                                        // Deffer the command
                                        synchronized (m_differedCommands) {
                                            m_differedCommands.add(new DifferedSparkRequest(spark, level,
                                                    m_request, m_sparkRequest));
                                        }
                                    } else {
                                        // We can animate the Spark now
                                        BFEProtos.BFESparkResponse response = spark.handle(m_sparkRequest.getCommand());

                                        // sendResponse back to Client
                                        BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                                                .setExtension(BFEProtos.BFESparkResponse.bFESparkResponseExt,
                                                        response)
                                                .build();
                                        m_request.sendResponse(message);

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

    public class DifferedSparkRequest {

        public BlazeSpark Spark;
        public BlazeLevel Level;
        public IBFERequest Request;
        public BFEProtos.BFESparkCommand Command;

        public DifferedSparkRequest(BlazeSpark spark, BlazeLevel level, IBFERequest request,
                                    BFEProtos.BFESparkCommand command) {
            Spark = spark;
            Level = level;
            Request = request;
            Command = command;
        }

    }
}
