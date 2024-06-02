package com.example.byebyeboxeyes.events;
import com.example.byebyeboxeyes.controller.TimerContainer;

/**
 * Interface for classes that listen for a timer favourite event to implement
 */
public interface ITimerFavouriteListener {
    /**
     * Do something when a timer favourite event is fired
     *
     * @param timerContainer The TimerContainer the event is fired from
     */
    void onFavourite(TimerContainer timerContainer);

}
