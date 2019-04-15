package com.example.singlemind;

public class AuthManager {

    private static AuthManager sSoleInstance;

    private AuthManager(){}  //private constructor.

    public static AuthManager getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new AuthManager();
        }

        return sSoleInstance;
    }

}
