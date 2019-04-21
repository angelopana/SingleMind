package com.example.singlemind.Model;

import java.util.Calendar;

public class Event {

    private String mEventName, mEventDescription;
    private String mEventTime;
    private int mEventType;
    private long mEventUID;

    //constructor
    public Event(){}

    //title, type, time, description, UID
    public Event(String eventName, int eventType, String eventTime, String eventDescription, long eventUID) {
        mEventType = eventType;
        mEventTime = eventTime;
        mEventName = eventName;
        mEventDescription = eventDescription;
        mEventUID = eventUID;
    }

    public long getmEventUID() {
        return mEventUID;
    }

    public void setmEventUID(long mEventUID) {
        this.mEventUID = mEventUID;
    }

    public int getmEventType() {
        return mEventType;
    }

    public void setmEventType(int mEventType) {
        this.mEventType = mEventType;
    }

    public String getmEventName() {
        return mEventName;
    }

    public void setmEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public String getmEventDescription() {
        return mEventDescription;
    }

    public void setmEventDescription(String mEventDescription) {
        this.mEventDescription = mEventDescription;
    }

    public String getmEventTime() {
        return mEventTime;
    }

    public void setmEventTime(String mEventTime) {
        this.mEventTime = mEventTime;
    }
}
