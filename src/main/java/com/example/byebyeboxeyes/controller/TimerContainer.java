package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.timer.Timer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

public class TimerContainer extends VBox {
    public Timer timer;
    private Label timerLabel;
    private OnEditListener onEditListener;
    private OnPlayListener onPlayListener;
    private OnDeleteListener onDeleteListener;
    private OnFavouriteListener onFavouriteListener; // Declare the listener
    private int isFavourite = 0; //track if it is a favourite timer
    private Button favouriteButton;
    private TimerController controller;

    public TimerController getController() {
        return controller;
    }

    public TimerContainer(Timer timer, TimerController controller) {
        this.timer = timer;
        this.controller = controller; // Store the controller reference
        createTimerContainer();
    }

    public TimerContainer(Timer timer) {
        this.timer = timer;
        createTimerContainer();

    }


    private void createTimerContainer() {
        StackPane timerPane = new StackPane();
        timerPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        timerPane.setPrefSize(200, 50);

        timerLabel = new Label(this.timer.toString());
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

        favouriteButton = new Button("Favourite");
        favouriteButton.setOnAction(event -> favouriteTimer());
        // Add to the VBox
        vbox.getChildren().add(favouriteButton);
    }

    private void favouriteTimer() {
        if (onFavouriteListener != null) {
            // Toggle favourite state immediately for UI responsiveness
            isFavourite = (isFavourite == 1) ? 0 : 1; // Toggle between 0 and 1

            System.out.println("Favourite button clicked. New isFavourite state: " + isFavourite);

            // Notify the listener on the JavaFX Application Thread for UI updates
            Platform.runLater(() -> onFavouriteListener.onFavourite(this));
        }
    }
    public void setFavourite(int isFavourite){
        this.isFavourite = isFavourite;

        Platform.runLater(this::updateFavouriteButtonAppearance);
    }
    public void updateFavouriteButtonAppearance() {
        if (isFavourite == 1) {
            favouriteButton.setText("Unfavourite");
        } else {
            favouriteButton.setText("Favourite");
        }
    }

    public int isFavourite() {
        return isFavourite;
    }
    private void updateTimerText(String newTime) {
        timerLabel.setText(newTime);
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
    public interface OnFavouriteListener {
        void onFavourite(TimerContainer timerContainer);
    }

    public void setOnFavouriteListener(OnFavouriteListener onFavouriteListener) {
        this.onFavouriteListener = onFavouriteListener;
    }
}

