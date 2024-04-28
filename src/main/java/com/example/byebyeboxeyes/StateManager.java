package com.example.byebyeboxeyes;

import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StateManager {
    private static final IntegerProperty windowWidth = new SimpleIntegerProperty();
    private static final IntegerProperty windowHeight = new SimpleIntegerProperty();
    private static Stage currentStage;
    private static Scene currentScene;

    //TODO: Check if needed
    private static IntegerProperty sceneWidth;
    private static IntegerProperty sceneHeight;


    public static int getWindowWidth() {
        return windowWidth.get();
    }
    public static void setWindowWidth(int width) {
        windowWidth.set(width);
    }

    public static IntegerProperty windowWidthProperty() {
        return windowWidth;
    }

    public static int getWindowHeight() {
        return windowHeight.get();
    }

    public static void setWindowHeight(int height) {
        windowHeight.set(height);
    }

    public static IntegerProperty windowHeightProperty() {
        return windowHeight;
    }

    public static void setCurrentScene(Scene scene) {
        currentScene = scene;
    }
    public static Scene getCurrentScene() {
        return currentScene;
    }
    public static Stage getCurrentStage() {
        return currentStage;
    }
    public static void setCurrentStage(Stage stage) {
        currentStage = stage;
    }
}