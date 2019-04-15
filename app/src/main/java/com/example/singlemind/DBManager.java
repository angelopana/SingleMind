package com.example.singlemind;

public class DBManager {

    private static DBManager sSoleInstance;

    private DBManager(){}  //private constructor.

    public static DBManager getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new DBManager();
        }

        return sSoleInstance;
    }

}