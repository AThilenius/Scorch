package com.thilenius.blaze.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alec on 1/17/15.
 */
public class PointsQuery extends Queryable implements IUpdateable {
    public int UserLevelId;
    public int Points;

    public PointsQuery(int userLevelId) {
        UserLevelId = userLevelId;
    }

    @Override
    public boolean isCacheValid() {
        // Never cache
        return false;
    }

    @Override
    public String getSqlQuery() {
        return  "SELECT points\n" +
                "FROM user_levels\n" +
                "WHERE id=" + UserLevelId;
    }

    public String getSqlUpdate() {
        return "UPDATE user_levels\n" +
                "SET points = CASE WHEN points < " + Points + "\n" +
                "THEN " + Points + " ELSE points END\n" +
                "WHERE id=" + UserLevelId;
    }

    @Override
    public boolean processResultSet(ResultSet rs) throws SQLException {
        rs.next();
        Points = rs.getInt("points");
        return true;
    }

    @Override
    public void fromCache(Queryable rs) {
        PointsQuery cachedQuery = (PointsQuery) rs;
        UserLevelId = cachedQuery.UserLevelId;
        Points = cachedQuery.Points;
    }

    @Override
    public String stringHash() {
        return "PointsQuery|" + UserLevelId;
    }
}
