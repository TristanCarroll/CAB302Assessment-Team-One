package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.events.EventService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class HomeController {
    //TODO: Add css styling to heading
    @FXML
    private Label eyeHealthHeading;
    @FXML
    private TextArea tipOftheDay;

    @FXML
    private ImageView settingsNavButton;
    @FXML
    private Button timersNavButton;
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
        if (event.getSource() == timersNavButton){
            EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/home-view.fxml");
        }
//        else if (event.getSource() == settingsNavButton){
//            EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/settings-view.fxml");
//        }
        else if (event.getSource() == statisticsNavButton){
            EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/statistics-view.fxml");
        }
        else if (event.getSource() == goalsNavButton){
            EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/goals-view.fxml");
        }
    }

    @FXML public void initialize() {
        //TODO: add rand string generator for diff tips
        tipOftheDay.appendText("Did you know eye health tip here?");
    }

    public void onImageClick(MouseEvent mouseEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/settings-view.fxml");
    }
}
