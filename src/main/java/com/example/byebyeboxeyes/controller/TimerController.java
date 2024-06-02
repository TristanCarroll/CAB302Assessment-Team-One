package com.example.byebyeboxeyes.controller;

import com.example.byebyeboxeyes.StateManager;
import com.example.byebyeboxeyes.events.EventService;
import com.example.byebyeboxeyes.events.ITimerPlayListener;
import com.example.byebyeboxeyes.model.SessionsDAO;
import com.example.byebyeboxeyes.timer.Timer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.awt.*;

/**
 * Controller which handles the currently running timer. Contains functionality for handling timers being
 * started/stopped from the timers page controller.
 */
public class TimerController implements Initializable, ITimerPlayListener {
    String audioFile = "src/main/resources/com/example/byebyeboxeyes/audio/darude.mp3";
    Media sound = new Media(new File(audioFile).toURI().toString());
    @FXML
    public AnchorPane currentTimer;
    private Timeline timeline;
    private int sessionID;
    private CurrentTimerContainer currentTimerContainer;

    /**
     * Initialize the class and add it to the list of event listeners for timer play and stop events,
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventService.getInstance().addPlayListener(this);
        EventService.getInstance().addStopListener(this::onStop);
    }

    /**
     * Injects the timer into the anchor pane as a TimerContainer.
     * Minimizes window to tray and alerts user.
     * Creates a timeline with an anonymous function to handle decrementing the timer.
     * Ends the session when the timer hits 00:00:00
     * @param timer The timer the stop event is fired from
     */
    public void onPlay(Timer timer) {
        if (!currentTimer.getChildren().isEmpty()) {
            return;
        }

        sessionID = StartSession(timer);
        CurrentTimerContainer timerContainer = new CurrentTimerContainer(timer);
        currentTimer.getChildren().add(timerContainer);

        var trayIconController = new TrayIconController();
        TrayIcon trayIcon = trayIconController.getTrayIcon();

        HWND hWnd = User32.INSTANCE.FindWindow(null, "Bye Bye Box Eyes");
        User32.INSTANCE.ShowWindow(hWnd, WinUser.SW_MINIMIZE);

        trayIcon.displayMessage("Timer Started", "Bye Bye Box Eyes", TrayIcon.MessageType.INFO);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timerContainer.updateTimerText(timerContainer.timer.decrementTime());
                if (timerContainer.timer.isFinished()) {
                    // play sound here
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                    timeline.stop();

                    SessionsDAO.getInstance().endSession(sessionID, System.currentTimeMillis()/1000);
                    currentTimer.getChildren().remove(timerContainer);

                    trayIcon.displayMessage("Timer Finished", "Bye Bye Box Eyes", TrayIcon.MessageType.INFO);
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * This method is called to update the session table if a timer is stopped early.
     *
     * @param timer The timer that is being stopped
     */
    public void onStop(Timer timer) {
        if (timeline != null) {
            timeline.stop();  // Stop the timeline
            currentTimer.getChildren().clear(); // Remove the timer from the UI
            SessionsDAO.getInstance().endSession(sessionID, System.currentTimeMillis()/1000);
        }
    }

    /**
     * Creates a record in the session table with the unix start time that the countdown begins.
     *
     * @param timer The timer being started
     * @return The primary key/session id of the newly created record
     */
    private int StartSession(Timer timer) {
        return SessionsDAO.getInstance().startSession(
                StateManager.getCurrentUser().getUserID(),
                timer.getTimerID(),
                //TODO: Helper method for this it's gonna be used a lot
                System.currentTimeMillis()/1000
        );
    }
}
