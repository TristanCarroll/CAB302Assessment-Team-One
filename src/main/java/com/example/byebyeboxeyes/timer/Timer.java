package com.example.byebyeboxeyes.timer;

/**
 * Timer class to handle the applications timer function
 * handles running time, favourite inputs, deleting timers
 * Includes getters and setters
 */
public class Timer {
    private int timerID;
    private int userID;
    private int hours;
    private int minutes;
    private int seconds;
    private int decrementedHours;
    private int decrementedMinutes;
    private int decrementedSeconds;
    private int isFavourite;

    /**
     *
     * @param timerID ID of the input timer
     * @param userID userID associated with their timer
     * @param hours hours field for the timer
     * @param minutes minutes field for the timer
     * @param seconds seconds field for the timer
     * @param isFavourite favourite option for user to select
     */
    public Timer(int timerID, int userID, int hours, int minutes, int seconds, int isFavourite) {
        this.timerID = timerID;
        this.userID = userID;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.isFavourite = isFavourite;
        this.decrementedHours = hours;
        this.decrementedMinutes = minutes;
        this.decrementedSeconds = seconds;
    }

    // Getters, setters
    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }
    public int getUserID() { return userID; }
    public int getTimerID() { return timerID; }
    public int getIsFavourite() { return isFavourite; }
    public void setHours(int hours) {
        this.hours = hours;
    }
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    public void setIsFavourite(int isFavourite) { this.isFavourite = isFavourite; }
    public void setDecrementedHours(int decrementedHours) { this.decrementedHours = decrementedHours; };
    public void setDecrementedMinutes(int decrementedMinutes) { this.decrementedMinutes = decrementedMinutes; }
    public void setDecrementedSeconds(int decrementedSeconds) { this.decrementedSeconds = decrementedSeconds; }

    /**
     * Timer method to decrement the running timer
     * @return the string format of the input time value
     */
    public String decrementTime() {
        if (decrementedSeconds > 0) {
            decrementedSeconds--;
        } else if (minutes > 0) {
            decrementedMinutes--;
            decrementedSeconds = 59;
        } else if (hours > 0) {
            decrementedHours--;
            decrementedMinutes = 59;
            decrementedSeconds = 59;
        }
        return String.format("%02d:%02d:%02d", decrementedHours, decrementedMinutes, decrementedSeconds);
    }

    /**
     * Method to handle a finished timer
     * @return the reset hours, minutes, and seconds
     */
    public boolean isFinished() {
        return (decrementedHours == 0 && decrementedMinutes == 0 && decrementedSeconds == 0) && resetDecrements();
    }

    /**
     * Boolean to handle resetting the timer hours, minutes, and seconds
     * @return true
     */
    private boolean resetDecrements() {
        decrementedHours = hours;
        decrementedMinutes = minutes;
        decrementedSeconds = seconds;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

