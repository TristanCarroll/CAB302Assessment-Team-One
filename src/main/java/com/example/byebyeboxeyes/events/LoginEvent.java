package com.example.byebyeboxeyes.events;

import javafx.event.Event;
import javafx.event.EventType;

public class LoginEvent extends Event {
    public static final EventType<LoginEvent> loginSuccessful = new EventType<>("loginSuccessful");

    public LoginEvent() {
        super(loginSuccessful);
    }
}
