package com.example.singlemind;

public class ICSManager {

    private static ICSManager sSoleInstance;

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