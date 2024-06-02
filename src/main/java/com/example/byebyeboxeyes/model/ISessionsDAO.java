package com.example.byebyeboxeyes.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ISessionsDAO {
    /**
     * Creates a record in the sessions table with a start time and no end time.
     *
     * @param userID The user identification number
     * @param timerID The timer identification number
     * @param unixStartTime A long, representing a unix time value
     * @return
     */
    int startSession(int userID, int timerID, long unixStartTime);

    /**
     * Updates a record in the sessions table with a unix end time
     *
     * @param sessionID The session identification number
     * @param unixEndTime A long, representing a unix time value
     */
    void endSession(int sessionID, long unixEndTime);

    /**
     * Creates a collection of data, representing the amount of time spent using timers per day, in seconds,
     * for a specific user
     *
     * @param userId The user to find session time for
     * @return The data, in a list of SessionsDataCollator.TotalDailyData objects
     */
    List<SessionsDataCollator.TotalDailyData> getTotalSessionTimePerDay(int userId);

    /**
     * Gets the total timer usage time on the current system-date
     *
     * @param userId The user to find associated sessions for
     * @return The data, in a list of SessionsDataCollator.TotalDailyData objects
     */
    List<SessionsDataCollator.TotalDailyData> getTotalUsageToday(int userId);

    /**
     * Gets the sum of all sessions total time. The return type is a list, but it should always have a length of one.
     *
     * @param userId The user to find associated sessions for
     * @return The data, in a list of SessionsDataCollator.TotalDailyData objects
     */
    List<SessionsDataCollator.TotalDailyData> getTotalUsageOverall(int userId);

    /**
     * Gets the total count of sessions where the start unix time falls on the same date as the current system time.
     *
     * @param userId The user to find associated sessions for
     * @return An integer representing the count of sessions
     */
    int getNumberOfSessionsToday(int userId);

    /**
     * Gets the total count of sessions for a user, regardless of date
     *
     * @param userId The user to find associated sessions for
     * @return An integer representing the count of all sessions
     */
    int getNumberOfSessionsOverall(int userId);

    /**
     * Deletes all records from the table associated with a given user id
     *
     * @param userId The user id to find associated sessions for
     */
    void deleteSessionsForUser(int userId);

    /**
     * Gets a list containing all the dates that a user has created a session
     *
     * @param userId The user id to find associated sessions for
     * @return A list of LocalDate's that a unixStartTime falls into
     */
    List<LocalDate> getUniqueSessionDates(int userId);
}
