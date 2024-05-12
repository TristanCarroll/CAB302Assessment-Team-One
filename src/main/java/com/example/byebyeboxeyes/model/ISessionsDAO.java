package com.example.byebyeboxeyes.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ISessionsDAO {
    int startSession(int userID, int timerID, long unixStartTime);
    void endSession(int sessionID, long unixEndTime);
}
