package com.example.byebyeboxeyes.model;

import com.example.byebyeboxeyes.model.SessionsDataCollator.TotalDailyData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of sessionsDAO. Creates a session table if one doesn't exist and contains methods
 * with JDBC queries that perform CRUD operations on the table.
 */
public class SessionsDAO implements ISessionsDAO {
    private Connection connection;
    private static SessionsDAO instance = new SessionsDAO(SqliteConnection.getInstance());
    private SessionsDAO(Connection connection) {
        this.connection = connection;
        createTableIfNotExists();
    }

    /**
     * Get an instance of the SessionsDAO
     *
     * @return The singleton instance of SessionsDAO
     */
    public static SessionsDAO getInstance() {
        return instance;
    }

    /**
     * Creates a table in the database called "users" with the appropriate columns, if one does not already exist.
     */
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

    /**
     * Creates a record in the sessions table with a start time and no end time.
     *
     * @param userID The user identification number
     * @param timerID The timer identification number
     * @param unixStartTime A long, representing a unix time value
     * @return
     */
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
    /**
     * Updates a record in the sessions table with a unix end time
     *
     * @param sessionID The session identification number
     * @param unixEndTime A long, representing a unix time value
     */
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
    /**
     * Creates a collection of data, representing the amount of time spent using timers per day, in seconds,
     * for a specific user
     *
     * @param userId The user to find session time for
     * @return The data, in a list of SessionsDataCollator.TotalDailyData objects
     */
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
    /**
     * Gets the total timer usage time on the current system-date
     *
     * @param userId The user to find associated sessions for.
     * @return The data, in a list of SessionsDataCollator.TotalDailyData objects
     */
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
    /**
     * Gets the sum of all sessions total time. The return type is a list, but it should always have a length of one.
     *
     * @param userId The user to find associated sessions for
     * @return The data, in a list of SessionsDataCollator.TotalDailyData objects
     */
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
    /**
     * Gets the total count of sessions where the start unix time falls on the same date as the current system time.
     *
     * @param userId The user to find associated sessions for
     * @return An integer representing the count of sessions
     */
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
    /**
     * Gets the total count of sessions for a user, regardless of date
     *
     * @param userId The user to find associated sessions for
     * @return An integer representing the count of all sessions
     */
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
    /**
     * Deletes all records from the table associated with a given user id
     *
     * @param userId The user id to find associated sessions for
     */
    public void deleteSessionsForUser(int userId) {
        String query = "DELETE FROM sessions WHERE userID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Gets a list containing all the dates that a user has created a session
     *
     * @param userId The user id to find associated sessions for
     * @return A list of LocalDate's that a unixStartTime falls into
     */
    public List<LocalDate> getUniqueSessionDates(int userId) {
        List<LocalDate> dates = new ArrayList<>();
        String sql = "SELECT DISTINCT DATE(datetime(unixStartTime, 'unixepoch')) AS sessionDate " +
                "FROM sessions " +
                "WHERE userID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dates.add(LocalDate.parse(rs.getString("sessionDate")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
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
