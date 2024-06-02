package com.example.byebyeboxeyes;

import com.example.byebyeboxeyes.controller.LandingController;
import com.example.byebyeboxeyes.controller.NavigationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the application
 */
public class HelloApplication extends Application {
    private final static String title = "Bye Bye Box Eyes";

    /**
     * Sets the stage and scene
     * Loads the landing-view.fxml and stylesheet.css
     * @param stage set the stage
     */
    @Override
    public void start(Stage stage) throws IOException {

        String stylesheet = HelloApplication.class.getResource("stylesheet.css").toExternalForm();
        setupWindowDimensionListeners(stage);
        setupSceneListener(stage);
        StateManager.setCurrentStage(stage);
        NavigationController.getInstance();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("landing-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
        stage.setTitle(title);
        stage.setScene(scene);
        scene.getStylesheets().add(stylesheet);
        stage.show();
    }

    /**
     * Launch the program
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Set the stage dimensions of the Windows width and height
     */
    private void setupWindowDimensionListeners(Stage stage) {
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            StateManager.setWindowWidth(newVal.intValue());
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            StateManager.setWindowHeight(newVal.intValue());
        });
    }

    /**
     * Event handler for setting the scene
     */
    private void setupSceneListener(Stage stage) {
        stage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            StateManager.setCurrentScene(newScene);
        });
    }
}