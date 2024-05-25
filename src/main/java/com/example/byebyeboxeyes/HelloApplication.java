package com.example.byebyeboxeyes;

import com.example.byebyeboxeyes.controller.LandingController;
import com.example.byebyeboxeyes.controller.NavigationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private final static String title = "Bye Bye Box Eyes";

    // TODO: Create and implement responsive design elements
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


    public static void main(String[] args) {
        launch();
    }

    private void setupWindowDimensionListeners(Stage stage) {
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            StateManager.setWindowWidth(newVal.intValue());
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            StateManager.setWindowHeight(newVal.intValue());
        });
    }

    private void setupSceneListener(Stage stage) {
        stage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            StateManager.setCurrentScene(newScene);
        });
    }
}