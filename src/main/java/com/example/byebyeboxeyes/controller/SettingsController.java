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
import java.util.ResourceBundle;

import static com.example.byebyeboxeyes.controller.SignUpController.passwordHash;

/**
 * SettingsController class to handle navigation of user logging out,
 * resetting their password in the settings view, and deleting their account
 */
public class SettingsController implements Initializable {
    public Button logoutButton;
    private final UserDAO userDAO;
    @FXML
    public PasswordField newUserPassword;
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

    /**
     * Navigation to handle the logout button click.
     * When user clicks logout navigate back to the landing-view.fxml
     */
    public void onLogoutButtonClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/landing-view.fxml");
    }

    /**
     * Reset the current loggedin user password. Updates the new password to the user profile
     */
    public void onResetButtonClick(ActionEvent actionEvent) {
        User currentUser = StateManager.getCurrentUser();
        String userEmail = currentUser.getEmail();
        System.out.println(userEmail);
        userDAO.updateUserPassword(passwordHash(newUserPassword.getText()), userEmail);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reset Password");
        alert.setHeaderText("Your password was changed.");
        alert.setContentText("Your password has been successfully updated.");
        alert.showAndWait();
    }

    /**
     * SignedIn method to handle the current loggedin userName. Useful to display the userName in text fields
     */
    @FXML
    public void SignedIn() {
        SignedIn.setText(StateManager.getCurrentUser().getUserName());
    }

    /**
     * Deletes the current loggein users account on delete button click
     */
    public void onDeleteButtonClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyDeleteAccountButtonClick();
    }
}
