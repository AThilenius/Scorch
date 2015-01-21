package com.thilenius.blaze.frontend.protos.handlers;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.assignment.AssignmentLoader;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.assignment.LoadState;
import com.thilenius.blaze.data.AssignmentQuery;
import com.thilenius.blaze.data.PointsQuery;
import com.thilenius.blaze.data.UserQuery;
import com.thilenius.blaze.frontend.IBFERequest;
import com.thilenius.blaze.frontend.protos.BFEProtos;

import java.util.*;

/**
 * Created by Alec on 11/15/14.
 */
public class BFEAssignmentHandler extends BFEProtoHandler {

    private Random m_random = new Random();
    private AssignmentLoader m_assignmentLoader = new AssignmentLoader();
    private Map<String, LoadState> m_loadedStatesByUsername = new HashMap<String, LoadState>();

    public BlazeLevel getActiveLevelForPlayer(String username) {
        return m_loadedStatesByUsername.get(username).Level;
    }

    public void setDefault(UserQuery userQuery) {

        // Only set defaults if nothing is loaded yet.
        LoadState loadState = m_loadedStatesByUsername.get(userQuery.Username);
        if (loadState == null) {
            loadState = new LoadState();
            m_loadedStatesByUsername.put(userQuery.Username, loadState);
            loadState.setDefault(userQuery);
        }

    }

    @Override
    public boolean Handle(IBFERequest request) {
        if (!request.getRequest().hasExtension(BFEProtos.BFELoadLevelRequest.bFELoadLevelRequestExt)) {
            return false;
        }

        class AssignmentTask implements Runnable {
            private IBFERequest m_request;
            public AssignmentTask(IBFERequest taskRequest) { m_request = taskRequest; }

            @Override
            public void run() {
                BFEProtos.BFELoadLevelRequest loadRequest
                        = m_request.getRequest().getExtension(BFEProtos.BFELoadLevelRequest.bFELoadLevelRequestExt);
                BFEProtos.BFELoadLevelResponse response;
                AssignmentQuery assignmentQuery = new AssignmentQuery(loadRequest.getAuthToken(),
                        loadRequest.getLevelNumber());

                if (!Blaze.RemoteDataConnection.query(assignmentQuery)) {
                    response = BFEProtos.BFELoadLevelResponse.newBuilder()
                            .setFailureReason("Invalid AuthToken")
                            .build();
                } else {
                    if (assignmentQuery.OpenDate.compareTo(new java.util.Date()) > -1) {
                        // Not yet open
                        response = BFEProtos.BFELoadLevelResponse.newBuilder()
                                .setFailureReason(
                                        "Assignment has not yet opened. Please wait for the assignment to open.")
                                .build();
                    } else if (assignmentQuery.DueDate.compareTo(new java.util.Date()) < 1) {
                        // Past due
                        response = BFEProtos.BFELoadLevelResponse.newBuilder()
                                .setFailureReason(
                                        "The assignment is past due, you cannot work on this assignment any longer.")
                                .build();
                    } else {
                        // All is happy and wonderful, life is filled with rainbows'n shit. Now query points.
                        // This is done separate because it is never cached
                        PointsQuery pointsQuery = new PointsQuery(assignmentQuery.UserLevelId);
                        if (!Blaze.RemoteDataConnection.query(pointsQuery)) {
                            // I guess all is not rainbows :,( Now I haz a sadness.
                            response = BFEProtos.BFELoadLevelResponse.newBuilder()
                                    .setFailureReason( "Failed to load User Level Points.")
                                    .build();
                        } else {
                            // All really is rainbows! FINALLY, marshal back to the main thread and handle this shit
                            class AssignmentTaskMain implements Runnable {
                                private AssignmentLoader m_assignmentLoader;
                                private AssignmentQuery m_assignmentQuery;
                                private PointsQuery m_pointsQuery;
                                public AssignmentTaskMain(AssignmentLoader assignmentLoader,
                                                          AssignmentQuery assignmentQuery1,
                                                          PointsQuery pointsQuery1) {
                                    m_assignmentLoader = assignmentLoader;
                                    m_assignmentQuery = assignmentQuery1;
                                    m_pointsQuery = pointsQuery1;
                                }
                                @Override
                                public void run() {
                                    LoadState loadState = m_loadedStatesByUsername.get(m_assignmentQuery.Username);
                                    if (loadState == null) {
                                        loadState = new LoadState();
                                        m_loadedStatesByUsername.put(m_assignmentQuery.Username, loadState);
                                    }

                                    BFEProtos.BFEMessage message = loadState.transitionState(m_assignmentLoader,
                                            m_assignmentQuery, m_pointsQuery.Points);
                                    m_request.sendResponse(message);
                                }
                            }

                            // Blahh. What a disgusting language Java is...
                            Blaze.marshalToGameLoop(new AssignmentTaskMain(m_assignmentLoader, assignmentQuery,
                                    pointsQuery));
                            return;
                        }
                    }
                }

                // Send out the error message
                m_request.sendResponse(
                        BFEProtos.BFEMessage.newBuilder()
                        .setExtension(BFEProtos.BFELoadLevelResponse.bFELoadLevelResponseExt, response)
                        .build());
            }
        }

        Blaze.ThreadPool.execute(new AssignmentTask(request));
        return true;
    }
}
