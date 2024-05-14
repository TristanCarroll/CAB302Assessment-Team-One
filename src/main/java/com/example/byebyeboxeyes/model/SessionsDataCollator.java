package com.example.byebyeboxeyes.model;

public class SessionsDataCollator {
    public static class TotalDailyData {
        private String date;
        private Double totalTime;

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
