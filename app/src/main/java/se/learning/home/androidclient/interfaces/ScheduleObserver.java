package se.learning.home.androidclient.interfaces;

import DTO.Schedule;
import DTO.ScheduledEvent;

/**
 * Created by Alexander on 2016-04-28.
 */
public interface ScheduleObserver {
    //void getWholeSchedule(Schedule schedule);
    //void updateSchedule(ScheduledEvent newScheduledEvent);

    void updateSchedule(Schedule schedule);
}
