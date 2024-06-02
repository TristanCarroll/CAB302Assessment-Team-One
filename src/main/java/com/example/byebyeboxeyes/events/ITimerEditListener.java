package com.example.byebyeboxeyes.events;

import com.example.byebyeboxeyes.controller.TimerContainer;
/**
 * Interface for classes that listen for a timer edit event to implement
 */
public interface ITimerEditListener {
    /**
     * Do something when a timer edit event is fired
     *
     * @param timerContainer The TimerContainer the event is fired from
     */
    void onEdit(TimerContainer timerContainer);
}
