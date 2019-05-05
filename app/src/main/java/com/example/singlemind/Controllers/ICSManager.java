package com.example.singlemind.Controllers;

import android.util.Log;

import com.example.singlemind.Model.Event;
import com.example.singlemind.UI.IUpdatable;

import java.util.ArrayList;
import java.util.List;

public class ICSManager {

    private static ICSManager sSoleInstance;
    private ArrayList<Event> mEvents = new ArrayList<>();
    private static int mHomeworkCounter, mAppointmentCounter, mGatheringCounter, mPartyCounter, mBirthdayCounter, mOtherCounter;

    private static final String TAG = "ICSManager";

    private ICSManager(){
        mHomeworkCounter = 0;
        mAppointmentCounter = 0;
        mGatheringCounter = 0;
        mPartyCounter = 0;
        mBirthdayCounter = 0;
        mOtherCounter = 0;
    }  //private constructor.

    public static ICSManager getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new ICSManager();
        }

        return sSoleInstance;
    }

    public void saveEvents(List<Event> events){
        for (Event event: events) {

            DBManager.getInstance().setEvent(event, new IUpdatable() {
                @Override
                public void onUpdateSuccess() {
                    switch(event.getmEventType()){
                        case 0:
                            setHomeworkCounter(mHomeworkCounter++);
                        case 1:
                            setAppointmentCounter(mAppointmentCounter++);
                        case 2:
                            setGatheringCounter(mGatheringCounter++);
                        case 4:
                            setPartyCounter(mPartyCounter++);
                        case 5:
                            setBirthdayCounter(mBirthdayCounter++);
                        case 6:
                            setOtherCounter(mOtherCounter++);
                    }
                }

                @Override
                public void onUpdateFailed() {
                    Log.i(TAG, "fail");
                }
            });
        }
    }

    public int getHomeworkCounter(){
        return mHomeworkCounter;
    }

    public void setHomeworkCounter(int amount){
        mHomeworkCounter = amount;
    }

    public int getAppointerCounter(){
        return mAppointmentCounter;
    }

    public void setAppointmentCounter(int amount){
        mAppointmentCounter = amount;
    }

    public int getGatheringCounter(){
        return mGatheringCounter;
    }

    public void setGatheringCounter(int amount){
        mGatheringCounter = amount;
    }

    public int getBirthdayCounter(){
        return mBirthdayCounter;
    }

    public void setBirthdayCounter(int amount){
        mBirthdayCounter = amount;
    }

    public int getPartyCounter(){
        return mPartyCounter;
    }

    public void setPartyCounter(int amount){
        mPartyCounter = amount;
    }

    public int getOtherCounter(){
        return mOtherCounter;
    }

    public void setOtherCounter(int amount){
        mOtherCounter = amount;
    }


}