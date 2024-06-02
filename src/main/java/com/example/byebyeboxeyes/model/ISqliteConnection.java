package com.example.byebyeboxeyes.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface ISqliteConnection {
    /**
     * Get an instance of a class that implements ISqliteConnection
     * @return A connection object
     */
    static Connection getInstance() {
        return null;
    }
}
