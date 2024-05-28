package com.example.byebyeboxeyes.events;

import com.example.byebyeboxeyes.controller.TimerContainer;
import com.example.byebyeboxeyes.controller.TimerController;
import com.example.byebyeboxeyes.timer.Timer;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton service for handling events. Contains a list of event listeners and can call the
 * relevant methods when an event is triggered.
 */
public class EventService {
    private static final EventService instance = new EventService();
    private List<INavigationEventListener> loginEventListeners = new ArrayList<>();
    private List<INavigationEventListener> navigationEventListeners = new ArrayList<>();
    private List<ITimerEditListener> timerEditListeners = new ArrayList<>();
    private List<ITimerPlayListener> timerPlayListeners = new ArrayList<>();
    private List<ITimerDeleteListener> timerDeleteListeners = new ArrayList<>();
    private List<ITimerFavouriteListener> timerFavouriteListeners = new ArrayList<>();
    private List<IStopListener> stopListeners = new ArrayList<>();


    private EventService() {

    }
    public static EventService getInstance() {
        return instance;
    }

    //TODO:
    public void addLoginEventListener(INavigationEventListener listener) {
        loginEventListeners.add(listener);
    }
    public void addNavigationEventListener(INavigationEventListener listener) {
        navigationEventListeners.add(listener);
    }
    public void addEditListener(ITimerEditListener listener) {
        timerEditListeners.add(listener);
    }
    public void addPlayListener(ITimerPlayListener listener) {
        timerPlayListeners.add(listener);
    }
    public void addDeleteListener(ITimerDeleteListener listener) {
        timerDeleteListeners.add(listener);
    }
    public void addFavouriteListener(ITimerFavouriteListener listener) { timerFavouriteListeners.add(listener); }
    public void addStopListener(IStopListener listener) { stopListeners.add(listener); }
    public void notifyLoginSuccessful() {
        for (INavigationEventListener listener : loginEventListeners) {
            listener.onLoginSuccessful();
        }
    }
    public void notifyNavigationEvent(String fxmlPath) {
        for (INavigationEventListener listener: navigationEventListeners) {
            listener.onNavigationEvent(fxmlPath);
        }
    }
    public void notifyEditButtonClick(TimerContainer timerContainer) {
        // TODO:
        //      Listeners are added as part of the onEdit method call in TimersPageController.
        //      Iterating over a copy avoids a ConcurrentModifcationException, but there's likely a better way
        //      to handle refreshing the page...
        List<ITimerEditListener> timerEditListenersCopy = timerEditListeners;
        for (ITimerEditListener listener : timerEditListenersCopy) {
            listener.onEdit(timerContainer);
        }
    }
    public void notifyPlayButtonClick(Timer timer) {
        for (ITimerPlayListener listener : timerPlayListeners) {
            listener.onPlay(timer);
        }
    }
    public void notifyDeleteButtonClick(TimerContainer timerContainer) {
        for (ITimerDeleteListener listener : timerDeleteListeners) {
            listener.onDelete(timerContainer);
        }
    }
    public void notifyFavouriteButtonClick(TimerContainer timerContainer) {
        for (ITimerFavouriteListener listener : timerFavouriteListeners) {
            listener.onFavourite(timerContainer);
        }
    }
    public void notifyStopButtonClick(Timer timer) {
        for (IStopListener listener : stopListeners) {
            listener.onStop(timer);
        }
    }
}