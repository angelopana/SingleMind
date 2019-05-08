package com.example.singlemind.Controllers;

import android.util.Log;

import com.example.singlemind.Model.Event;
import com.example.singlemind.UI.IUpdatable;

import java.util.ArrayList;
import java.util.List;

public class ICSManager {

    private static ICSManager sSoleInstance;
    private ArrayList<Event> mEvents = new ArrayList<>();

    private static final String TAG = "ICSManager";

    private ICSManager(){
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
                    Log.i(TAG, "success");
                }

                @Override
                public void onUpdateFailed() {
                    Log.i(TAG, "fail");
                }
            });
        }
    }

}