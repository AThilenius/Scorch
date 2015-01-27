package com.thilenius.blaze.data;

import com.thilenius.utilities.types.Location3D;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alec on 1/17/15.
 */
public class UserQuery extends Queryable{
    public String Username;
    public String FirstName;
    public String LastName;
    public Location3D ArenaLocation;
    public String Permissions;

    public UserQuery(String username) {
        Username = username;
    }

    public UserQuery(String username, String firstName, String lastName, Location3D arenaLocation, String permissions) {
        Username = username;
        FirstName = firstName;
        LastName = lastName;
        ArenaLocation = arenaLocation;
        Permissions = permissions;
    }

    @Override
    public String getSqlQuery() {
        return "SELECT users.firstName, users.lastName, users.arenaLocation, users.permissions\n" +
                "FROM users\n" +
                "WHERE users.username=\"" + Username + "\"";
    }

    @Override
    public boolean processResultSet(ResultSet rs) throws SQLException {
        rs.next();
        FirstName = rs.getString("firstName");
        LastName = rs.getString("lastName");
        ArenaLocation = new Location3D(rs.getString("arenaLocation"));
        Permissions = rs.getString("permissions");
        return true;
    }

    @Override
    public void fromCache(Queryable rs) {
        UserQuery cachedQuery = (UserQuery) rs;
        FirstName = cachedQuery.FirstName;
        LastName = cachedQuery.LastName;
        Username = cachedQuery.Username;
        ArenaLocation = cachedQuery.ArenaLocation;
        Permissions = cachedQuery.Permissions;
    }

    @Override
    public String stringHash() {
        return "UserQuery|" + Username;
    }
}
