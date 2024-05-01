package com.example.byebyeboxeyes.timer;

public class Timer {
    private int userID;
    private int hours;
    private int minutes;
    private int seconds;

    public Timer(int userID, int hours, int minutes, int seconds) {
        this.userID = userID;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
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

    public void setHours(int hours) {
        this.hours = hours;
    }
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    public void setUserID(int userID) { this.userID = userID; }


    public void decrementTime() {
        if (seconds > 0) {
            seconds--;
        } else if (minutes > 0) {
            minutes--;
            seconds = 59;
        } else if (hours > 0) {
            hours--;
            minutes = 59;
            seconds = 59;
        }
    }



    public boolean isFinished() {
        return hours == 0 && minutes == 0 && seconds == 0;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

