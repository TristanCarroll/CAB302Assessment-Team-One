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
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

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
    @FXML
    private VBox weeklyTrackerContainer;
    @FXML
    private VBox monthlyTrackerContainer;
    @FXML
    private HBox weeklyTracker;
    @FXML
    private GridPane monthlyTracker;
    private boolean isWeeklyView = true;
    @FXML
    private ToggleButton viewToggleButton;
    @FXML
    private Button previousButton;
    private LocalDate currentWeekStart = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1); // Start with current week
    private YearMonth currentMonth = YearMonth.now(); // Start with current month
    @FXML
    private Label weekLabel;

    @FXML
    private Label monthLabel;

    @FXML
    private Button nextButton;

    private static final String USAGE_PER_DAY = "Total Usage per Day";
    private static final String DAYS_USED = "Days Used";

    private static final List<String> options = Arrays.asList(
            USAGE_PER_DAY,
            DAYS_USED
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
                    weeklyTrackerContainer.setVisible(false);
                    monthlyTrackerContainer.setVisible(false);
                    viewToggleButton.setVisible(false);
                    previousButton.setVisible(false);
                    nextButton.setVisible(false);
                    weekLabel.setVisible(false); // Hide week label
                    monthLabel.setVisible(false); // Hide month label
                    break;
                case DAYS_USED:
                    if (!chartContainer.getChildren().isEmpty()) {
                        chartContainer.getChildren().remove(0);
                    }
                    currentChoice = DAYS_USED;
                    setupMonthlyTracker();
                    setupWeeklyTracker();
                    weeklyTrackerContainer.setVisible(true);
                    monthlyTrackerContainer.setVisible(true);
                    viewToggleButton.setVisible(true);  // Show toggle button
                    previousButton.setVisible(true);     // Show previous button
                    nextButton.setVisible(true);
                    weekLabel.setVisible(true);
                    break;
            }
        });
        chartChoiceBox.setValue(currentChoice);
        weeklyTracker.setVisible(isWeeklyView);
        monthlyTracker.setVisible(!isWeeklyView);
        viewToggleButton.setText(isWeeklyView ? "Monthly View" : "Weekly View");

        updateTrackerLabels();
        updateButtonStates();
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
    private void setupWeeklyTracker() {
        monthlyTracker.getChildren().clear();
        weeklyTracker.getChildren().clear();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = currentWeekStart;
        List<LocalDate> sessionDates = SessionsDAO.getInstance().getUniqueSessionDates(StateManager.getCurrentUser().getUserID());

        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            Circle circle = new Circle(5);
            circle.setFill(sessionDates.contains(date) ? Color.GREEN : Color.LIGHTGRAY);
            Label label = new Label(date.getDayOfWeek().name().substring(0, 1));
            VBox dayContainer = new VBox(circle, label);
            dayContainer.setPadding(new Insets(5));
            weeklyTracker.getChildren().add(dayContainer);
        }
    }

    private void setupMonthlyTracker() {
        weeklyTracker.getChildren().clear();
        monthlyTracker.getChildren().clear();

        LocalDate startOfMonth = currentMonth.atDay(1); // Start from the 1st of the selected month
        int daysInMonth = currentMonth.lengthOfMonth(); // Get the number of days in the selected month
        List<LocalDate> sessionDates = SessionsDAO.getInstance().getUniqueSessionDates(StateManager.getCurrentUser().getUserID());

        for (int i = 0; i < daysInMonth; i++) {
            LocalDate date = startOfMonth.plusDays(i);
            Circle circle = new Circle(5);
            circle.setFill(sessionDates.contains(date) ? Color.GREEN : Color.LIGHTGRAY);
            Label label = new Label(String.valueOf(date.getDayOfMonth()));
            VBox dayContainer = new VBox(circle, label);
            dayContainer.setPadding(new Insets(5));
            monthlyTracker.add(dayContainer, (i % 7), (i / 7));
        }
    }
    private void updateTrackerLabels() {
        weekLabel.setText("Week of " + currentWeekStart.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
        monthLabel.setText(currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
    }

    private void updateButtonStates() {
        previousButton.setDisable(false);
        nextButton.setDisable(false);
    }

    @FXML
    public void previousMonth(ActionEvent event) {
        currentMonth = currentMonth.minusMonths(1);
        setupMonthlyTracker();
        updateTrackerLabels();
        updateButtonStates();
    }
    @FXML
    public void nextMonth(ActionEvent event) {
        currentMonth = currentMonth.plusMonths(1);
        setupMonthlyTracker();
        updateTrackerLabels();
        updateButtonStates();
    }
    @FXML
    public void previousWeek(ActionEvent event) {
        currentWeekStart = currentWeekStart.minusWeeks(1);
        // Ensure currentMonth is updated to match the new week
        currentMonth = YearMonth.from(currentWeekStart);
        setupWeeklyTracker();
        updateTrackerLabels();
        updateButtonStates();
    }

    @FXML
    public void nextWeek(ActionEvent event) {
        currentWeekStart = currentWeekStart.plusWeeks(1);
        // Ensure currentMonth is updated to match the new week
        currentMonth = YearMonth.from(currentWeekStart);
        setupWeeklyTracker();
        updateTrackerLabels();
        updateButtonStates();
    }

    @FXML
    public void toggleView(ActionEvent event) {
        isWeeklyView = !isWeeklyView;


        if (isWeeklyView) {
            currentWeekStart = currentMonth.atDay(1);
        }

        weeklyTracker.setVisible(isWeeklyView);
        monthlyTracker.setVisible(!isWeeklyView);

        viewToggleButton.setText(isWeeklyView ? "Monthly View" : "Weekly View");

        // Update label visibility
        weekLabel.setVisible(isWeeklyView);
        monthLabel.setVisible(!isWeeklyView);

        // Update button action based on view
        previousButton.setOnAction(isWeeklyView ? this::previousWeek : this::previousMonth);
        nextButton.setOnAction(isWeeklyView ? this::nextWeek : this::nextMonth);

        // Refresh the tracker for the new view
        if (isWeeklyView) {
            weekLabel.setManaged(true);
            setupWeeklyTracker();
        } else {
            weekLabel.setManaged(false);
            setupMonthlyTracker();
        }

        updateTrackerLabels();
        updateButtonStates();
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
