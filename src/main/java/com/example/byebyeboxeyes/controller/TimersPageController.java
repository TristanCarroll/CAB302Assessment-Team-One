package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

import com.example.byebyeboxeyes.timer.Timer;
import com.example.byebyeboxeyes.model.TimerDAO;

public class TimersPageController implements Initializable {

    @FXML private TextField hoursField;
    @FXML private TextField minutesField;
    @FXML private TextField secondsField;
    @FXML private Button createTimerButton;
    @FXML private FlowPane recentTimersFlowPane;

    private Timeline timeline;
    private final TimerDAO timerDAO = TimerDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recentTimersFlowPane.getChildren().clear();
        displayTimersFromDatabase();
    }

    @FXML
    public void createNewTimer() {
        try {
            int hours = Integer.parseInt(hoursField.getText());
            int minutes = Integer.parseInt(minutesField.getText());
            int seconds = Integer.parseInt(secondsField.getText());

            // Basic validation
            if (hours < 0 || minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59) {
                // ... show an error message ...
                return;
            }

            Timer timer = new Timer(StateManager.getCurrentUser().getUserID(), hours, minutes, seconds);
            recentTimersFlowPane.getChildren().add(createTimerBox(timer));
            startTimer(timer);

            timerDAO.saveTimer(timer);

        } catch (NumberFormatException e) {
        }
    }

    private void startTimer(Timer timer) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timer.decrementTime();
            if (timer.isFinished()) {
                timeline.stop();
            }
            updateTimerLabel(timer);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void displayTimersFromDatabase() {
        List<Timer> allTimers = timerDAO.loadTimers(StateManager.getCurrentUser().getUserID());
        for (Timer timer : allTimers) {
            recentTimersFlowPane.getChildren().add(createTimerBox(timer));
            startTimer(timer);
        }
    }
    private StackPane createTimerBox(Timer timer) {
        StackPane timerPane = new StackPane();
        timerPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        timerPane.setPrefSize(100, 50);

        Label timerLabel = new Label(timer.toString());
        timerLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        timerPane.getChildren().add(timerLabel);

        return timerPane;
    }
    private void updateTimerLabel(Timer timer) {
        for (int i = 0; i < recentTimersFlowPane.getChildren().size(); i++) {
            if (recentTimersFlowPane.getChildren().get(i).getUserData() == timer) {
                Label timerLabel = (Label) ((FlowPane) recentTimersFlowPane.getChildren().get(i)).getChildren().get(0);
                timerLabel.setText(timer.toString());
                break;
            }
        }
    }
}