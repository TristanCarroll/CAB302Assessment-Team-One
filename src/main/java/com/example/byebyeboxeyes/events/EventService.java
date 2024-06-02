package com.example.byebyeboxeyes.events;

import com.example.byebyeboxeyes.controller.TimerContainer;
import com.example.byebyeboxeyes.timer.Timer;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton service for handling events. Contains a list of event listeners and can call the
 * relevant methods when an event is triggered.
 */
public class EventService {
    private static final EventService instance = new EventService();
    private final List<INavigationEventListener> loginEventListeners = new ArrayList<>();
    private final List<INavigationEventListener> navigationEventListeners = new ArrayList<>();
    private final List<ITimerEditListener> timerEditListeners = new ArrayList<>();
    private final List<ITimerPlayListener> timerPlayListeners = new ArrayList<>();
    private final List<ITimerDeleteListener> timerDeleteListeners = new ArrayList<>();
    private final List<ITimerFavouriteListener> timerFavouriteListeners = new ArrayList<>();
    private final List<IDeleteAccountEventListener> deleteAccountListeners = new ArrayList<>();
    private final List<ITimerStopListener> stopListeners = new ArrayList<>();

    private EventService() {

    }
    public static EventService getInstance() {
        return instance;
    }

    /**
     * Adds a class that implements INavigationEventListener to a list of listeners for login events
     *
     * @param listener A class that implements INavigationEventListener
     */
    public void addLoginEventListener(INavigationEventListener listener) {
        loginEventListeners.add(listener);
    }

    /**
     * Adds a class that implements INavigationEventListener to a list of listeners for navigation events
     *
     * @param listener A class that implements INavigationEventListener
     */
    public void addNavigationEventListener(INavigationEventListener listener) {
        navigationEventListeners.add(listener);
    }

    /**
     * Adds a class that implements ITimerEditlistener to a list of listeners for timer edit events
     *
     * @param listener A class that implements ITimerEditListener
     */
    public void addEditListener(ITimerEditListener listener) {
        timerEditListeners.add(listener);
    }

    /**
     * Adds a class that implements ITimerPlayListener to a list of listeners for timer play events
     *
     * @param listener A class that implements ITimerPlayListener
     */
    public void addPlayListener(ITimerPlayListener listener) {
        timerPlayListeners.add(listener);
    }

    /**
     * Adds a class that implements ITimerDeleteListener to a list of listeners for timer delete events
     *
     * @param listener A class that implements ITimerDeleteListener
     */
    public void addDeleteListener(ITimerDeleteListener listener) {
        timerDeleteListeners.add(listener);
    }

    /**
     * Adds a class that implements ITimerStopListener to a list of listeners for timer stop events
     *
     * @param listener A class that implements ITimerStopListener
     */
    public void addStopListener(ITimerStopListener listener) {
        stopListeners.add(listener);
    }

    /**
     * Adds a class that implements IDeleteAccountEventListener to a list of listeners for delete account events
     *
     * @param listener A class that implements IDeleteAccountEventListener
     */
    public void addDeleteAccountEventListener(IDeleteAccountEventListener listener) {
        deleteAccountListeners.add(listener);
    }

    /**
     * Adds a class that implements ITimerFavouriteListener to a list of listeners for timer favourite events
     * @param listener A class that implements ITimerFavouriteListener
     */
    public void addFavouriteListener(ITimerFavouriteListener listener) { timerFavouriteListeners.add(listener); }

    /**
     * Notifies listeners in the loginEventlisteners list that a login event has been fired
     */
    public void notifyLoginSuccessful() {
        for (INavigationEventListener listener : loginEventListeners) {
            listener.onLoginSuccessful();
        }
    }

    /**
     * Notifies listeners in the navigationEventListeners list that a navigation event has been fired
     *
     * @param fxmlPath The fxml path of the navigation "to" view
     */
    public void notifyNavigationEvent(String fxmlPath) {
        for (INavigationEventListener listener: navigationEventListeners) {
            listener.onNavigationEvent(fxmlPath);
        }
    }

    /**
     * Notifies listeners in the timerEditListeners list that a timer edit event has been fired
     *
     * @param timerContainer The TimerContainer the event was fired from
     */
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

    /**
     * Notifies listeners in the timerPlayListeners list that a timer play event has been fired
     *
     * @param timer The Timer the event was fired from
     */
    public void notifyPlayButtonClick(Timer timer) {
        for (ITimerPlayListener listener : timerPlayListeners) {
            listener.onPlay(timer);
        }
    }

    /**
     * Notifies listeners in the timerDeleteListeners list that a timer delete event has been fired
     *
     * @param timerContainer The TimerContainer the event was fired from
     */
    public void notifyDeleteButtonClick(TimerContainer timerContainer) {
        for (ITimerDeleteListener listener : timerDeleteListeners) {
            listener.onDelete(timerContainer);
        }
    }

    /**
     * Notifies listeners in the timerFavouriteListeners list that a timer favourite event has been fired
     *
     * @param timerContainer The TimerContainer the event was fired from
     */
    public void notifyFavouriteButtonClick(TimerContainer timerContainer) {
        for (ITimerFavouriteListener listener : timerFavouriteListeners) {
            listener.onFavourite(timerContainer);
        }
    }

    /**
     * Notifies listeners in the deleteAccountListeners list that a delete account event has been fired
     */
    public void notifyDeleteAccountButtonClick() {
        for (IDeleteAccountEventListener listener : deleteAccountListeners) {
            listener.onDeleteAccount();
        }
    }

    /**
     * Notifies listeners in the stopListeners list that a timer stop event has been fired
     *
     * @param timer The timer that is associated with the event
     */
    public void notifyStopButtonClick(Timer timer) {
        for (ITimerStopListener listener : stopListeners) {
            listener.onStop(timer);
        }
    }
}