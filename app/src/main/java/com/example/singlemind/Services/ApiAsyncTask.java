package com.example.singlemind.Services;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.singlemind.Controllers.ICSManager;
import com.example.singlemind.UI.ImportActivity;
import com.example.singlemind.UI.MainActivity;
import com.example.singlemind.UI.RegistrationActivity;
import com.example.singlemind.Utility.DateFormatterUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
**/

public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
    private ImportActivity mActivity;

    public ApiAsyncTask(ImportActivity activity) {
        this.mActivity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            mActivity.updateResultsText(getDataFromApi());
            Intent intent = new Intent(mActivity, MainActivity.class);
            mActivity.startActivity(intent);

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
//            mActivity.showGooglePlayServicesAvailabilityErrorDialog(
//                    availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mActivity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    ImportActivity.REQUEST_AUTHORIZATION );

        } catch (IOException e) {

            Log.i("LoginActivity", "Error in background get Google cal events");
        }
        return null;
    }


    private List<String> getDataFromApi() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        List<String> eventStrings = new ArrayList<String>();
        Events events = mActivity.mService.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Event> items = events.getItems();
        List<com.example.singlemind.Model.Event> singlemindEvents = new ArrayList<>();

        for (Event event : items) {

            //create new event
            com.example.singlemind.Model.Event e = new com.example.singlemind.Model.Event();

            //get start time
            DateTime start = event.getStart().getDateTime();

            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();

            }

            String fixedTime = new DateFormatterUtil().fixDateFromGoogle(start.toString());

            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), fixedTime));

            e.setmEventName(event.getSummary());
            e.setmEventDescription(event.getDescription());
            e.setmEventTime(fixedTime);
            e.setmEventUID(System.currentTimeMillis());
            e.setmEventType(5);
            singlemindEvents.add(e);
        }

        //now we save to firebase
        ICSManager.getInstance().saveEvents(singlemindEvents);

        return eventStrings;
    }

}