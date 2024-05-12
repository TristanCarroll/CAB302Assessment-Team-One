package com.example.byebyeboxeyes.model;

import com.example.byebyeboxeyes.timer.Timer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SessionsDAO implements ISessionsDAO {
    private Connection connection;
    private static SessionsDAO instance = new SessionsDAO(SqliteConnection.getInstance());
    private SessionsDAO(Connection connection) {
        this.connection = connection;
        createTableIfNotExists();
    }
    public static SessionsDAO getInstance() {
        return instance;
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS sessions \n(" +
                "sessionID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "userID INTEGER NOT NULL,\n" +
                "timerID INTEGER NOT NULL,\n" +
                "unixStartTime BIGINT NOT NULL,\n" +
                "unixEndTime BIGINT\n" +
                ");";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public int startSession(int userID, int timerID, long unixStartTime) {
        String sql = "INSERT INTO sessions(userID, timerID, unixStartTime) VALUES(?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, timerID);
            stmt.setLong(3, unixStartTime);
            stmt.executeUpdate();

            // Getting the generated primary key
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
    public void endSession(int sessionID, long unixEndTime) {
        String sql = "UPDATE sessions\n" +
                "SET unixEndTime = ?\n" +
                "WHERE sessionID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, unixEndTime);
            stmt.setInt(2, sessionID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
