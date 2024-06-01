package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import com.example.byebyeboxeyes.model.UserDAO;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.byebyeboxeyes.controller.SignUpController.passwordHash;


public class SettingsController implements Initializable {
    public Button logoutButton;
    private final UserDAO userDAO;
    @FXML
    public PasswordField newUserPassword;
    @FXML
    public PasswordField currentUserPassword;
    @FXML
    private Button DeleteAccountButton;
    @FXML
    private Label DeleteAccount;
    @FXML
    private Label SignedIn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SignedIn();
    }

    public SettingsController() {
        userDAO = UserDAO.getInstance();
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/landing-view.fxml");
    }

    public void onResetButtonClick(ActionEvent actionEvent) {
        User currentUser = StateManager.getCurrentUser();
        String userEmail = currentUser.getEmail();

        if (Objects.equals(currentUser.getPassword(), passwordHash(currentUserPassword.getText()))) {
            userDAO.updateUserPassword(passwordHash(newUserPassword.getText()), userEmail);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reset Password");
            alert.setHeaderText("Your password was changed.");
            alert.setContentText("Your password has been successfully updated.");
            alert.showAndWait();
        } else {
            // Incorrect password
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Reset Password Failed");
            alert.setHeaderText("Incorrect Password");
            alert.setContentText("The password you entered is incorrect.");
            alert.showAndWait();
        }


    }

    @FXML
    public void SignedIn() {
        SignedIn.setText(StateManager.getCurrentUser().getUserName());
    }

    //TODO: ADD THE LOGIC DELETEACCOUNT IS ADDED ABOVE
    public void onDeleteButtonClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyDeleteAccountButtonClick();
    }
}
