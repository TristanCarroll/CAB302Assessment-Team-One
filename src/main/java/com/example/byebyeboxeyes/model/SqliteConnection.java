package com.example.byebyeboxeyes.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class SqliteConnection implements ISqliteConnection {
    private static Connection instance = null;

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

