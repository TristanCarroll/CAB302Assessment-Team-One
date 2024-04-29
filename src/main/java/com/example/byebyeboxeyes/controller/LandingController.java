package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LandingController {

    @FXML
    public void onLoginButtonClick(ActionEvent actionEvent) {
        //TODO: Implement login authentication with persistence database
        EventService.getInstance().notifyLoginSuccessful();
    }

    public void onHyperlinkClick(ActionEvent actionEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/signup-view.fxml");
    }
}