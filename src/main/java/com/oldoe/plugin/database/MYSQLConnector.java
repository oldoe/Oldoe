package com.oldoe.plugin.database;

import com.oldoe.plugin.Oldoe;

import java.sql.*;
import java.util.logging.Level;

public class MYSQLConnector {

    private Oldoe plugin;

    private Connection connection;
    private Statement stmt;
    private ResultSet rs;

    private String ip;
    private String database;
    private String username;
    private String password;

    public MYSQLConnector(Oldoe pl) {
        this.plugin = pl;

        ip = plugin.getConfig().getString("db_ip");
        database = plugin.getConfig().getString("db_name");
        username = plugin.getConfig().getString("db_username");
        password = plugin.getConfig().getString("db_password");

        getConnection();
        makeTables();
    }

    private void getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
            setConnection(DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&autoReconnect=true&useSSL=false&testOnBorrow=true&validationQuery='SELECT NOW()'&validationInterval=60000&testWhileIdle=true", ip , 3306, database, username, password)));
        } catch (Exception e) {
            plugin.getServer().getLogger().log(Level.SEVERE, plugin.getName() + " - DB Connection Error: " + e.getMessage());
        }
    }

    private void setConnection(Connection conn) {
        this.connection = conn;
    }

    public ResultSet executeSQL(String sql) {
        try {
            stmt = connection.createStatement();

            if (stmt.execute(sql)) {
                rs = stmt.getResultSet();
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "SQLException: " + e.getMessage());
            plugin.getLogger().log(Level.SEVERE, "SQLState: " + e.getSQLState());
            plugin.getLogger().log(Level.SEVERE, "VendorError: " + e.getErrorCode());
        }

        return rs;
    }

    private void makeTables() {
        // MySQL code to create the users table
        String usersTable = String.format("CREATE TABLE IF NOT EXISTS `%s`.`oldoe_users` (" +
                "`id` INT(11) NOT NULL AUTO_INCREMENT," +
                "`uuid` VARCHAR(255) NOT NULL ," +
                "`permission` INT(2) NOT NULL DEFAULT 5 ," +
                "`name` VARCHAR(255) NOT NULL ," +
                "`cash` DECIMAL(12,2) NOT NULL DEFAULT 0.00 ," +
                "PRIMARY KEY (`id`), " +
                "UNIQUE KEY `uuid` (`uuid`) " +
                ") ENGINE = InnoDB;", database);

        String bansTable = String.format("CREATE TABLE IF NOT EXISTS `%s`.`oldoe_bans` (" +
                "`id` INT(11) NOT NULL AUTO_INCREMENT," +
                "`uuid` VARCHAR(255) NOT NULL ," +
                "`name` VARCHAR(255) NOT NULL ," +
                "`reason` VARCHAR(255) NOT NULL ," +
                "`staff` INT(11) NOT NULL , " +
                "`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , " +
                "PRIMARY KEY (`id`), " +
                "UNIQUE KEY `uuid` (`uuid`) " +
                ") ENGINE = InnoDB;", database);

        // MySQL code to create the homes table
        String homesTable = String.format("CREATE TABLE IF NOT EXISTS `%s`.`oldoe_homes` ( " +
                "`id` INT(11) NOT NULL AUTO_INCREMENT , " +
                "`uuid` INT(11) NOT NULL , " +
                "`world` VARCHAR(255) NOT NULL , " +
                "`x` DOUBLE NOT NULL , " +
                "`y` DOUBLE NOT NULL , " +
                "`z` DOUBLE NOT NULL , " +
                "`pitch` FLOAT NOT NULL , " +
                "`yaw` FLOAT NOT NULL , " +
                "PRIMARY KEY (`id`)," +
                "UNIQUE KEY `uuid` (`uuid`), " +
                "FOREIGN KEY (`uuid`) REFERENCES oldoe_users(`id`) ON UPDATE CASCADE ON DELETE CASCADE" +
                ") ENGINE = InnoDB;", database);

        // MySQL code to create the plots table
        String plotsTable = String.format("CREATE TABLE IF NOT EXISTS `%s`.`oldoe_plots` ( " +
                "`id` INT(11) NOT NULL AUTO_INCREMENT , " +
                "`owner` INT(11) NOT NULL , " +
                "`world` VARCHAR(255) NOT NULL , " +
                "`name` VARCHAR(255) NOT NULL , " +
                "`x` Int(11) NOT NULL , " +
                "`z` Int(11) NOT NULL , " +
                "PRIMARY KEY (`id`)," +
                "FOREIGN KEY (`owner`) REFERENCES oldoe_users(`id`) ON UPDATE CASCADE ON DELETE CASCADE" +
                ") ENGINE = InnoDB;", database);

        // MySQL code to create the plots permission table
        String plotPermTable = String.format("CREATE TABLE IF NOT EXISTS `%s`.`oldoe_plot_perms` ( " +
                "`id` INT(11) NOT NULL AUTO_INCREMENT , " +
                "`plot` INT(11) NOT NULL , " +
                "`user` INT(11) NOT NULL , " +
                "PRIMARY KEY (`id`)," +
                "FOREIGN KEY (`plot`) REFERENCES oldoe_plots(`id`) ON UPDATE CASCADE ON DELETE CASCADE, " +
                "FOREIGN KEY (`user`) REFERENCES oldoe_users(`id`) ON UPDATE CASCADE ON DELETE CASCADE" +
                ") ENGINE = InnoDB;", database);

        // Execute queries and close statement
        executeSQL(usersTable);
        executeSQL(bansTable);
        executeSQL(homesTable);
        executeSQL(plotsTable);
        executeSQL(plotPermTable);
        close();
    }

    public int getPlayerID(String uuid) {
        int userID = -1;
        int count = 0;

        while(count < 1) {

            try {
                String sql = String.format("SELECT id FROM oldoe_users WHERE uuid LIKE '%s'", uuid);
                ResultSet resultSet = executeSQL(sql);

                if (resultSet != null) {
                    while (resultSet.next()) {
                        userID = resultSet.getInt("id");
                    }
                    break; // If we got an answer without exception, break.
                }

            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, e.getMessage());
            }

            count++;
        }
        return userID;
    }

    public void UpdatePlayerName(String uuid, String name) {
        String sql = String.format("UPDATE `oldoe_users` SET `name` = '%s' WHERE `uuid` LIKE '%s'", name, uuid);
        executeSQL(sql);
        close();
    }

    public void close() {
        if (this.rs != null) {
            try {
                this.rs.close();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, e.getMessage());
            }
        }

        if (this.stmt != null) {
            try {
                this.stmt.close();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, e.getMessage());
            }
        }

        this.rs = null;
        this.stmt = null;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = null;
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, e.getMessage());
        }
    }
}
