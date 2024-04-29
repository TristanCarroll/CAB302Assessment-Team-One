package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.events.EventService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController {
    @FXML
    private Button homeNavButton;
    @FXML
    private Button settingsNavButton;
    @FXML
    private Button statisticsNavButton;
    @FXML
    private Button goalsNavButton;
    @FXML
    public void onNavButtonClick(ActionEvent event) {
        //TODO:
        //  This should be fine as long as the nav buttons are only used for navigation.
        //  If we end up extending their functionality we may need to consider what's passed into the function
        //  to better inform the target which button has been clicked.
        if (event.getSource() == homeNavButton){
            EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/home-view.fxml");
        }
        else if (event.getSource() == settingsNavButton){
            EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/settings-view.fxml");
        }
        else if (event.getSource() == statisticsNavButton){
            EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/statistics-view.fxml");
        }
        else if (event.getSource() == goalsNavButton){
            EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/goals-view.fxml");
        }
    }
}
