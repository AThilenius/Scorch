package com.thilenius.blaze.frontend;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.assignment.AssignmentLoader;
import com.thilenius.blaze.assignment.BlazeAssignment;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;
import com.thilenius.blaze.player.BlazePlayer;

import java.nio.channels.SocketChannel;

/**
 * Created by Alec on 11/15/14.
 */
public class BFEAssignmentServer {

    private BFESocketServer m_socketServer;
    private AssignmentLoader m_assignmentLoader;

    public BFEAssignmentServer(BFESocketServer socketServer) {
        m_socketServer = socketServer;
        m_assignmentLoader = new AssignmentLoader();
    }

    public void Handle(SocketChannel socketChannel, BFEProtos.BFELoadLevelRequest request) {
        BFEProtos.BFELoadLevelResponse response = null;

        BlazePlayer player = Blaze.World.getPlayerByAuthToken(request.getAuthToken());

        if (player == null) {
            response = BFEProtos.BFELoadLevelResponse.newBuilder()
                    .setFailureReason("Invalid Authentication Token")
                    .build();
        } else {
            BlazeLevel level = m_assignmentLoader.LoadLevel(player, request.getAssignmentName(),
                    request.getLevelNumber());

            if (level == null) {
                response = BFEProtos.BFELoadLevelResponse.newBuilder()
                        .setFailureReason("Invalid Assignment Name or Level Number")
                        .build();
            } else {
                response = BFEProtos.BFELoadLevelResponse.newBuilder()
                        .setSparkCount(level.getSparks().length)
                        .build();
            }
        }

        BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                .setExtension(BFEProtos.BFELoadLevelResponse.bFELoadLevelResponseExt, response)
                .build();

        m_socketServer.send(socketChannel, message.toByteArray());
    }

    public BlazeLevel getActiveLevelForPlayer(BlazePlayer player) {
        return m_assignmentLoader.getActiveLevelForUsername(player.PlayerData.getUserName());
    }

}
