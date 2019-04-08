package com.example.singlemind.Model;

public class Event {

    private String mEventType, mEventTime, mEventName, mEventContent;

    //constructor
    public Event(String eventType, String eventTime, String eventName, String eventContent) {
        mEventType = eventType;
        mEventTime = eventTime;
        mEventName = eventName;
        mEventContent = eventContent;
    }

    public String getmEventType() {
        return mEventType;
    }

    public void setmEventType(String mEventType) {
        this.mEventType = mEventType;
    }

    public String getmEventTime() {
        return mEventTime;
    }

    public void setmEventTime(String mEventTime) {
        this.mEventTime = mEventTime;
    }

    public String getmEventName() {
        return mEventName;
    }

    public void setmEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public String getmEventContent() {
        return mEventContent;
    }

    public void setmEventContent(String mEventContent) {
        this.mEventContent = mEventContent;
    }
}
