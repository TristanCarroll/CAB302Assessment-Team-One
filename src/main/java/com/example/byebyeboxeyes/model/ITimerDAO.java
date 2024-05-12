package com.example.byebyeboxeyes.model;

import com.example.byebyeboxeyes.timer.Timer;

import java.util.ArrayList;
 interface ITimerDAO {
    int saveTimer(int userID, int hours, int minutes, int seconds);
    ArrayList<Timer> loadTimers(int userID);
    void deleteTimer(int pk);
    void updateTimer(int pk, int hours, int minutes, int seconds);
}
