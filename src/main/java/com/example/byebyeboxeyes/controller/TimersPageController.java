package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

import com.example.byebyeboxeyes.timer.Timer;
import com.example.byebyeboxeyes.model.TimerDAO;

public class TimersPageController implements Initializable {

    @FXML private TextField hoursField;
    @FXML private TextField minutesField;
    @FXML private TextField secondsField;
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

            Timer timer = new Timer(StateManager.getCurrentUser().getUserID(), hours, minutes, seconds);

            recentTimersFlowPane.getChildren().clear();
            recentTimersFlowPane.getChildren().add(createTimerContainer(timer));

            timerDAO.saveTimer(timer);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void displayTimersFromDatabase() {
        List<Timer> allTimers = timerDAO.loadTimers(StateManager.getCurrentUser().getUserID());
        int index = recentTimersFlowPane.getChildren().isEmpty() ?
                0 : recentTimersFlowPane.getChildren().size() - 1;
        for (Timer timer : allTimers) {
            recentTimersFlowPane.getChildren().add(index, createTimerContainer(timer));
        }
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> createNewTimer());
        recentTimersFlowPane.getChildren().add(addButton);
    }

    private StackPane createTimerContainer(Timer timer) {
        StackPane timerPane = new StackPane();
        timerPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        timerPane.setPrefSize(200, 50);

        Label timerLabel = new Label(timer.toString());
        timerLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Button editButton = new Button("Edit");
        Button playButton = new Button("Play");
        Button deleteButton = new Button("Delete");

        deleteButton.setOnAction(Event -> {
            recentTimersFlowPane.
        });

        VBox vbox = new VBox(editButton, playButton, deleteButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);

        HBox hbox = new HBox(timerLabel, vbox);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(5);

        timerPane.getChildren().add(hbox);

        return timerPane;
    }
}