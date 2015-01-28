package com.thilenius.blaze.data;

import com.thilenius.blaze.Blaze;

import java.sql.*;
import java.util.HashMap;

// Manages a SQL or HTTP connection for remote data access
public class RemoteData {
    private Connection m_sqlInstance;
    private HashMap<String, Queryable> m_queryCache = new HashMap<String, Queryable>();

    public void connect() {
        if (m_sqlInstance != null) {
            return;
        }

        try
        {
            // First try to connect to production.
            try {
                System.out.println("Trying to connect DEVELOPMENT AWS RDS: MySQL");
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                m_sqlInstance = DriverManager.getConnection(
                        "jdbc:mysql://forge-dev.cfqsj371kgit.us-west-1.rds.amazonaws.com:3306/forgedb?" +
                                "user=admin&password=forgeadmin");
                System.out.println("Connected to DEVELOPMENT AWS RDS");
                return;
            } catch(SQLException e) {
                // Do nothing, fallback to Dev
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (InstantiationException e) {
                System.out.println(e.getMessage());
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("Failed to connect. Falling back to Development DB: SqlLite");
            Class.forName("org.sqlite.JDBC");
            m_sqlInstance = DriverManager.getConnection(
                    "jdbc:sqlite:/Users/Alec/Documents/Development/Forge/db/development.sqlite3");


            System.out.println("Connected to SQL Lite.");
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        if (m_sqlInstance == null) {
            connect();
            return m_sqlInstance;
        }

        // Check if it's valid
        try {
            if (!m_sqlInstance.isValid(5)) {

                // Reconnect
                try { m_sqlInstance.close(); } catch (SQLException e) { }
                m_sqlInstance = null;
                connect();
                return m_sqlInstance;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            // Force close and reconnect
            try { m_sqlInstance.close(); } catch (SQLException e1) { }
            m_sqlInstance = null;
            connect();
            return m_sqlInstance;
        }

        // Still an active connection
        return m_sqlInstance;
    }

    public boolean query(Queryable query) {
        Statement statement = null;
        ResultSet resultSet = null;

        // Cache Check
        synchronized (m_queryCache) {
            if (m_queryCache.containsKey(query.stringHash())) {

                // Expired?
                if (!m_queryCache.get(query.stringHash()).isCacheValid()) {
                    m_queryCache.remove(query.stringHash());
                } else {
                    long startTime = System.currentTimeMillis();
                    query.fromCache(m_queryCache.get(query.stringHash()));
                    System.out.println("[CACHE] SQL Query: " + query.getClass().getName() + ". Took: " +
                            (System.currentTimeMillis() - startTime) + "ms.");
                    return true;
                }
            }
        }

        try {
            long startTime = System.currentTimeMillis();
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query.getSqlQuery());
            if (resultSet.isBeforeFirst()) {
                boolean result = query.processResultSet(resultSet);
                if (result == true) {
                    // Cache it for later use
                    query.CacheTimeMs = System.currentTimeMillis();
                    synchronized (m_queryCache) {
                        m_queryCache.put(query.stringHash(), query);
                    }
                }
                System.out.println("SQL Query: " + query.getClass().getName() + ". Took: " +
                        (System.currentTimeMillis() - startTime) + "ms.");
                return result;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
        }
    }

    public boolean update(IUpdateable updateable) {
        class RemoteDataUpdateTask implements Runnable {
            private IUpdateable m_updateable;
            public RemoteDataUpdateTask(IUpdateable taskUneatable) { m_updateable = taskUneatable; }

            @Override
            public void run() {
                Statement statement = null;
                try {
                    long startTime = System.currentTimeMillis();
                    statement = getConnection().createStatement();
                    statement.executeUpdate(m_updateable.getSqlUpdate());
                    System.out.println("SQL Update: " + m_updateable.getClass().getName() + ". Took: " +
                            (System.currentTimeMillis() - startTime) + "ms on a background thread.");
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try { if (statement != null) statement.close(); } catch (Exception e) {};
                }
            }

        }

        Blaze.ThreadPool.execute(new RemoteDataUpdateTask(updateable));
        return true;
    }

    public void forceCacheFlush() {
        synchronized (m_queryCache) {
            m_queryCache.clear();
        }

        System.out.println("Blaze SQL cache cleared.");
    }
}
