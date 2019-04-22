package com.example.singlemind.Controllers;

import android.util.Log;

import com.applandeo.materialcalendarview.EventDay;
import com.example.singlemind.Model.Event;
import com.example.singlemind.R;
import com.example.singlemind.UI.IFiretoreObjectListener;
import com.example.singlemind.UI.IUpdatable;
import com.example.singlemind.Utility.DateFormatterUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class DBManager {

    private static final String TAG = "DBManager";
    private static final String DESCIRPTION_KEY = "mEventDescription";
    private static final String TITLE_KEY = "mEventName";
    private static final String DATE_KEY = "mEventTime";
    private static final String TYPE_KEY = "mEventType";
    private static final String UID_KEY = "mEventUID";


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static DBManager sSoleInstance;

    private DBManager(){}  //private constructor.

    public static DBManager getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new DBManager();
        }

        return sSoleInstance;
    }


    public void setEvent(Event event, final IUpdatable iUpdatable) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String identifier = Long.valueOf(event.getmEventUID()).toString();

        db.collection(user.getEmail()).document(identifier).set(event, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        iUpdatable.onUpdateSuccess();
                        Log.d(TAG, "Event written successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iUpdatable.onUpdateFailed();
                        Log.w(TAG, "Error adding event", e);
                    }
                });
    }


    public void getEventDays(final IFiretoreObjectListener iFiretoreObjectListener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        List<EventDay> mEvents = new ArrayList<>();

        db.collection(user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String time = document.getString(DATE_KEY);

                                Calendar cal = Calendar.getInstance();
                                cal = new DateFormatterUtil().getCalDateFromString(time);

                                //add dot dependent on type
                                mEvents.add(new EventDay(cal, R.drawable.ic_dot));
                            }

                            iFiretoreObjectListener.onRetrievalSuccess(mEvents);
                        } else {

                            Log.d(TAG, "Error getting documents: ", task.getException());
                            iFiretoreObjectListener.onRetrievalFailure();
                        }
                    }
                });
    }

    public void getEvents(final IFiretoreObjectListener iFiretoreObjectListener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        List<Event> mRecyclerEvents = new ArrayList<>();

        db.collection(user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String description = document.getString(DESCIRPTION_KEY);
                                String title = document.getString(TITLE_KEY);
                                String time = document.getString(DATE_KEY);
                                int type = document.getLong(TYPE_KEY).intValue();
                                long UID = (Long) document.get(UID_KEY);

                                mRecyclerEvents.add(new Event(title, type, time, description, UID));
                            }

                            iFiretoreObjectListener.onRetrievalSuccess(mRecyclerEvents);
                        } else {

                            Log.d(TAG, "Error getting documents: ", task.getException());
                            iFiretoreObjectListener.onRetrievalFailure();
                        }
                    }
                });
    }

}
