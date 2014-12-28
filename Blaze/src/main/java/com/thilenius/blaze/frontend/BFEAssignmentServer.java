package com.thilenius.blaze.frontend;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.assignment.AssignmentLoader;
import com.thilenius.blaze.assignment.BlazeAssignment;
import com.thilenius.blaze.assignment.BlazeLevel;
import com.thilenius.blaze.assignment.LoadState;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;
import com.thilenius.blaze.player.BlazePlayer;
import com.thilenius.utilities.types.Location3D;

import java.nio.channels.SocketChannel;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Alec on 11/15/14.
 */
public class BFEAssignmentServer {

    private BFESocketServer m_socketServer;
    private AssignmentLoader m_assignmentLoader;
    private Random m_random = new Random();
    private Map<String, LoadState> m_loadedStatesByUsername = new HashMap<String, LoadState>();

    // Assignment Server gets a request that looks like this:
    // User: Alec, Load Assignment X, Load Level Y, At location Z

    // Player can be in one of these states:

    public BFEAssignmentServer(BFESocketServer socketServer) {
        m_socketServer = socketServer;
        m_assignmentLoader = new AssignmentLoader();
    }

    public void Handle(SocketChannel socketChannel, BFEProtos.BFELoadLevelRequest request) {
        // QUERY the DB for needed information
        try {
            Statement statement = Blaze.SqlInstance.createStatement();

            // Fuck me that is an ugly QUERY... This could turn out to be a huge performance sink, may need
            // to pre-process this in Forge.
            ResultSet rs = statement.executeQuery(
                "SELECT users.firstName, " +
                        "users.lastName, " +
                        "users.username, users.arenaLocation, " +
                        "assignment_descriptions.dueDate, " +
                        "assignment_descriptions.open_date, " +
                        "assignment_descriptions.jarPath, " +
                        "user_levels.id" + "\n" +
                "FROM user_assignments\n" +
                "  JOIN users\n" +
                "    ON users.id = user_assignments.user_id\n" +
                "  JOIN assignment_descriptions\n" +
                "    ON assignment_descriptions.id = user_assignments.assignment_description_id\n" +
                "  JOIN level_descriptions\n" +
                "    ON level_descriptions.assignment_description_id = user_assignments.assignment_description_id\n" +
                "  JOIN user_levels\n" +
                "    ON user_levels.user_assignment_id = user_assignments.id\n" +
                "   AND user_levels.level_description_id = level_descriptions.id\n" +
                "WHERE user_assignments.authToken = \"" + request.getAuthToken() + "\"\n" +
                "  AND level_descriptions.levelNumber = " + request.getLevelNumber() + "");

            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            rs.next();
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String username = rs.getString("username");
            String arenaLocStr = rs.getString("arenaLocation");
            String dueDateStr = rs.getString("dueDate");
            java.util.Date dueDate = dateTimeFormat.parse(dueDateStr);
            String openDateStr = rs.getString("open_date");
            java.util.Date openDate = dateTimeFormat.parse(openDateStr);
            Location3D arenaLocation = new Location3D(arenaLocStr);
            String jarPath = rs.getString("jarPath");
            int userLevelId = rs.getInt("id");
            rs.next();

            if (openDate.compareTo(new java.util.Date()) > -1) {
                // Not yet open
                BFEProtos.BFELoadLevelResponse response = BFEProtos.BFELoadLevelResponse.newBuilder()
                        .setFailureReason("Assignment has not yet opened. Please wait for the assignment to open.")
                        .build();
                m_socketServer.send(socketChannel,
                        BFEProtos.BFEMessage.newBuilder()
                            .setExtension(BFEProtos.BFELoadLevelResponse.bFELoadLevelResponseExt, response)
                            .build()
                            .toByteArray());
                return;
            }

            if (dueDate.compareTo(new java.util.Date()) < 1) {
                // Past due
                BFEProtos.BFELoadLevelResponse response = BFEProtos.BFELoadLevelResponse.newBuilder()
                        .setFailureReason("The assignment is past due, you cannot work on this assignment any longer.")
                        .build();
                m_socketServer.send(socketChannel,
                        BFEProtos.BFEMessage.newBuilder()
                            .setExtension(BFEProtos.BFELoadLevelResponse.bFELoadLevelResponseExt, response)
                            .build()
                            .toByteArray());
                return;
            }

            LoadState loadState = m_loadedStatesByUsername.get(username);
            if (loadState == null) {
                loadState = new LoadState();
                m_loadedStatesByUsername.put(username, loadState);
            }

            // TODO: Switch this to a generated seed at some point. Not sure how I want to do it yet
            int seed = 0;

            BFEProtos.BFEMessage message = loadState.transitionState(m_assignmentLoader, jarPath, arenaLocation,
                    lastName + ", " + firstName, request.getLevelNumber(), seed, userLevelId);
            m_socketServer.send(socketChannel, message.toByteArray());

        } catch (SQLException e) {
            e.printStackTrace();
            BFEProtos.BFELoadLevelResponse response = BFEProtos.BFELoadLevelResponse.newBuilder()
                    .setFailureReason("Blaze SQL error: " + e.getStackTrace().toString())
                    .build();
            m_socketServer.send(socketChannel,
                    BFEProtos.BFEMessage.newBuilder()
                    .setExtension(BFEProtos.BFELoadLevelResponse.bFELoadLevelResponseExt, response)
                    .build()
                    .toByteArray());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public BlazeLevel getActiveLevelForPlayer(String username) {
        return m_loadedStatesByUsername.get(username).Level;
    }

}
