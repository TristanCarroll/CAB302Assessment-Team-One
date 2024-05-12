package com.example.byebyeboxeyes.events;

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
}