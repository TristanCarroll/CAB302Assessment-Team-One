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
    private static final String COLUMN_USER_ID = "UserID";
    private static final String COLUMN_HOURS = "hours";
    private static final String COLUMN_MINUTES = "minutes";
    private static final String COLUMN_SECONDS = "seconds";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_START_TIME = "StartTime";
    private static final String COLUMN_END_TIME = "EndTime";
    private Connection connection;
    private static TimerDAO instance = new TimerDAO(SqliteConnection.getInstance());
    private TimerDAO(Connection connection) {
        this.connection = connection;
        createTableIfNotExists();
    }
    public static TimerDAO getInstance() {
        return instance;
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n"
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + COLUMN_USER_ID + " INTEGER NOT NULL,\n"
                + COLUMN_HOURS + " INTEGER NOT NULL,\n"
                + COLUMN_MINUTES + " INTEGER NOT NULL,\n"
                + COLUMN_SECONDS + " INTEGER NOT NULL,\n"
                + COLUMN_STATUS + " TEXT\n"
                + ");";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveTimer(Timer timer) {
        String sql = "INSERT INTO timers(UserID, hours, minutes, seconds) VALUES(?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, timer.getUserID());
            stmt.setInt(2, timer.getHours());
            stmt.setInt(3, timer.getMinutes());
            stmt.setInt(4, timer.getSeconds());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Timer> loadTimers(int userID) {
        String sql = "SELECT " + COLUMN_ID + ", " + COLUMN_USER_ID + ", " +  COLUMN_HOURS + ", " + COLUMN_MINUTES + ", "
                + COLUMN_SECONDS + " FROM " + TABLE_NAME +
                " WHERE UserID = " + userID;

        List<Timer> timers = new ArrayList<>();

        try (PreparedStatement stmt  = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                timers.add(new Timer(
                        rs.getInt(COLUMN_USER_ID),
                        rs.getInt(COLUMN_HOURS),
                        rs.getInt(COLUMN_MINUTES),
                        rs.getInt(COLUMN_SECONDS)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return timers;
    }
    public void deleteTimer(String pk) throws Exception {
        String query = "DELETE FROM timers WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pk);
            statement.executeUpdate();
        }
    }
}

