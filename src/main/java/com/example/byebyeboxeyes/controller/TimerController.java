package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.events.ITimerPlayListener;
import com.example.byebyeboxeyes.timer.Timer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class TimerController implements Initializable, ITimerPlayListener {

    @FXML
    public AnchorPane currentTimer;
    private Timeline timeline;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventService.getInstance().addPlayListener(this);
    }

    public void onPlay(Timer timer) {
        TimerContainer timerContainer = new TimerContainer(timer);
        currentTimer.getChildren().add(timerContainer);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timerContainer.timer.decrementTime();
                timerContainer.updateTimerText(timerContainer.timer.toString());
                if (timerContainer.timer.isFinished()) {
                    timeline.stop();
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
