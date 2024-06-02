package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.timer.Timer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Current timer container class
 */
public class CurrentTimerContainer extends VBox {
    public Timer timer;
    private Label timerLabel;

    /**
     * create the current timer container
     * @param timer select current timer
     */
    public CurrentTimerContainer(Timer timer) {
        this.timer = timer;
        createContainer();
    }

    /**
     * create container specifications
     * styling, size, position
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
     * Updates the timer text. Auto increments from timer 1
     * @param newTime sets the label time: Timerx
     */
    public void updateTimerText(String newTime) {
        timerLabel.setText(newTime);
    }

}
