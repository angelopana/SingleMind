package com.example.singlemind.Utility;

import com.example.singlemind.Model.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventBuilderUtil {

    private List<Event> mEvents = new ArrayList<>();

    private static final String TAG = "EventBuilderUtil";

    public EventBuilderUtil() {}

    public EventBuilderUtil(List<Event> events) {
        mEvents = events;
    }

    public List<Event> getEventsByDay(List<Event> events, Calendar rangeCal){

        for (Event e: events) {
            String calStr = e.getmEventTime();
            Calendar testableCal = new DateFormatterUtil().getCalDateFromString(calStr);

            if (isWithinRange(testableCal, rangeCal)){
                mEvents.add(e);
            }
        }

        return mEvents;
    }

    boolean isWithinRange(Calendar testableCal, Calendar rangeDate) {
        Date startDate = atStartOfDay(rangeDate.getTime());
        Date endDate = atEndOfDay(rangeDate.getTime());

        Long testableStartTime = startDate.getTime();
        Long testableEndTime = endDate.getTime();

        if (testableCal.getTimeInMillis() < testableStartTime ||
                    testableCal.getTimeInMillis() > testableEndTime) {
            return false;
        }
        else {
            return true;
        }
    }

    public Date atEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public Date atStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
