package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.events.EventService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.Random;

public class HomeController {
    //TODO: Add css styling to heading
    @FXML
    private Button eyeTipButton;
    @FXML
    private TextArea eyeTipTxtArea;

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

    /**
     * Button to display random eye health tips when the user selects the See Tips! button
     */
    @FXML
    protected void eyeTipButton() {

        String []tips = {
                "Adjust your computer!\nPosition your computer screen 20 to 28 inches from your eyes",
                "Adjust your posture!\nPoor posture can increase your risk of eye strain. Sit up straight, keep your shoulders relaxed, and use a chair with the right height",
                "Take regular breaks!\nFollow the 20-20-20 rule.\nLook at something 20 feet away for 20 seconds every 20 minutes",
                "Limit reflections and glare!\nLight from windows or bright lamps can reflect on your computer screen and cause eye strain. Use lower wattage bulbs and avoid bright overhead lights",
                "Adjust screen brightness!\nThe brightness of your screen should be the same as the level of brightness in the room around you"
        };
        Random randTips = new Random();
        eyeTipTxtArea.setText(tips[randTips.nextInt(tips.length)]);
    }

    public void onImageClick(MouseEvent mouseEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/settings-view.fxml");
    }
}
