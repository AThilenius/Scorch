package com.thilenius.blaze.data;

import com.thilenius.blaze.Blaze;

import java.sql.*;

// Manages a SQL or HTTP connection for remote data access
public class RemoteData {
    private Connection m_sqlInstance;

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
                                "autoReconnect=true?user=admin&password=forgeadmin");
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

    public InfoQuery getInfo(String assignmentAuthToken) {
        Statement statement = null;
        ResultSet resultSet = null;
        InfoQuery infoQuery = null;
        try {
            statement = Blaze.SqlInstance.createStatement();
            resultSet = statement.executeQuery(InfoQuery.getQuery(assignmentAuthToken));
            if (resultSet.isBeforeFirst()) {
                infoQuery = InfoQuery.fromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
        }

        return infoQuery;
    }

}
