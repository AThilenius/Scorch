package com.thilenius.blaze.data;

import com.thilenius.utilities.types.Location3D;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersQuery extends Queryable{

    public List<UserQuery> Users;

    @Override
    public String getSqlQuery() {
        return "SELECT users.username, users.firstName, users.lastName, users.arenaLocation, users.permissions\n" +
                "FROM users";
    }

    @Override
    public boolean processResultSet(ResultSet rs) throws SQLException {
        Users = new ArrayList<UserQuery>();
        while (rs.next()) {
            UserQuery user = new UserQuery(
                    rs.getString("username"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    new Location3D(rs.getString("arenaLocation")),
                    rs.getString("permissions"));
            Users.add(user);
        }
        return true;
    }

    @Override
    public void fromCache(Queryable rs) {
        UsersQuery cachedQuery = (UsersQuery) rs;
        Users = cachedQuery.Users;
    }

    @Override
    public String stringHash() {
        return "UsersQuery";
    }

    @Override
    public boolean isCacheValid() {
        return false;
    }
}

