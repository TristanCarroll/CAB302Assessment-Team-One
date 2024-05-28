package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.events.ITimerDeleteListener;
import com.example.byebyeboxeyes.events.ITimerEditListener;
import com.example.byebyeboxeyes.events.ITimerFavouriteListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import com.example.byebyeboxeyes.timer.Timer;
import com.example.byebyeboxeyes.model.TimerDAO;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;

import com.example.byebyeboxeyes.timer.Timer;
import com.example.byebyeboxeyes.model.TimerDAO;

public class TimersPageController implements
        Initializable,
        ITimerEditListener,
        ITimerDeleteListener,
        ITimerFavouriteListener
{

    @FXML
    private TextField hoursField;
    @FXML
    private TextField minutesField;
    @FXML
    private TextField secondsField;
    @FXML
    private FlowPane recentTimersFlowPane;
    @FXML
    private FlowPane favouriteTimersFlowPane;
    private ArrayList<Timer> allTimers;


    private final TimerDAO timerDAO = TimerDAO.getInstance();
    private Map<Integer, TimerContainer> timerContainers = new HashMap<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recentTimersFlowPane.getChildren().clear();
        favouriteTimersFlowPane.getChildren().clear();
        displayTimersFromDatabase();
        EventService.getInstance().addEditListener(this);
        EventService.getInstance().addDeleteListener(this);
        EventService.getInstance().addFavouriteListener(this);

        timerDAO.loadTimers(StateManager.getCurrentUser().getUserID(), timers -> {
            Platform.runLater(() -> {
                allTimers = timers;
                for (Timer timer : allTimers) {
                    TimerContainer container = createTimerContainer(timer);

                    // Set the correct favourite status after the container is created
                    container.setFavourite(timerDAO.isTimerFavourite(timer.getTimerID())); // Update based on database value
                    // Determine the controller to add the container to
                    TimerController targetController = timerContainers.containsKey(timer.getTimerID()) ?
                            timerContainers.get(timer.getTimerID()).getController() : null;

                    // Add the container to the correct FlowPane
                    if (container.isFavourite() == 1) {
                        addToFavourites(container);
                    } else {
                        addToRecent(container);
                    }
                }
            });
        });
    }
    public void addToFavourites(TimerContainer container) {
            favouriteTimersFlowPane.getChildren().add(container);
    }

    public void addToRecent(TimerContainer container) {
            recentTimersFlowPane.getChildren().add(container);
    }

    @FXML
    public void createNewTimer() {
        try {
            int hours = getValidatedIntFromTextField(hoursField, 0, 23);
            int minutes = getValidatedIntFromTextField(minutesField, 0, 59);
            int seconds = getValidatedIntFromTextField(secondsField, 0, 59);


            int timerID = timerDAO.saveTimer(StateManager.getCurrentUser().getUserID(), hours, minutes, seconds, 0);
            Timer timer = new Timer(timerID, StateManager.getCurrentUser().getUserID(), hours, minutes, seconds, 0);
            TimerContainer timerContainer = createTimerContainer(timer);
            timerContainers.put(timerID, timerContainer);

            // Determine the target FlowPane (recent or favorite)
            FlowPane targetPane = (timer.getIsFavourite() == 1) ? favouriteTimersFlowPane : recentTimersFlowPane;

            // Add the TimerContainer to the correct FlowPane
            targetPane.getChildren().add(0, timerContainer);

            clearInputFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for hours, minutes, and seconds.");
        }
    }

    private void displayTimersFromDatabase() {
        for (Map.Entry<Integer, TimerContainer> entry : timerContainers.entrySet()) {
            if (entry.getValue().isFavourite() == 1) { // Compare to 1 for favorite
                favouriteTimersFlowPane.getChildren().add(entry.getValue());
            } else {
                recentTimersFlowPane.getChildren().add(entry.getValue());
            }
//            timerContainers.put(timer.getTimerID(), timerContainer);
        }
//        Button addButton = new Button("Add");
//       addButton.setOnAction(event -> createNewTimer());
//        recentTimersFlowPane.getChildren().add(addButton);
    }
    private TimerContainer createTimerContainer(Timer timer) {
        TimerContainer timerContainer = new TimerContainer(timer);
        timerContainer.setFavourite(timerDAO.isTimerFavourite(timer.getTimerID()));


        return timerContainer;
    }
    private void clearInputFields() {
        hoursField.clear();
        minutesField.clear();
        secondsField.clear();
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
    public void onFavourite(TimerContainer timerContainer) {
        int timerId = timerContainer.timer.getTimerID();
        int isFavourite = timerDAO.isTimerFavourite(timerId);

        System.out.println("Toggling favourite status for timer " + timerId +
                " (current status: " + isFavourite + ")"); // Log before update

        // Toggle favourite status (0 to 1, or 1 to 0)
        int newFavouriteValue = (isFavourite == 1) ? 0 : 1;
        timerDAO.setTimerFavourite(timerId, newFavouriteValue);
        timerContainer.setFavourite(newFavouriteValue);  // Update the TimerContainer's state

        // Update the UI by moving the TimerContainer to the correct FlowPane
        if (newFavouriteValue == 1) {  // If it's now a favourite, move it to favourites
            favouriteTimersFlowPane.getChildren().add(timerContainer);
            recentTimersFlowPane.getChildren().remove(timerContainer);
        } else {                        // No longer a favourite
            recentTimersFlowPane.getChildren().add(timerContainer);
            favouriteTimersFlowPane.getChildren().remove(timerContainer);
        }
    }

    @Override
    public void onEdit(TimerContainer timerContainer){
        //TODO: Make this a method
        int hours;
        hours = hoursField.getText().isEmpty() ?
                0 : Integer.parseInt(hoursField.getText());
        int minutes;
        minutes = minutesField.getText().isEmpty() ?
                0 : Integer.parseInt(minutesField.getText());
        int seconds;
        seconds = secondsField.getText().isEmpty() ?
                0 : Integer.parseInt(secondsField.getText());

        timerContainer.timer.setHours(hours);
        timerContainer.timer.setMinutes(minutes);
        timerContainer.timer.setSeconds(seconds);

        timerDAO.updateTimer(timerContainer.timer.getTimerID(), hours, minutes, seconds, timerContainer.timer.getIsFavourite());
        timerContainer.updateTimerText(timerContainer.timer.toString());
    }
    @Override
    public void onDelete(TimerContainer timerContainer) {
        recentTimersFlowPane.getChildren().remove(timerContainer);
        favouriteTimersFlowPane.getChildren().remove(timerContainer); // Also remove from favouriteTimersFlowPane

        try {
            TimerDAO.getInstance().deleteTimer(timerContainer.timer.getTimerID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}