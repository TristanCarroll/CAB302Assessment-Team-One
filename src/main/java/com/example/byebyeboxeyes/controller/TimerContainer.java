package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.timer.Timer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TimerContainer extends VBox {
    public Timer timer;
    private OnEditListener onEditListener;
    private OnPlayListener onPlayListener;
    private OnDeleteListener onDeleteListener;
    public TimerContainer(Timer timer) {
        this.timer = timer;
        createTimerContainer();
    }

    private void createTimerContainer() {
        StackPane timerPane = new StackPane();
        timerPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        timerPane.setPrefSize(200, 50);

        Label timerLabel = new Label(this.timer.toString());
        timerLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> editTimer());

        Button playButton = new Button("Play");
        playButton.setOnAction(event -> playTimer());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteTimer());

        VBox vbox = new VBox(editButton, playButton, deleteButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);

        HBox hbox = new HBox(timerLabel, vbox);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(5);

        timerPane.getChildren().add(hbox);
        getChildren().add(timerPane);
    }

    private void editTimer() {
        if (onEditListener != null) {
            onEditListener.onEdit(this);
        }
    }
    private void playTimer() {
        if (onPlayListener != null) {
            onPlayListener.onPlay(this);
        }
    }
    private void deleteTimer() {
        if (onDeleteListener != null) {
            onDeleteListener.onDelete(this);
        }
    }
    public void setOnEditListener(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }
    public void setOnPlayListener(OnPlayListener onPlayListener) {
        this.onPlayListener = onPlayListener;
    }
    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }
    public interface OnEditListener {
        void onEdit(TimerContainer timerContainer);
    }
    public interface OnPlayListener {
        void onPlay(TimerContainer timerContainer);
    }
    public interface OnDeleteListener {
        void onDelete(TimerContainer timerContainer);
    }
}
