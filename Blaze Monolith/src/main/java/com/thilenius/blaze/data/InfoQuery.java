package com.thilenius.blaze.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alec on 1/12/15.
 */
public class InfoQuery extends Queryable{
    public String AuthToken;
    public String FirstName;
    public String LastName;
    public String Username;

    public InfoQuery(String assignmentAuthToken) {
        AuthToken = assignmentAuthToken;
    }

    @Override
    public String getSqlQuery() {
        return "SELECT users.firstName, users.lastName, users.username\n" +
                "FROM user_assignments\n" +
                "  JOIN users\n" +
                "    ON users.id = user_assignments.user_id\n" +
                "WHERE user_assignments.authToken = \"" + AuthToken + "\"";
    }

    @Override
    public boolean processResultSet(ResultSet rs) throws SQLException {
        rs.next();
        FirstName = rs.getString("firstName");
        LastName = rs.getString("lastName");
        Username = rs.getString("username");
        return true;
    }

    @Override
    public void fromCache(Queryable rs) {
        InfoQuery cachedQuery = (InfoQuery) rs;
        FirstName = cachedQuery.FirstName;
        LastName = cachedQuery.LastName;
        Username = cachedQuery.Username;
    }

    @Override
    public String stringHash() {
        return "InfoQuery|" + AuthToken;
    }
}
