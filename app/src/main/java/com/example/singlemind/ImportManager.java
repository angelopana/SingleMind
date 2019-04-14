package com.example.singlemind;

public class ImportManager {

    private static ImportManager sSoleInstance;

    private ImportManager(){}  //private constructor.

    public static ImportManager getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new ImportManager();
        }

        return sSoleInstance;
    }


    private void importFromFile() {

    }
}
