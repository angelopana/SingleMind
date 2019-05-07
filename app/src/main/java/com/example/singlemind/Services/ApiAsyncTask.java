package com.example.singlemind.Services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.singlemind.Controllers.ICSManager;
import com.example.singlemind.UI.ImportActivity;
import com.example.singlemind.UI.LoginActivity;
import com.example.singlemind.UI.MainActivity;
import com.example.singlemind.Utility.DateFormatterUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */

/**
 * Created by miguel on 5/29/15.
 */

public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
    private ImportActivity mActivity;

    /**
     * Constructor.
     * @param activity MainActivity that spawned this task.
     */
    public ApiAsyncTask(ImportActivity activity) {
        this.mActivity = activity;
    }

    /**
     * Background task to call Google Calendar API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            mActivity.updateResultsText(getDataFromApi());

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

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
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

            //format date.

            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();

                //fix date
            }
            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), start));

            Log.i("ASYNC TASK  ", event.getSummary() + "  " + event.getDescription() + "  " + start);

            e.setmEventName(event.getSummary());
            e.setmEventDescription(event.getDescription());
            //e.setmEventTime(start);
            e.setmEventUID(System.currentTimeMillis());
            e.setmEventType(6);
            singlemindEvents.add(e);
        }

        //now save count reference and save to firebase
        //ICSManager.getInstance().saveEvents(singlemindEvents);

        return eventStrings;
    }

}