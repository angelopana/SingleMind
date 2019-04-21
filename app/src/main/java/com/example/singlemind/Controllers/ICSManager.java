package com.example.singlemind.Controllers;

import android.util.Log;

import com.example.singlemind.Model.Event;

import java.util.ArrayList;

public class ICSManager {

    private static ICSManager sSoleInstance;
    private ArrayList<Event> mEvents = new ArrayList<>();

    private static final String TAG = "ICSManager";

    private ICSManager(){}  //private constructor.

    public static ICSManager getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new ICSManager();
        }

        return sSoleInstance;
    }

    public void buildEventFromICS() {
        //
    }

}