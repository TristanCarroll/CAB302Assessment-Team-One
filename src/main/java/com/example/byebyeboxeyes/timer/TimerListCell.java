package com.example.byebyeboxeyes.timer;

import javafx.scene.control.ListCell;

public class TimerListCell extends ListCell<Timer> {
    @Override
    protected void updateItem(Timer timer, boolean empty) {
        super.updateItem(timer, empty);
        if (empty || timer == null) {
            setText(null);
        } else {
            setText(timer.toString()); //Can replace with whatever customization we want for that page
        }
    }
}