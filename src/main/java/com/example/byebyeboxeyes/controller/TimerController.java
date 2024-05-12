package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.timer.Timer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TimerController implements Initializable {

    @FXML
    public AnchorPane currentTimer;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timer timer = new Timer(1, 2, 3, 4, 5);
        currentTimer.getChildren().add(new TimerContainer(timer));
    }
}
