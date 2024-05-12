package com.example.byebyeboxeyes.model;

import com.example.byebyeboxeyes.timer.Timer;
import com.example.byebyeboxeyes.model.SqliteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimerDAO implements ITimerDAO {
    private static final String DB_NAME = "timers.db";

    // Table and column names
    private static final String TABLE_NAME = "timers";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "UserID";
    private static final String COLUMN_HOURS = "hours";
    private static final String COLUMN_MINUTES = "minutes";
    private static final String COLUMN_SECONDS = "seconds";
    private static final String COLUMN_STATUS = "status";
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

    public int saveTimer(int userID, int hours, int minutes, int seconds) {
        String sql = "INSERT INTO timers(UserID, hours, minutes, seconds) VALUES(?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, hours);
            stmt.setInt(3, minutes);
            stmt.setInt(4, seconds);
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

    public ArrayList<Timer> loadTimers(int userID) {
        String sql = "SELECT " + COLUMN_ID + ", " + COLUMN_USER_ID + ", " +  COLUMN_HOURS + ", " + COLUMN_MINUTES + ", "
                + COLUMN_SECONDS + " FROM " + TABLE_NAME +
                " WHERE UserID = " + userID;

        ArrayList<Timer> timers = new ArrayList<>();

        try (PreparedStatement stmt  = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                timers.add(new Timer(
                        rs.getInt(COLUMN_ID),
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
    public void deleteTimer(int pk) {
        String query = "DELETE FROM timers WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pk);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateTimer(int pk, int hours, int minutes, int seconds) {
        String query =
                "UPDATE " + TABLE_NAME + "\n" +
                "SET " + COLUMN_HOURS + " = ?, " +
                COLUMN_MINUTES + " = ?, " +
                COLUMN_SECONDS + " = ?\n" +
                "WHERE " + COLUMN_ID + " = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, hours);
            pstmt.setInt(2, minutes);
            pstmt.setInt(3, seconds);
            pstmt.setInt(4, pk);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

