package com.example.singlemind.Utility;

import android.util.Log;

import com.google.api.client.util.DateTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatterUtil {

    public DateFormatterUtil(){}
    private final static String TAG = "DateFormatterUtil";

    public void getStartTimeFromDate(DateTime date) {

        DateFormat output = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        String start = output.format(date);


        Log.i(TAG, "day and time of event is: " + start.toString());


//        return outputStr;
    }

    public String getTimeDate(Calendar cal) {

        Date timeDate = cal.getTime();
        String outputStr = null;
        DateFormat output = new SimpleDateFormat("h:mmaa");
        outputStr = output.format(timeDate);

        return outputStr;
    }

    public String getDayDate(Calendar cal) {

        Date timeDate = cal.getTime();
        String outputStr = null;
        DateFormat output = new SimpleDateFormat("EEEE MM/dd/yy");
        outputStr = output.format(timeDate);

        return outputStr;
    }

    public String getDatePickerDate(Calendar cal) {

        Date timeDate = cal.getTime();
        String outputStr = null;
        DateFormat output = new SimpleDateFormat("MM/dd/yy");
        outputStr = output.format(timeDate);

        return outputStr;
    }

    public String getFullDate(Calendar cal) {
        Date timeDate = cal.getTime();
        String outputStr = null;
        DateFormat output = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        outputStr = output.format(timeDate);

        return outputStr;
    }

    public Calendar createCalDate() {
        Calendar cal = Calendar.getInstance();

        return cal;
    }

    public Calendar getCalDateFromString(String input) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            Date date = sdf.parse(input);
            cal.setTime(date);
            return cal;
        }
        catch (ParseException e) {
            Log.e(TAG, e.toString());
        }

        return cal;
    }

    public String fixDateFromGoogle(String date){

        String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
        DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
        org.joda.time.DateTime dateTime = dtf.parseDateTime(date);

        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ss");

        String test = dtfOut.print(dateTime);
        return test;
    }
}
