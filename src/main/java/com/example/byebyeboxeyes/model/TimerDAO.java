package com.example.byebyeboxeyes.model;

import com.example.byebyeboxeyes.timer.Timer;
import com.example.byebyeboxeyes.model.SqliteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.concurrent.Task;

/**
 * The Data Access Object for the Timer class
 * Creates a Timer DB that is connected to the userID
 * Uses methods to handle creating the timer DB if one doesn't exist, saving timers to the logged users profile, loading the timers for the current logged user,
 * delete the timers from the DB, delete timers from the current logged users added timers,
 * update timers that have been edited, and set the favourite timers to the user profile
 */
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
    private static final String COLUMN_FAV = "favourite";

    private Connection connection;
    private static TimerDAO instance = new TimerDAO(SqliteConnection.getInstance());
    private TimerDAO(Connection connection) {
        this.connection = connection;
        createTableIfNotExists();
    }
    public static TimerDAO getInstance() {
        return instance;
    }

    /**
     * Create a timer table in the DB timers.db if one does not exist
     * sets the column ID as the primary key to autoincrement
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n"
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + COLUMN_USER_ID + " INTEGER NOT NULL,\n"
                + COLUMN_HOURS + " INTEGER NOT NULL,\n"
                + COLUMN_MINUTES + " INTEGER NOT NULL,\n"
                + COLUMN_SECONDS + " INTEGER NOT NULL,\n"
                + COLUMN_FAV + " INTEGER DEFAULT 0,\n"
                + COLUMN_STATUS + " TEXT\n"
                + ");";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Save a timer into the timers.db for the current logged-in user
     * @param userID saves the userID as the primary key in the timers.db table
     * @param hours int hours in the format 00 to 59
     * @param minutes int minutes in the format 00 to 59
     * @param seconds int sec in the format of 00 to 59
     * @param favourite favourite the selected timer to the favourite section
     * @return the saved userID with the entered hour,min,sec to the favourite column in the timers.db
     */
    public int saveTimer(int userID, int hours, int minutes, int seconds, int favourite) {
        String sql = "INSERT INTO timers(UserID, hours, minutes, seconds, favourite) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, hours);
            stmt.setInt(3, minutes);
            stmt.setInt(4, seconds);
            stmt.setInt(5, favourite);

            stmt.executeUpdate();

            // Getting the generated primary key
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                return -1; // Or throw an exception
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1; // Or throw an exception
        }
    }

    /**
     * Load the timers from the timers.db
     * @param userID userID associated with the timers.db
     * @param callback executed when the function completes or some event happens
     */
    public void loadTimers(int userID, TimersLoadCallback callback) {
        Task<ArrayList<Timer>> loadTimersTask = new Task<>() {
            @Override
            protected ArrayList<Timer> call() throws Exception {
                String sql = "SELECT " + COLUMN_ID + ", " + COLUMN_USER_ID + ", " + COLUMN_HOURS + ", " + COLUMN_MINUTES + ", "
                        + COLUMN_SECONDS + ", " + COLUMN_FAV + " FROM " + TABLE_NAME + " WHERE UserID = ?";

                ArrayList<Timer> timers = new ArrayList<>();

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, userID);

                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int timerID = rs.getInt(COLUMN_ID);
                            int hours = rs.getInt(COLUMN_HOURS);
                            int minutes = rs.getInt(COLUMN_MINUTES);
                            int seconds = rs.getInt(COLUMN_SECONDS);
                            int favourite = rs.getInt(COLUMN_FAV);

                            timers.add(new Timer(timerID, userID, hours, minutes, seconds, favourite));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error loading timers from the database", e);
                }
                return timers;
            }
        };

        loadTimersTask.setOnSucceeded(event -> callback.onTimersLoaded(loadTimersTask.getValue()));
        loadTimersTask.setOnFailed(event -> {
            Throwable exception = loadTimersTask.getException();
            exception.printStackTrace(); // Log the error for debugging
            // Handle the exception as needed (e.g., show an error message to the user)
        });

        new Thread(loadTimersTask).start(); // Run the task on a new thread
    }

    /**
     * Delete timer from table in timers.db
     * @param pk takes int for timer ID
     */
    public void deleteTimer(int pk) {
        String query = "DELETE FROM timers WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pk);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete the timer from current user
     * takes the current user and gets their ID to be deleted from their profile
     * @param userId int that takes the userID
     */
    public void deleteTimersForUser(int userId) {
        String query = "DELETE FROM timers WHERE userID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the timer table in the timers.db
     * @param pk get the timer ID
     * @param hours int hours field
     * @param minutes int minutes field
     * @param seconds int seconds field
     * @param isFavourite int for favourite timerID
     */
    public void updateTimer(int pk, int hours, int minutes, int seconds, int isFavourite) {
        String query = "UPDATE " + TABLE_NAME + " SET " + COLUMN_HOURS + " = ?, "
                + COLUMN_MINUTES + " = ?, " + COLUMN_SECONDS + " = ?, " + COLUMN_FAV + " = ? WHERE "
                + COLUMN_ID + " = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, hours);
            pstmt.setInt(2, minutes);
            pstmt.setInt(3, seconds);
            pstmt.setInt(4, isFavourite);
            pstmt.setInt(5, pk);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Set the timer to favourite on button click
     * @param timerId int for timerID
     * @return the int for the favourite timerIO
     */
    public int isTimerFavourite(int timerId) {
        String sql = "SELECT favourite FROM timers WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, timerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("favourite");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Default to not favourite if an error occurs or timer not found
    }

    /**
     * Set the user timer to the favourite section
     * @param timerId int for the timerID
     * @param favourite set the favourite value
     */
    public void setTimerFavourite(int timerId, int favourite) {
        String sql = "UPDATE timers SET favourite = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            System.out.println("Setting timer " + timerId + " to favourite: " + favourite);

            stmt.setInt(1, favourite); // Set the favourite value in the prepared statement
            stmt.setInt(2, timerId);
            stmt.executeUpdate();

            connection.commit();
            System.out.println("Updated favourite status in the database.");
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Database update failed, rolled back changes.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public interface TimersLoadCallback {
        void onTimersLoaded(ArrayList<Timer> timers);
    }

}

