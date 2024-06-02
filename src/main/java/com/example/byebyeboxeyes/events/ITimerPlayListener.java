package com.example.byebyeboxeyes.events;

import com.example.byebyeboxeyes.timer.Timer;
/**
 * Interface for classes that listen for a timer play event to implement
 */
public interface ITimerPlayListener {
    /**
     * Do something when a timer start event is fired
     *
     * @param timer The timer the stop event is fired from
     */
    void onPlay(Timer timer);
}
