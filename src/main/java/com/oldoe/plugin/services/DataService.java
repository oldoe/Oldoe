package com.oldoe.plugin.services;

import com.oldoe.plugin.Oldoe;
import com.oldoe.plugin.database.MYSQLConnector;

public class DataService {

    private static MYSQLConnector dbConnector = null;
    public static MYSQLConnector getDatabase() {
        return dbConnector;
    }

    public void registerService(Oldoe pl) {
        this.dbConnector = new MYSQLConnector(pl);

    }

    public void DisableService() {
        // Close any existing mysql connections
        if (dbConnector != null) {
            dbConnector.closeConnection();
        }
    }
}
