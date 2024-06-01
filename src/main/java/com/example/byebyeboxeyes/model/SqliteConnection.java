package com.example.byebyeboxeyes.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Sqlite connection class to connect to the boxeyes.db
 * Users loggedin credentials will be stored and saved to the appropriate columns in the DB table
 */
public class SqliteConnection implements ISqliteConnection {
    private static Connection instance = null;

    /**
     * Create the connection to the database boxeyes.db
     */
    private SqliteConnection() {
        String url = "jdbc:sqlite:boxeyes.db";

        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }
    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }
}

