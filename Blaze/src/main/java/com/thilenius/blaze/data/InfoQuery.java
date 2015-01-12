package com.thilenius.blaze.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alec on 1/12/15.
 */
public class InfoQuery {
    public String FirstName;
    public String LastName;

    public static String getQuery(String assignmentAuthToken) {
        return "SELECT users.firstName, users.lastName\n" +
                "FROM user_assignments\n" +
                "  JOIN users\n" +
                "    ON users.id = user_assignments.user_id\n" +
                "WHERE user_assignments.authToken = \"" + assignmentAuthToken + "\"";
    }

    public static InfoQuery fromResultSet(ResultSet rs) throws SQLException {
        InfoQuery result = new InfoQuery();
        rs.next();
        result.FirstName = rs.getString("firstName");
        result.LastName = rs.getString("lastName");
        return result;
    }
}
