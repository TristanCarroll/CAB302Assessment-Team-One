package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.HelloApplication;
import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.events.IDeleteAccountEventListener;
import com.example.byebyeboxeyes.events.INavigationEventListener;
import com.example.byebyeboxeyes.model.SessionsDAO;
import com.example.byebyeboxeyes.model.TimerDAO;
import com.example.byebyeboxeyes.model.UserDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * NavigationController class to handle navigation through the application
 * Handles user log in, the navigation events on pane button clicks, and delete account logic
 */
public class NavigationController implements INavigationEventListener, IDeleteAccountEventListener {
    // private BorderPane mainBorderPane;
    private static final NavigationController instance = new NavigationController();
    private NavigationController() {
        EventService.getInstance().addLoginEventListener(this);
        EventService.getInstance().addNavigationEventListener(this);
        EventService.getInstance().addDeleteAccountEventListener(this);
    }
    public static NavigationController getInstance() {
        return instance;
    }

    /**
     * Method to handle a users successful login.
     * If successful it will load the home-view.fxml and set the stylesheet.css
     * catches unsuccessful exceptions
     */
    @Override
    public void onLoginSuccessful() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/byebyeboxeyes/home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),
                    StateManager.getCurrentScene().getWidth(), StateManager.getCurrentScene().getHeight());
            Stage stage = StateManager.getCurrentStage();
            String stylesheet = HelloApplication.class.getResource("stylesheet.css").toExternalForm();
            scene.getStylesheets().add(stylesheet);
            stage.setScene(scene);
        } catch (IOException e) {
            // TODO:
            //  Better error handling
            e.printStackTrace();
        }
    }

    /**
     * Method to handle the navigation of the home.fxml, signup.fxml, landing.fxml, reset.fxml pages
     * Loads the stylesheet.css
     * @param fxmlPath load the fxmlPath if it contains the " " fxml file.
     */
    @Override
    public void onNavigationEvent(String fxmlPath) {
        try {
            // TODO:
            //  The if else statement fixes the bug but I think this could probably be handled better
            //TODO:
            // Add navigation fxml, load it once, always set the center to home.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (fxmlPath.contains("home") || fxmlPath.contains("signup") || fxmlPath.contains("landing") || fxmlPath.contains(("reset")) ) {
                StateManager.getCurrentScene().setRoot(fxmlLoader.load());
                String stylesheet = HelloApplication.class.getResource("stylesheet.css").toExternalForm();
                StateManager.getCurrentScene().getStylesheets().add(stylesheet);
            }
            else {
                ((BorderPane)StateManager.getCurrentScene().getRoot()).setCenter(fxmlLoader.load());

            }
        } catch (Exception e) {
            //TODO:
            //  Better error handling
            e.printStackTrace();
        }
    }

    /**
     * Method to delete the current loggedin users account. Sets alerts to the user to make certain of the choice to delete their account.
     * Delete account is irreversible. If delete is successful - load the landing-view.fxml
     * Otherwise if the user cancels, notify account delete cancel to the alert section
     */
    @Override
    public void onDeleteAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action is irreversible.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteAccount();
            EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/landing-view.fxml");
        } else {
            System.out.println("Account deletion cancelled.");
        }
    }

    /**
     * Get the loggedin userID and delete their UserDAO, TimerDAO, and SessionsDAO from the DB
     */
    private void deleteAccount() {
        UserDAO.getInstance().deleteUser(StateManager.getCurrentUser().getUserID());
        TimerDAO.getInstance().deleteTimersForUser(StateManager.getCurrentUser().getUserID());
        SessionsDAO.getInstance().deleteSessionsForUser(StateManager.getCurrentUser().getUserID());
    }
}
