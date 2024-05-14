package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.events.ITimerPlayListener;
import com.example.byebyeboxeyes.model.SessionsDAO;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class TimerController implements Initializable, ITimerPlayListener {
    String audioFile = "src/main/resources/com/example/byebyeboxeyes/audio/darude.mp3";
    Media sound = new Media(new File(audioFile).toURI().toString());
    @FXML
    public AnchorPane currentTimer;
    private Timeline timeline;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventService.getInstance().addPlayListener(this);
    }

    public void onPlay(Timer timer) {
        if (!currentTimer.getChildren().isEmpty()) {
            return;
        }
        int sessionID = SessionsDAO.getInstance().startSession(
                StateManager.getCurrentUser().getUserID(),
                timer.getTimerID(),
                //TODO: Helper method for this it's gonna be used a lot
                System.currentTimeMillis()/1000
        );
        CurrentTimerContainer timerContainer = new CurrentTimerContainer(timer);
        currentTimer.getChildren().add(timerContainer);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timerContainer.timer.decrementTime();
                timerContainer.updateTimerText(timerContainer.timer.toString());
                if (timerContainer.timer.isFinished()) {
                    // play sound here
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                    timeline.stop();
                    SessionsDAO.getInstance().endSession(sessionID, System.currentTimeMillis()/1000);
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
