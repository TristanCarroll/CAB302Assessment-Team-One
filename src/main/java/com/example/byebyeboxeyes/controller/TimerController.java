package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.model.TimerDAO;
import com.example.byebyeboxeyes.timer.Timer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class TimerController implements javafx.fxml.Initializable, com.example.byebyeboxeyes.controller.TimerContainer.OnPlayListener, com.example.byebyeboxeyes.controller.TimerContainer.OnFavouriteListener, com.example.byebyeboxeyes.controller.TimerContainer.OnEditListener, com.example.byebyeboxeyes.controller.TimerContainer.OnDeleteListener {

    @FXML
    public AnchorPane currentTimer;
    @FXML
    public VBox timerList; // This should be in the AnchorPane in the FXML
    @FXML
    public FlowPane favouriteTimersFlowPane;
    @FXML
    private TextField hoursField;
    @FXML
    private TextField minutesField;
    @FXML
    private TextField secondsField;


    private final TimerDAO timerDAO = TimerDAO.getInstance();
    private TimersPageController timersPageController;
    private Map<Integer, TimerContainer> timerContainers = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timerDAO.loadTimers(com.example.byebyeboxeyes.StateManager.getCurrentUser().getUserID(), timers -> {
            Platform.runLater(() -> {
                if(timerList != null){
                    timerList.getChildren().clear();
                    favouriteTimersFlowPane.getChildren().clear();
                    for (Timer timer : timers) {
                        TimerContainer container = createTimerContainer(timer);
                        timerContainers.put(timer.getTimerID(), container);
                        container.setOnPlayListener(this);
                        container.setOnFavouriteListener(this);

                        if (timer.isFavourite() == 1) {
                            favouriteTimersFlowPane.getChildren().add(container);
                        } else {
                            timerList.getChildren().add(container);
                        }
                    }
                }
            });
        });
    }

    private void addToRecent(TimerContainer container) {
        timerList.getChildren().add(container);
    }
    private TimerContainer createTimerContainer(Timer timer) {
        TimerContainer timerContainer = new TimerContainer(timer);
        timerContainer.setOnEditListener(this);
        timerContainer.setOnPlayListener(this);
        timerContainer.setOnDeleteListener(this);
        timerContainer.setFavourite(timer.isFavourite());
        timerContainer.setOnFavouriteListener(this);
        return timerContainer;
    }

    private void clearInputFields() {
        hoursField.clear();
        minutesField.clear();
        secondsField.clear();
    }

    @FXML
    public void createNewTimer() {
        try {
            int hours = getValidatedIntFromTextField(hoursField, 0, 23);
            int minutes = getValidatedIntFromTextField(minutesField, 0, 59);
            int seconds = getValidatedIntFromTextField(secondsField, 0, 59);

            int timerID = timerDAO.saveTimer(2, hours, minutes, seconds, 0);
            Timer timer = new Timer(timerID, 2, hours, minutes, seconds, 0);
            TimerContainer timerContainer = createTimerContainer(timer);
            timerContainers.put(timerID, timerContainer);

            // Conditional logic to determine placement:
            if (timer.isFavourite() == 1) {
                timersPageController.addToFavourites(timerContainer, this);
            } else {
                timersPageController.addToRecent(timerContainer, this);
            }

            clearInputFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for hours, minutes, and seconds.");
        }
    }
    private int getValidatedIntFromTextField(TextField textField, int minValue, int maxValue) {
        int value = textField.getText().isEmpty() ? 0 : Integer.parseInt(textField.getText());
        if (value < minValue || value > maxValue) {
            throw new NumberFormatException("Value out of range");
        }
        return value;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void onPlay(TimerContainer timerContainer) {
        // ... (your timer start/stop logic here) ...
    }

    @Override
    public void onEdit(TimerContainer timerContainer) {
        // ... (your timer edit logic here) ...
    }

    @Override
    public void onDelete(TimerContainer timerContainer) {
        timerList.getChildren().remove(timerContainer);
        favouriteTimersFlowPane.getChildren().remove(timerContainer);
        try {
            TimerDAO.getInstance().deleteTimer(timerContainer.timer.getTimerID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFavourite(TimerContainer timerContainer) {
        int timerId = timerContainer.timer.getTimerID();
        int currentFavouriteValue = timerDAO.isTimerFavourite(timerId);

        // Toggle favourite status (0 to 1, or 1 to 0)
        int newFavouriteValue = (currentFavouriteValue == 1) ? 0 : 1;
        timerDAO.setTimerFavourite(timerId, newFavouriteValue);

        // Update the UI by moving the TimerContainer to the correct FlowPane
        if (newFavouriteValue == 1) {  // Now a favourite
            favouriteTimersFlowPane.getChildren().add(timerContainer);
            timerList.getChildren().remove(timerContainer);
        } else {                        // No longer a favourite
            timerList.getChildren().add(timerContainer);
            favouriteTimersFlowPane.getChildren().remove(timerContainer);
        }

        // Update the TimerContainer's state
        timerContainer.setFavourite(newFavouriteValue);
    }
}
