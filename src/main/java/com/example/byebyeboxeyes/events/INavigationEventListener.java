package com.example.byebyeboxeyes.events;
/**
 * Interface for classes that listen for a navigation event to implement
 */
public interface INavigationEventListener {
    /**
     * Do something when the navigation event is specific to logging in
     */
    void onLoginSuccessful();

    /**
     * Do something when the navigation event is fired
     *
     * @param fxmlPath The fxml path of the navigation to view
     */
    void onNavigationEvent(String fxmlPath);
}
