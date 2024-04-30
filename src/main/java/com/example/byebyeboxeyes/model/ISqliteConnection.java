package com.example.byebyeboxeyes.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface ISqliteConnection {
    static Connection connection = null;
    static Connection getInstance() {
        return null;
    }
}
