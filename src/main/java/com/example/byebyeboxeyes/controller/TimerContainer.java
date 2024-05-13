package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.timer.Timer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

// TODO:
//  Interface?
public class TimerContainer extends VBox {
    public Timer timer;
    private Label timerLabel;
    private int isFavourite = 0; //track if it is a favourite timer
    private Button favouriteButton;
    private TimerController controller;

    public TimerController getController() {
        return controller;
    }

    public TimerContainer(Timer timer, TimerController controller) {
        this.timer = timer;
        this.controller = controller; // Store the controller reference
        createContainer();
    }

    public TimerContainer(Timer timer) {
        this.timer = timer;
        createContainer();
    }

    private void createContainer() {
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

//    private void favouriteTimer() {
//        if (onFavouriteListener != null) {
//            isFavourite = (isFavourite == 1) ? 0 : 1;
//            System.out.println("Favourite button clicked. New isFavourite state: " + isFavourite);
//
//            // Update button appearance immediately
//            Platform.runLater(this::updateFavouriteButtonAppearance);
//
//            // Notify the listener (this can still be delayed)
//            Platform.runLater(() -> onFavouriteListener.onFavourite(this));
//        }
//    }
    public void setFavourite(int isFavourite) {
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
    public void updateTimerText(String newTime) {
        timerLabel.setText(newTime);
    }
    private void editTimer() {
        EventService.getInstance().notifyEditButtonClick(this);
    }
    private void playTimer() {
        EventService.getInstance().notifyPlayButtonClick(this.timer);
    }
    private void deleteTimer() {
        EventService.getInstance().notifyDeleteButtonClick(this);
    }
    private void favouriteTimer() {
        EventService.getInstance().notifyFavouriteButtonClick(this);
    }

}
