package com.thilenius.blaze.data;

import com.thilenius.blaze.assignment.AssignmentLoader;
import com.thilenius.utilities.types.Location3D;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Alec on 1/12/15.
 */
public class AssignmentQuery extends Queryable {
    public String AuthToken;
    public int LevelNumber;
    public String FirstName;
    public String LastName;
    public String Username;
    public Location3D ArenaLocation;
    public String JarPath;
    public int UserLevelId;
    public java.util.Date DueDate;
    public java.util.Date OpenDate;

    public AssignmentQuery(String authToken, int levelNumber) {
        AuthToken = authToken;
        LevelNumber = levelNumber;
    }

    @Override
    public String getSqlQuery() {
        return  "SELECT users.firstName, " +
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
                "WHERE user_assignments.authToken = \"" + AuthToken + "\"\n" +
                "  AND level_descriptions.levelNumber = " + LevelNumber + "";
    }

    @Override
    public boolean processResultSet(ResultSet rs) throws SQLException {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        rs.next();
        FirstName = rs.getString("firstName");
        LastName = rs.getString("lastName");
        Username = rs.getString("username");
        String arenaLocStr = rs.getString("arenaLocation");
        ArenaLocation = new Location3D(arenaLocStr);
        JarPath = rs.getString("jarPath");
        UserLevelId = rs.getInt("id");
        try {
            String dueDateStr = rs.getString("dueDate");
            DueDate = dateTimeFormat.parse(dueDateStr);
            String openDateStr = rs.getString("open_date");
            OpenDate = dateTimeFormat.parse(openDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void fromCache(Queryable rs) {
        AssignmentQuery cachedQuery = (AssignmentQuery) rs;
        FirstName = cachedQuery.FirstName;
        LastName = cachedQuery.LastName;
        Username = cachedQuery.Username;
        ArenaLocation = cachedQuery.ArenaLocation;
        JarPath = cachedQuery.JarPath;
        UserLevelId = cachedQuery.UserLevelId;
        DueDate = cachedQuery.DueDate;
        OpenDate = cachedQuery.OpenDate;
    }

    @Override
    public String stringHash() {
        return "AssignmentQuery|" + AuthToken + "|" + LevelNumber;
    }
}
