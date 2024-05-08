package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.model.User;
import com.example.byebyeboxeyes.model.UserDAO;
import com.example.byebyeboxeyes.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.SQLException;

//Might need later
import java.util.Objects;

public class SignUpController {
    public Button cancelButton;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private TextField registerUserNameTextField;
    @FXML
    private TextField registerEmailTextField;
    @FXML
    private TextField registerPasswordTextField;

    public String emailPattern = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    //TODO:
    // ADD Register button LATER
    @FXML
    private Button registerButton;
    private final UserDAO userDAO;

    /**
     * Creates a listener for the window height property of the StateManager,
     * and adjusts the bottom padding accordingly.
     */
    public SignUpController() {
        userDAO = UserDAO.getInstance();
        setupListener();
    }
    private void setupListener() {
        StateManager.windowHeightProperty().addListener((obs, oldVal, newVal) -> {
            double paddingValue = newVal.doubleValue() * 0.60; // 60% of height
            mainStackPane.setPadding(new Insets(20, 20, paddingValue, 20));
        });
    }

    /**
     * Inserts a new user into the users table of the DB.
     * This will catch a PRIMARY KEY CONSTRAINT error if the username is already taken
     * and alert the user with a popup box
     */
    @FXML
    private void onRegisterButtonClick(){
        try{
            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(registerEmailTextField.getText());
            if (matcher.matches()) {
                userDAO.addUser(new User(
                        registerUserNameTextField.getText(),
                        registerEmailTextField.getText(),
                        registerPasswordTextField.getText()
                ));
                EventService.getInstance().notifyLoginSuccessful();
            } else {
                throw new Exception("Email is invalid.");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Registration Error");
            alert.setHeaderText("Registration Failed");
            if (e instanceof SQLException && ((SQLException) e).getErrorCode() == Constants.SQLITE_CONSTRAINT) {
                alert.setContentText("Username is already taken");
            } else {
                //TODO:
                //  Improve error message clarity.
                alert.setContentText(e.getLocalizedMessage());
            }
            alert.showAndWait();
        }
    }

    public void oncancelButtonClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/landing-view.fxml");
    }
}

