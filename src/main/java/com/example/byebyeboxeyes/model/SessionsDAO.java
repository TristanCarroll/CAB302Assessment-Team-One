package com.example.byebyeboxeyes.model;

import com.example.byebyeboxeyes.model.SessionsDataCollator.TotalDailyData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        String sql =
                "UPDATE sessions\n" +
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
    public List<TotalDailyData> getTotalSessionTimePerDay(int userId) {
        List<TotalDailyData> data = new ArrayList<>();

        String sql =
                // TODO:
                //  This considers only the start time for calculating averages.
                //  It's arguable whether or not this is correct for sessions occuring over midnight.
                "SELECT date(datetime(unixStartTime, 'unixepoch')) AS sessionDate, " +
                        "SUM((unixEndTime - unixStartTime)) AS totalSessionTime " +
                        "FROM sessions " +
                        "WHERE unixEndTime IS NOT NULL " +
                        "AND userID = ? " +
                        "GROUP BY sessionDate";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String date = rs.getString("sessionDate");
                double averageTime = rs.getDouble("totalSessionTime");
                data.add(new TotalDailyData(date, averageTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    public List<TotalDailyData> getTotalUsageToday(int userId) {
        List<TotalDailyData> data = new ArrayList<>();

        String sql =
                "SELECT SUM((unixEndTime - unixStartTime)) AS totalUsage " +
                        "FROM sessions " +
                        "WHERE unixEndTime IS NOT NULL " +
                        "AND userID = ? " +
                        "AND date(datetime(unixStartTime, 'unixepoch')) = date('now')";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double totalUsage = rs.getDouble("totalUsage");
                data.add(new TotalDailyData(LocalDate.now().toString(), totalUsage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
    public List<TotalDailyData> getTotalUsageOverall(int userId) {
        List<TotalDailyData> data = new ArrayList<>();

        String sql =
                "SELECT SUM((unixEndTime - unixStartTime)) AS totalUsage " +
                        "FROM sessions " +
                        "WHERE unixEndTime IS NOT NULL " +
                        "AND userID = ? ";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double totalUsage = rs.getDouble("totalUsage");
                data.add(new TotalDailyData(LocalDate.now().toString(), totalUsage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
    public int getNumberOfSessionsToday(int userId) {
        int numberOfSessions = 0;

        String sql =
                "SELECT COUNT(*) AS sessionCount " +
                        "FROM sessions " +
                        "WHERE userID = ? " +
                        "AND date(datetime(unixStartTime, 'unixepoch')) = date('now')";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                numberOfSessions = rs.getInt("sessionCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfSessions;
    }
    public int getNumberOfSessionsOverall(int userId) {
        int numberOfSessions = 0;

        String sql =
                "SELECT COUNT(*) AS sessionCount " +
                        "FROM sessions " +
                        "WHERE userID = ? ";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                numberOfSessions = rs.getInt("sessionCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfSessions;
    }
//    public void insertMockDataForCharts(int userID, int timerID, long unixStartTime, long unixEndtime) {
//        String sql = "INSERT INTO sessions(userID, timerID, unixStartTime, unixEndtime) VALUES(?, ?, ?, ?)";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setInt(1, userID);
//            stmt.setInt(2, timerID);
//            stmt.setLong(3, unixStartTime);
//            stmt.setLong(4, unixEndtime);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
