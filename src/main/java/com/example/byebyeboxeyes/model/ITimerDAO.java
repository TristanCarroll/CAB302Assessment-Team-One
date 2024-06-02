package com.example.byebyeboxeyes.model;

import com.example.byebyeboxeyes.timer.Timer;

import java.util.ArrayList;
 interface ITimerDAO {
     /**
      * Adds a record to the timer table
      *
      * @param userID The user identification number the timer is associated with
      * @param hours The humber of hours on the timer
      * @param minutes The number of minutes on the timer
      * @param seconds The number of seconds on the timer
      * @param favourite An integer value, either 1 or 0, representing whether the timer is favourited
      * @return
      */
    int saveTimer(int userID, int hours, int minutes, int seconds, int favourite);

     /**
      * Creates a task which returns all the timers associated with a specific user
      *
      * @param userID The user identification number to find timers for
      * @param callback Function used to access the value of the task on success
      */
    void loadTimers(int userID, com.example.byebyeboxeyes.model.TimerDAO.TimersLoadCallback callback);

     /**
      * Delete a record from the timer table based on it's primary key
      *
      * @param pk The primary key
      */
    void deleteTimer(int pk);

     /**
      * Deletes all records from the timer table associated with a specific user id
      *
      * @param userId the user Id
      */
    void deleteTimersForUser(int userId);

     /**
      * Updates a record in the timer table based on the provided primary key
      *
      * @param pk The primary key
      * @param hours The new number of hours
      * @param minutes The new number of minutes
      * @param seconds The new number of seconds
      * @param favourite The new favourite's status
      */
    void updateTimer(int pk, int hours, int minutes, int seconds, int favourite);

     /**
      * Returns the integer value of favourite from the timer table, for a specific timer
      * @param timerId The timer identification number/primary key
      * @return An integer indicating whether the timer is a favourite
      */
    int isTimerFavourite(int timerId);

     /**
      * Updates the favourite column for a record in the timer table
      *
      * @param timerId The identification number of the timer to update
      * @param favourite The new value for the cell
      */
    void setTimerFavourite(int timerId, int favourite);

}
