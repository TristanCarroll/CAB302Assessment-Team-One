package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.model.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import com.example.byebyeboxeyes.model.UserDAO;


public class SettingsController {
    public Button logoutButton;
    private final UserDAO userDAO;

    public SettingsController() {
        userDAO = UserDAO.getInstance();
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/landing-view.fxml");
    }

    public void onResetButtonClick(ActionEvent actionEvent) {
        User currentUser = StateManager.getCurrentUser();
        String userEmail = currentUser.getEmail();
        // userDAO.updateUserPassword();
    }


}
