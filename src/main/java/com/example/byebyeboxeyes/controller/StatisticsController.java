package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.model.SessionsDAO;
import com.example.byebyeboxeyes.model.SessionsDataCollator.TotalDailyData;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {
    @FXML
    VBox chartContainer;
    @FXML
    ChoiceBox<String> chartChoiceBox;
    @FXML
    Label totalTimeToday;
    @FXML
    Label totalTimeOverall;
    @FXML
    Label numberSessionsToday;
    @FXML
    Label numberSessionsOverall;

    private static final String USAGE_PER_DAY = "Total Usage per Day";
    private static final List<String> options = Arrays.asList(
            USAGE_PER_DAY,
            "blah"
    );
    private static String currentChoice = USAGE_PER_DAY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        totalTimeToday.setText(secondsToHhMmSs(getTotalUsageToday()));
        totalTimeOverall.setText(secondsToHhMmSs(getTotalUsageOverall()));
        numberSessionsToday.setText(getNumberOfSessionsToday().toString());
        numberSessionsOverall.setText(getNumberOfSessionsOverall().toString());
        chartChoiceBox.getItems().addAll(options);

        chartChoiceBox.setOnAction(event -> {
            String selectedOption = chartChoiceBox.getValue();

            switch (selectedOption) {
                case USAGE_PER_DAY:
                    if (!chartContainer.getChildren().isEmpty()) {
                        chartContainer.getChildren().remove(0);
                    }
                    setupTotalPerDayChart();
                    currentChoice = USAGE_PER_DAY;
                    break;
                case "blah":
                    if (!chartContainer.getChildren().isEmpty()) {
                        chartContainer.getChildren().remove(0);
                    }
                    currentChoice = "blah";
                    break;
            }
        });
        chartChoiceBox.setValue(currentChoice);

    }
    private void setupTotalPerDayChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total Session Time (Hours)");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Total Session Time per Day");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Session Time");

        List<TotalDailyData> sessionsData =
                SessionsDAO.
                getInstance().
                        getTotalSessionTimePerDay(StateManager.getCurrentUser().getUserID());
        for (TotalDailyData data : sessionsData) {
            series.getData().add(new XYChart.Data<>(data.getDate(), secondsToHours(data.getTotalTime())));
        }

        lineChart.getData().add(series);

        chartContainer.getChildren().add(lineChart);
    }
    private static <T extends Number> double secondsToHours(T number) {
        return number.doubleValue()/60/60;
    }
    private static <T extends Number> String secondsToHhMmSs(T seconds) {
        // Convert the input to an integer value
        int totalSeconds = seconds.intValue();

        // Calculate hours, minutes, and seconds
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int secondsLeft = totalSeconds % 60;

        // Format the time components to string with zero padding for minutes and seconds
        return String.format("%d:%02d:%02d", hours, minutes, secondsLeft);
    }
    private Number getTotalUsageToday() {
        return SessionsDAO.getInstance().getTotalUsageToday(
                StateManager.getCurrentUser().getUserID()).
                get(0).getTotalTime();
    }
    private Number getTotalUsageOverall() {
        return SessionsDAO.getInstance().getTotalUsageOverall(
                StateManager.getCurrentUser().getUserID()).
                get(0).getTotalTime();
    }
    private Number getNumberOfSessionsToday() {
        return SessionsDAO.getInstance().getNumberOfSessionsToday(
                        StateManager.getCurrentUser().getUserID());
    }
    private Number getNumberOfSessionsOverall() {
        return SessionsDAO.getInstance().getNumberOfSessionsOverall(
                StateManager.getCurrentUser().getUserID());
    }
//    public void insertMockData() {
//        long currentTime = System.currentTimeMillis()/1000;
//        long oneDaySeconds = 24 * 60 * 60;
//        int userID = 1;
//        int timerID = 100;
//
//        for (int i = 0; i < 30; i++) {  // Insert data for 30 days
//            long unixStartTime = currentTime - (i * oneDaySeconds); // Decrement by one day for each iteration
//            long sessionDuration = (long) (Math.random() * (2 * 60 * 60)); // Random session duration up to 2 hours
//            long unixEndTime = unixStartTime + sessionDuration;
//
//            SessionsDAO.getInstance().insertMockDataForCharts(userID, timerID, unixStartTime, unixEndTime);
//        }
//    }
}
