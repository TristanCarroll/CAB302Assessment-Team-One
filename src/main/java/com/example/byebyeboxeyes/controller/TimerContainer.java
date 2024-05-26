package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.timer.Timer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// TODO:
//  Interface?
public class TimerContainer extends VBox {
    public Timer timer;
    private Label timerLabel;
    private int isFavourite = 0; //track if it is a favourite timer
    private Button favouriteButton;
    private TimerController controller;
    int iconSize = 12;
    // Load Icons
    Image editIcon = new Image(getClass().getResourceAsStream("/com/example/byebyeboxeyes/images/editing.png"), iconSize, iconSize, true, true);
    Image playIcon = new Image(getClass().getResourceAsStream("/com/example/byebyeboxeyes/images/play-button-arrowhead.png"), iconSize, iconSize, true, true);
    Image deleteIcon = new Image(getClass().getResourceAsStream("/com/example/byebyeboxeyes/images/delete.png"), iconSize, iconSize, true, true);
    Image favIcon = new Image(getClass().getResourceAsStream("/com/example/byebyeboxeyes/images/unfilled_star.png"), iconSize, iconSize, true, true);
    Image unfavIcon = new Image(getClass().getResourceAsStream("/com/example/byebyeboxeyes/images/star.png"), iconSize, iconSize, true, true);

    public TimerController getController() {
        return controller;
    }

    public TimerContainer(Timer timer) {
        this.timer = timer;
        createContainer();
    }

    private void createContainer() {
        // --- Styling ---
        String mainBackgroundColor = "#C8E6C9"; // Light green background
        String borderColor = "#4CAF50"; // Green border
        String buttonBackgroundColor = "#81C784"; // Medium green button
        String buttonHoverColor = "#66BB6A"; // Slightly darker green on hover
        String labelTextColor = "#388E3C"; // Dark green text

        // --- Layout ---
        StackPane timerPane = new StackPane();
        timerPane.setBackground(new Background(new BackgroundFill(Color.web(mainBackgroundColor), CornerRadii.EMPTY, Insets.EMPTY)));
        timerPane.setStyle("-fx-border-color: " + borderColor + "; -fx-border-radius: 5;");
        timerPane.setPadding(new Insets(10));
        timerPane.setPrefSize(150, 100); // Increased height for better visual balance

        timerLabel = new Label(this.timer.toString());
        timerLabel.setStyle("-fx-font-size: 20; -fx-text-fill: " + labelTextColor + ";");

        // --- Timer Title ---
        Label timerTitle = new Label("Timer " + this.timer.getTimerID()); // Display Timer ID
        timerTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: " + labelTextColor + ";"); // Make it bold and match the color

        timerLabel = new Label(this.timer.toString());

        // --- Icon Buttons ---

        Button playButton = new Button();
        playButton.setGraphic(new ImageView(playIcon));
        playButton.setStyle("-fx-background-color: " + buttonBackgroundColor + "; -fx-background-radius: 5;");
        playButton.setOnMouseEntered(e -> playButton.setStyle("-fx-background-color: " + buttonHoverColor + ";"));
        playButton.setOnMouseExited(e -> playButton.setStyle("-fx-background-color: " + buttonBackgroundColor + ";"));
        playButton.setOnAction(event -> playTimer());


        Button editButton = new Button();
        editButton.setGraphic(new ImageView(editIcon));
        editButton.setStyle("-fx-background-color: " + buttonBackgroundColor + "; -fx-background-radius: 5;");
        editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-background-color: " + buttonHoverColor + ";"));
        editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color: " + buttonBackgroundColor + ";"));
        editButton.setOnAction(event -> editTimer());


        Button deleteButton = new Button();
        deleteButton.setGraphic(new ImageView(deleteIcon));
        deleteButton.setStyle("-fx-background-color: " + buttonBackgroundColor + "; -fx-background-radius: 5;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: " + buttonHoverColor + ";"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: " + buttonBackgroundColor + ";"));
        deleteButton.setOnAction(event -> deleteTimer());

        favouriteButton = new Button();
        favouriteButton.setGraphic(new ImageView(favIcon));
        favouriteButton.setStyle("-fx-background-color: " + buttonBackgroundColor + "; -fx-background-radius: 5;");
        favouriteButton.setOnMouseEntered(e -> favouriteButton.setStyle("-fx-background-color: " + buttonHoverColor + ";"));
        favouriteButton.setOnMouseExited(e -> favouriteButton.setStyle("-fx-background-color: " + buttonBackgroundColor + ";"));
        favouriteButton.setOnAction(event -> {

            favouriteTimer();
            // Update Icon on click
            if (isFavourite == 1) {
                favouriteButton.setGraphic(new ImageView(unfavIcon));
            } else {
                favouriteButton.setGraphic(new ImageView(favIcon));
            }
        });


        // --- Arrange Buttons Horizontally ---
        HBox buttonBox = new HBox( playButton,editButton, deleteButton, favouriteButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(5); // Adjust spacing as needed

        // --- Arrange Title, Buttons, and Label ---
        VBox vbox = new VBox(timerTitle, buttonBox, timerLabel); // Add timerTitle to VBox
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        timerPane.getChildren().add(vbox);
        getChildren().add(timerPane);

        // Add a subtle shadow to the timer pane
        timerPane.setEffect(new DropShadow(5, 2, 2, Color.web("#888888")));
    }



    public void setFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
        Platform.runLater(this::updateFavouriteButtonAppearance);
    }
    public void updateFavouriteButtonAppearance() {

        if (isFavourite == 1) {
            favouriteButton.setGraphic(new ImageView(unfavIcon)); // Set to unfavorite icon
        } else {
            favouriteButton.setGraphic(new ImageView(favIcon));   // Set to favorite icon
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

        if (timer.getHours() != 0 || timer.getMinutes() != 0 || timer.getSeconds() != 0)
        {
            EventService.getInstance().notifyPlayButtonClick(this.timer);
        }
    }
    private void deleteTimer() {
        EventService.getInstance().notifyDeleteButtonClick(this);
    }
    private void favouriteTimer() {
        EventService.getInstance().notifyFavouriteButtonClick(this);
    }

}
