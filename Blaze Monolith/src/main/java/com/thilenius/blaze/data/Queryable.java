package com.thilenius.blaze.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alec on 1/12/15.
 */
public abstract class Queryable {
    public long CacheTimeout = 10 * 60;
    public long CacheTimeMs;

    public boolean isCacheValid() {
        return (System.currentTimeMillis() - CacheTimeMs) < (CacheTimeout * 1000);
    }

    public abstract String getSqlQuery();
    public abstract boolean processResultSet(ResultSet rs) throws SQLException;
    public abstract void fromCache(Queryable rs);
    public abstract String stringHash();
}
