package com.example.byebyeboxeyes.model;

/**
 * SessionsDataCollator class to get the total time of the run timers
 * and current date of the logged-in session
 */
public class SessionsDataCollator {
    public static class TotalDailyData {
        private String date;
        private Double totalTime;

        /**
         * TotalDailyData to calculate the users overall time based on the current local date
         * @param date gets the current local date
         * @param totalTime gets the total usage time for the opened/run timers in the application
         * for the current local date
         */
        public TotalDailyData(String date, Double totalTime) {
            this.date = date;
            this.totalTime = totalTime;
        }

        public Double getTotalTime() {
            return totalTime;
        }

        public String getDate() {
            return date;
        }
    }
}
