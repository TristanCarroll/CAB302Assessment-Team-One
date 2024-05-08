package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;

import com.example.byebyeboxeyes.timer.Timer;
import com.example.byebyeboxeyes.model.TimerDAO;

public class TimersPageController implements Initializable, TimerContainer.OnDeleteListener {

    @FXML
    private TextField hoursField;
    @FXML
    private TextField minutesField;
    @FXML
    private TextField secondsField;
    @FXML
    private FlowPane recentTimersFlowPane;

    private final TimerDAO timerDAO = TimerDAO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recentTimersFlowPane.getChildren().clear();
        displayTimersFromDatabase();
    }

    @FXML
    public void createNewTimer() {
        try {

            int hours;
            hours = hoursField.getText().isEmpty() ?
                     0 : Integer.parseInt(hoursField.getText());
            int minutes;
            minutes = minutesField.getText().isEmpty() ?
                    0 : Integer.parseInt(minutesField.getText());
            int seconds;
            seconds = secondsField.getText().isEmpty() ?
                    0 : Integer.parseInt(secondsField.getText());

            // Basic validation
            if (hours < 0 || minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59) {
                // ... show an error message ...
                return;
            }

            int timerID = timerDAO.saveTimer(StateManager.getCurrentUser().getUserID(), hours, minutes, seconds);
            Timer timer = new Timer(timerID, StateManager.getCurrentUser().getUserID(), hours, minutes, seconds);
            recentTimersFlowPane.getChildren().clear();
            recentTimersFlowPane.getChildren().add(createTimerContainer(timer));
            displayTimersFromDatabase();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void displayTimersFromDatabase() {
        ArrayList<Timer> allTimers = timerDAO.loadTimers(StateManager.getCurrentUser().getUserID());
        int index = recentTimersFlowPane.getChildren().isEmpty() ?
                0 : recentTimersFlowPane.getChildren().size() - 1;
        for (Timer timer : allTimers) {
            recentTimersFlowPane.getChildren().add(index, createTimerContainer(timer));
        }
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> createNewTimer());
        recentTimersFlowPane.getChildren().add(addButton);
    }
    private Node createTimerContainer(Timer timer) {
        TimerContainer timerContainer = new TimerContainer(timer);
        timerContainer.setOnDeleteListener(this);
        return timerContainer;
    }
    @Override
    public void onDelete(TimerContainer timerContainer) {
        recentTimersFlowPane.getChildren().remove(timerContainer);
        try {
            TimerDAO.getInstance().deleteTimer(timerContainer.timer.getTimerID());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}