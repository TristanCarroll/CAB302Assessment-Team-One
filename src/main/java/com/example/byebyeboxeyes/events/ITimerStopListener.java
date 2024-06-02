package com.example.byebyeboxeyes.events;

import com.example.byebyeboxeyes.timer.Timer;

/**
 * Interface for classes that listen for a timer stop event to implement
 */
public interface ITimerStopListener {
    /**
     * Do something when a timer stop event is fired
     *
     * @param timer The timer that is being stopped
     */
    void onStop(Timer timer);
}
