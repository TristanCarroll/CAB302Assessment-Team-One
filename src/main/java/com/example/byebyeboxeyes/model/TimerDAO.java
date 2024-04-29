package com.example.byebyeboxeyes.model;

import com.example.byebyeboxeyes.timer.Timer;
import com.example.byebyeboxeyes.model.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TimerDAO {
    private static final String DB_NAME = "timers.db";

    // Table and column names
    private static final String TABLE_NAME = "timers";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HOURS = "hours";
    private static final String COLUMN_MINUTES = "minutes";
    private static final String COLUMN_SECONDS = "seconds";
    private static final String COLUMN_STATUS = "status";

    public TimerDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n"
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + COLUMN_HOURS + " INTEGER NOT NULL,\n"
                + COLUMN_MINUTES + " INTEGER NOT NULL,\n"
                + COLUMN_SECONDS + " INTEGER NOT NULL,\n"
                + COLUMN_STATUS + " TEXT\n"
                + ");";

        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveTimer(Timer timer) {
        String sql = "INSERT INTO timers(hours, minutes, seconds) VALUES(?, ?, ?)";

        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, timer.getHours());
            pstmt.setInt(2, timer.getMinutes());
            pstmt.setInt(3, timer.getSeconds());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Timer> loadTimers() {
        String sql = "SELECT " + COLUMN_ID + ", " + COLUMN_HOURS + ", " + COLUMN_MINUTES + ", "
                + COLUMN_SECONDS + " FROM " + TABLE_NAME;

        List<Timer> timers = new ArrayList<>();

        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement stmt  = conn.prepareStatement(sql);
             ResultSet rs    = stmt.executeQuery()){

            while (rs.next()) {
                timers.add(new Timer(
                        rs.getInt(COLUMN_HOURS),
                        rs.getInt(COLUMN_MINUTES),
                        rs.getInt(COLUMN_SECONDS)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return timers;
    }
}

