package com.thilenius.blaze.frontend;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;

import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Alec on 1/8/15.
 */
public class BFEInfoQueryServer {

    private BFESocketServer m_socketServer;

    public BFEInfoQueryServer(BFESocketServer socket) {
        m_socketServer = socket;
    }

    public void Handle(SocketChannel socketChannel, BFEProtos.BFEInfoQueryRequest request) {
        try {
            BFEProtos.BFEInfoQueryResponse response;
            Statement statement = Blaze.SqlInstance.createStatement();

            ResultSet rs = statement.executeQuery(
                    "SELECT users.firstName, users.lastName\n" +
                    "FROM user_assignments\n" +
                    "  JOIN users\n" +
                    "    ON users.id = user_assignments.user_id\n" +
                    "WHERE user_assignments.authToken = \"" + request.getAuthToken() + "\"");
            rs.next();
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            rs.next();

            if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
                response = BFEProtos.BFEInfoQueryResponse.newBuilder()
                        .setFailureReason("Invalid AuthToken.")
                        .build();
            } else {
                // Respond back to Client
                response = BFEProtos.BFEInfoQueryResponse.newBuilder()
                        .setBlazeResponse("Hello " + firstName + " " + lastName + "! Welcome to Blaze.")
                        .build();

                BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                        .setExtension(BFEProtos.BFEInfoQueryResponse.bFEInfoQueryResponseExt, response)
                        .build();
                m_socketServer.send(socketChannel, message.toByteArray());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            BFEProtos.BFEInfoQueryResponse response = BFEProtos.BFEInfoQueryResponse.newBuilder()
                    .setFailureReason("Blaze SQL error: " + e.getStackTrace().toString())
                    .build();
            m_socketServer.send(socketChannel,
                    BFEProtos.BFEMessage.newBuilder()
                            .setExtension(BFEProtos.BFEInfoQueryResponse.bFEInfoQueryResponseExt, response)
                            .build()
                            .toByteArray());
        }
    }
}
