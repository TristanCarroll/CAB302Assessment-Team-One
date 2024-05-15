package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    //TODO: Add css styling to heading

    @FXML
    private Label userSignedIn;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userSignedIn();
    }
    @FXML
    public void onNavButtonClick(ActionEvent event) {
        //TODO:
        //  This should be fine as long as the nav buttons are only used for navigation.
        //  If we end up extending their functionality we may need to consider what's passed into the function
        //  to better inform the target which button has been clicked.
        if (event.getSource() == timersNavButton){
            EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/timer-view.fxml");
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
     * Get current user and Display their username next to the settings icon
     */
    @FXML
    public void userSignedIn() {
        userSignedIn.setText(StateManager.getCurrentUser().getUserName());
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
                "Limit reflections and glare!\nLight from windows or bright lamps can reflect on your computer screen and cause eye strain",
                "Dim your workspace!\nUse lower wattage bulbs and avoid bright overhead lights",
                "Adjust screen brightness!\nThe brightness of your screen should be the same as the level of brightness in the room around you",
                "Eye Health!\nRest your eyes at least 15 minutes after each 2 hours of computer or digital device use",
                "Eye Health!\nThe use of over-the-counter artificial-tear solutions can reduce the effects of dry eye in CVS"
        };
        Random randTips = new Random();
        eyeTipTxtArea.setText(tips[randTips.nextInt(tips.length)]);
    }

    public void onImageClick(MouseEvent mouseEvent) {
        EventService.getInstance().notifyNavigationEvent("/com/example/byebyeboxeyes/settings-view.fxml");
    }

}
