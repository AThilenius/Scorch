package com.thilenius.blaze;

import com.sampullara.cli.Argument;

/**
 * Created by Alec on 2/26/15.
 */
public class BlazeArgs {

    @Argument(value = "db_host", description = "The host address for the master Database (IP address or Hostname. Ex: forge-dev.cfqsj371kgit.us-west-1.rds.amazonaws.com)", required = true)
    private String db_host;

    @Argument(value = "db_port", description = "The port for the master Database")
    private Integer db_port = 3306;

    @Argument(value = "db_database", description = "What database to use", required = true)
    private String db_database;

    @Argument(value = "db_login", description = "The login for the database", required = true)
    private String db_login;

    @Argument(value = "db_password", description = "The password for the database", required = true)
    private String db_password;

    public String getDbHostNoWWW() {
        String result = db_host;
        if (db_host.startsWith("www.")) {
            result.replace("www.", "");
        }
        return result;
    }

    public String getDbHost() {
        return db_host;
    }

    public int getDbPort() {
        return db_port;
    }

    public String getDbDatabase() {
        return db_database;
    }

    public String getDbLogin() {
        return db_login;
    }

    public String getDbPassword() {
        return db_password;
    }

    public String getMySqlConnectionString() {
        return "jdbc:mysql://" + getDbHostNoWWW() + ":" + getDbPort() + "/" + getDbDatabase() + "?user=" + getDbLogin()
                + "&password=" + getDbPassword();
    }

}
