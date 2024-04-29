package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.model.User;
import com.example.byebyeboxeyes.model.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.sql.SQLException;
import java.util.Objects;

public class LandingController {

    @FXML
    private Hyperlink hyperlink;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;
    private final UserDAO userDAO;
    public LandingController() {
        userDAO = UserDAO.getInstance();
        setupListener();
    }

    /**
     * Creates a listener for the window height property of the StateManager,
     * and adjusts the bottom padding accordingly.
     */
    private void setupListener() {
        StateManager.windowHeightProperty().addListener((obs, oldVal, newVal) -> {
            double paddingValue = newVal.doubleValue() * 0.60; // 60% of height
            mainStackPane.setPadding(new Insets(20, 20, paddingValue, 20));
        });
    }

    @FXML
    public void onLoginButtonClick(ActionEvent actionEvent) {
        //TODO: Implement login authentication with persistence database
        EventService.getInstance().notifyLoginSuccessful();
    }

    public void onHyperlinkClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/signup-view.fxml");
    }
}