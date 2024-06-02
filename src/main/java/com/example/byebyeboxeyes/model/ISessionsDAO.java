package com.example.byebyeboxeyes.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
