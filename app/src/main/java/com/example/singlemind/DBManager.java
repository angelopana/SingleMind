package com.example.singlemind;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class DBManager {


    private static final String TAG = "DBManager";
    private static final String UID_KEY = "UID";
    private static final String NAME_KEY = "Name";
    private static final String EMAIL_KEY = "Email";
    private static final String PHOTO_KEY = "Uri";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static DBManager sSoleInstance;

    private DBManager(){}  //private constructor.

    public static DBManager getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new DBManager();
        }

        return sSoleInstance;
    }

    public void addNewContract(FirebaseUser user) {

        Map<String, Object> newContact = new HashMap<>();

        newContact.put(EMAIL_KEY, user.getEmail());
        newContact.put(NAME_KEY, user.getDisplayName());
        newContact.put(UID_KEY, user.getUid());
        //newContact.put(PHOTO_KEY, user.getPhotoUrl().toString());

        db.collection("Users").document(user.getEmail()).set(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void updateContract(FirebaseUser user) {

        Map<String, Object> updateContact = new HashMap<>();

        updateContact.put(PHOTO_KEY, user.getPhotoUrl().toString());

        db.collection("Users").document(user.getEmail()).set(updateContact, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
}
