package com.example.singlemind;

import android.util.Log;

import com.applandeo.materialcalendarview.EventDay;
import com.example.singlemind.Model.Event;
import com.example.singlemind.Utility.DateFormatterUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventBuilder {

    private List<EventDay> mEvents = new ArrayList<>();

    private static final String TAG = "EventBuilder";

    EventBuilder(){}

    public void buildEventDayList(String title, int type, String time, String description, long UID){
//        Event e = new Event(title, type, time, description, UID);
//        mEvents.add(e);
        Calendar cal = Calendar.getInstance();
        cal = new DateFormatterUtil().getCalDateFromString(time);
        mEvents.add(new EventDay(cal, R.drawable.ic_dot));
    }
}
