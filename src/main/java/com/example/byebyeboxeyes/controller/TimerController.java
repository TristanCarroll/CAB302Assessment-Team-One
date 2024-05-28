package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.WinApi;
import com.example.byebyeboxeyes.WinApi.Shell32;
import com.example.byebyeboxeyes.WinApi.NOTIFYICONDATA;
import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.events.ITimerPlayListener;
import com.example.byebyeboxeyes.model.SessionsDAO;
import com.example.byebyeboxeyes.timer.Timer;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
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
    private int sessionID;
    private CurrentTimerContainer currentTimerContainer;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventService.getInstance().addPlayListener(this);
        EventService.getInstance().addStopListener(this::onStop);
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

        HWND hWnd = User32.INSTANCE.FindWindow(null, "Bye Bye Box Eyes");
        User32.INSTANCE.ShowWindow(hWnd, WinUser.SW_MINIMIZE);

        NOTIFYICONDATA nid = new NOTIFYICONDATA();
        nid.hWnd = hWnd;
        nid.uFlags = NOTIFYICONDATA.NIF_INFO;
        nid.szInfo = "Bye Bye Box Eyes".toCharArray();
        nid.szInfoTitle = "Timer Started".toCharArray();
        nid.dwInfoFlags = NOTIFYICONDATA.NIIF_INFO;
        nid.guidItem = GUID.newGuid();

        Shell32.Shell_NotifyIcon(WinApi.Shell32.NIM_ADD, nid);

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
                    currentTimer.getChildren().remove(timerContainer);

                    nid.szInfoTitle = "Timer Finished".toCharArray();
                    Shell32.Shell_NotifyIcon(Shell32.NIM_MODIFY, nid);
                    Shell32.Shell_NotifyIcon(WinApi.Shell32.NIM_DELETE, nid);
                    //User32.INSTANCE.ShowWindow(hWnd, WinUser.SW_MAXIMIZE);
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public void onStop(Timer timer) {
        if (timeline != null) {
            timeline.stop();  // Stop the timeline
            currentTimer.getChildren().clear(); // Remove the timer from the UI
            SessionsDAO.getInstance().endSession(sessionID, System.currentTimeMillis()/1000);
        }
    }
}
