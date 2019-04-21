package com.example.singlemind.Utility;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatterUtil {

    public DateFormatterUtil(){}
    private final static String TAG = "DateFormatterUtil";

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

}
