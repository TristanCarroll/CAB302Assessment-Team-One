package com.example.byebyeboxeyes.events;

import com.example.byebyeboxeyes.controller.TimerContainer;

/**
 * Interface for classes that listen for a timer delete event to implement
 */
public interface ITimerDeleteListener {
    /**
     * Do something when a timer delete event is fired
     *
     * @param timerContainer The TimerContainer the delete event is fired from
     */
    void onDelete(TimerContainer timerContainer);

}
