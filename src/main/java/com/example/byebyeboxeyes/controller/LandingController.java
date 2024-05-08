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
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Objects;

public class LandingController {
    @FXML
    private Button closeButton;
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
        //setupListener();
    }

    /**
     * Creates a listener for the window height property of the StateManager,
     * and adjusts the bottom padding accordingly.
     */
//    private void setupListener() {
//        StateManager.windowHeightProperty().addListener((obs, oldVal, newVal) -> {
//            double paddingValue = newVal.doubleValue() * 0.60; // 60% of height
//            mainStackPane.setPadding(new Insets(20, 20, paddingValue, 20));
//        });
//    }

    /**
     * Tries to find the username in the DB and check that the stored password matches what's entered in the field.
     * If username doesn't exist or the password is incorrect the user is alerted via popup,
     * otherwise the LoginSuccessful event is triggered.
     */
    @FXML
    public void onLoginButtonClick(ActionEvent actionEvent) {
        String test = userNameTextField.getText();
        User user = userDAO.getUser(userNameTextField.getText());
        if (user != null) {
            if (Objects.equals(user.getPassword(), passwordTextField.getText())) {
                EventService.getInstance().notifyLoginSuccessful();
            } else {
                // Incorrect password
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Incorrect Password");
                alert.setContentText("The password you entered is incorrect.");
                alert.showAndWait();
            }
        } else {
            // User not found
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("User Not Found");
            alert.setContentText("No user found with the specified username.");
            alert.showAndWait();
        }
    }

    /** Sends user to Registration page when they click the hyperlink **/
    public void onHyperlinkClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/signup-view.fxml");
    }

    @FXML
    private void onCloseButtonClick() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}