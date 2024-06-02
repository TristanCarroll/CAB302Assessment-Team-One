package com.example.byebyeboxeyes;

import com.example.byebyeboxeyes.model.User;
import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * State Manager class to direct the process of saving and restoring views between requests
 */
public class StateManager {
    private static final IntegerProperty windowWidth = new SimpleIntegerProperty();
    private static final IntegerProperty windowHeight = new SimpleIntegerProperty();
    private static Stage currentStage;
    private static Scene currentScene;

    public static User getCurrentUser() { return currentUser; }

    public static void setCurrentUser(User currentUser) { StateManager.currentUser = currentUser; }

    private static User currentUser;

    private static IntegerProperty sceneWidth;
    private static IntegerProperty sceneHeight;


    /**
     * Window width getter
     * @return width of application window
     */
    public static int getWindowWidth() {
        return windowWidth.get();
    }

    /**
     * Window width setter
     * @param width set width of application window
     */
    public static void setWindowWidth(int width) {
        windowWidth.set(width);
    }

    /**
     * int of window width
     * @return window width
     */
    public static IntegerProperty windowWidthProperty() {
        return windowWidth;
    }

    /**
     * window height getter
     * @return get height of window
     */
    public static int getWindowHeight() {
        return windowHeight.get();
    }

    /**
     * window height setter
     * @param height int height of application window
     */
    public static void setWindowHeight(int height) {
        windowHeight.set(height);
    }

    /**
     * Int of window height
     * @return int value of window height
     */
    public static IntegerProperty windowHeightProperty() {
        return windowHeight;
    }

    /**
     * setter for setting the current scene
     * @param scene enter the current scene
     */
    public static void setCurrentScene(Scene scene) {
        currentScene = scene;
    }

    /**
     * getter for the current scene
     * @return the current scene
     */
    public static Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * getter for current stage
     * @return the current stage
     */
    public static Stage getCurrentStage() {
        return currentStage;
    }

    /**
     * setter for current stage
     * @param stage enter the current stage
     */
    public static void setCurrentStage(Stage stage) {
        currentStage = stage;
    }
}