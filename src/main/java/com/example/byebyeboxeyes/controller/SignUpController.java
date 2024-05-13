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

import java.security.MessageDigest;
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

//   The following restrictions are imposed in the email address’ local part by using this regex:
//      It allows numeric values from 0 to 9.
//      Both uppercase and lowercase letters from a to z are allowed.
//      Allowed, are underscore “_”, hyphen “-“, and dot “.”
//      Dot isn’t allowed at the start and end of the local part.
//      Consecutive dots aren’t allowed.
//      For the local part, a maximum of 64 characters are allowed.
//   Restrictions for the domain part in this regular expression include:
//      It allows numeric values from 0 to 9.
//      We allow both uppercase and lowercase letters from a to z.
//      Hyphen “-” and dot “.” aren’t allowed at the start and end of the domain part.
//      No consecutive dots.
    public String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

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
                userDAO.addUser(
                        registerUserNameTextField.getText(),
                        registerEmailTextField.getText(),
                        passwordHash(registerPasswordTextField.getText()
                ));
                StateManager.setCurrentUser(userDAO.getUser(registerUserNameTextField.getText()));
                EventService.getInstance().notifyLoginSuccessful();
            } else {
                throw new Exception("Email is invalid");
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

    // Double check implementation is correct
    public static String passwordHash(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] rbt = md.digest();
            StringBuilder sb = new StringBuilder();
            for(byte b: rbt) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }catch (Exception e) {

        }
        return null;
    }

    public void oncancelButtonClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/landing-view.fxml");
    }
}

