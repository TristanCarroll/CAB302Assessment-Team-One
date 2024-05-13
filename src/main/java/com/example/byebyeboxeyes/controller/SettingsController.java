package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import com.example.byebyeboxeyes.model.UserDAO;
import javafx.scene.control.TextField;
import static com.example.byebyeboxeyes.controller.SignUpController.passwordHash;


public class SettingsController {
    public Button logoutButton;
    private final UserDAO userDAO;
    @FXML
    public TextField newUserPassword;

    public SettingsController() {
        userDAO = UserDAO.getInstance();
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/landing-view.fxml");
    }

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


}
