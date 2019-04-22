package com.example.singlemind.Utility;

import com.example.singlemind.Model.Event;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventBuilderUtil {

    private List<Event> mEvents = new ArrayList<>();

    EventBuilderUtil(List<Event> events) {
        mEvents = events;
    }

    public List<Event> getEventsByDay(List<Event> events, Calendar calendar){

        for (Event e: events) {
            if (isWithinRange(calendar)){
                mEvents.add(e);
            }
        }

        return mEvents;
    }

    boolean isWithinRange(Calendar testDate) {
        Calendar startDate = atStartOfDay(testDate);
        Calendar endDate = atEndOfDay(testDate);
        return !(testDate.before(startDate) || testDate.after(endDate));
    }

    public Calendar atEndOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }

    public Calendar atStartOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}
