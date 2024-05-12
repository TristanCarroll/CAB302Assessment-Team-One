package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.events.INavigationEventListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
public class NavigationController implements INavigationEventListener {
    // private BorderPane mainBorderPane;
    private static final NavigationController instance = new NavigationController();
    private NavigationController() {
        EventService.getInstance().addLoginEventListener(this);
        EventService.getInstance().addNavigationEventListener(this);
    }
    public static NavigationController getInstance() {
        return instance;
    }

    @Override
    public void onLoginSuccessful() {
        try {
            // TODO:
            //  Just replace the root of the current scene.
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/byebyeboxeyes/home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),
                    StateManager.getCurrentScene().getWidth(), StateManager.getCurrentScene().getHeight());
            Stage stage = StateManager.getCurrentStage();
            stage.setScene(scene);
        } catch (IOException e) {
            // TODO:
            //  Better error handling
            e.printStackTrace();
        }
    }

    @Override
    public void onNavigationEvent(String fxmlPath) {
        try {
            // TODO:
            //  The if else statement fixes the bug but I think this could probably be handled better
            //TODO:
            // Add navigation fxml, load it once, always set the center to home.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (fxmlPath.contains("home") || fxmlPath.contains("signup") || fxmlPath.contains("landing") ) {
                StateManager.getCurrentScene().setRoot(fxmlLoader.load());
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
}
