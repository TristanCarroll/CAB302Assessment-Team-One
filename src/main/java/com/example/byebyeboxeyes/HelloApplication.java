package com.example.byebyeboxeyes;

import com.example.byebyeboxeyes.controller.NavigationController;
import com.example.byebyeboxeyes.controller.TrayIconController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
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
        stage.setOnCloseRequest(this::onWindowClose);
        stage.show();

        var trayIconController = new TrayIconController();
        Image image = ImageIO.read(new File("src/main/resources/com/example/byebyeboxeyes/images/Logo-16.png"));
        TrayIcon trayIcon = new TrayIcon(image);
        if (!trayIconController.setTrayIcon(trayIcon)) {
            System.err.println("Failed to set tray icon. Exiting application.");
            Platform.exit();
        }
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

    private void onWindowClose(WindowEvent event) {
        var icon = new TrayIconController();
        if (icon.trayIconExists()) {
            icon.deleteTrayIcon();
        }
    }
}