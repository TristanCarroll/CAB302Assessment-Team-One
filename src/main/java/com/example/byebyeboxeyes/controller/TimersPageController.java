package com.example.byebyeboxeyes.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

import com.example.byebyeboxeyes.timer.Timer;
import com.example.byebyeboxeyes.timer.TimerListCell;
import com.example.byebyeboxeyes.model.TimerDAO;

public class TimersPageController implements Initializable {

    @FXML private TextField hoursField;
    @FXML private TextField minutesField;
    @FXML private TextField secondsField;
    @FXML private Button createTimerButton;
    @FXML private ListView<Timer> recentTimersList;

    private Timeline timeline;
    private final TimerDAO timerDAO = TimerDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recentTimersList.setCellFactory(listView -> new TimerListCell());
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

            Timer timer = new Timer(hours, minutes, seconds);
            recentTimersList.getItems().add(timer);
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
            recentTimersList.refresh();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void displayTimersFromDatabase() {
        List<Timer> allTimers = timerDAO.loadTimers();
        recentTimersList.getItems().setAll(allTimers);
    }
}