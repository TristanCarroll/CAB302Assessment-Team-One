package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.timer.Timer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * CurrentTimerContainer class which extends Vbox in order to create custom JavaFX elements.
 * This class provides additional functionality as well as specific styling for timers.
 * This is distinct from the TimerContainer class as the required functionality and timing is different.
 */
public class CurrentTimerContainer extends VBox {
    public Timer timer;
    private Label timerLabel;
    public CurrentTimerContainer(Timer timer) {
        this.timer = timer;
        createContainer();
    }

    /**
     * Creates the container with customer styling
     */
    private void createContainer() {
        StackPane timerPane = new StackPane();
        timerPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        timerPane.setPrefSize(140, 50);

        timerLabel = new Label(this.timer.toString());
        timerLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        HBox hbox = new HBox(timerLabel);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(5);

        timerPane.getChildren().add(hbox);
        getChildren().add(timerPane);
    }

    /**
     * Updates the label's text
     *
     * @param newTime String representation of a time in the form hh:mm:ss
     */
    public void updateTimerText(String newTime) {
        timerLabel.setText(newTime);
    }

}
