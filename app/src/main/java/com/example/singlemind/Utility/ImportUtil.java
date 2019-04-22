package com.example.singlemind.Utility;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;

import com.example.singlemind.Model.Event;
import com.google.protobuf.StringValue;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportUtil {

    public ImportUtil(){}

    private final static String TAG = "com.example.singlemind.Utility.ImportUtil";

    public String[] stringList;
    //StringBuilder dStart = new StringBuilder();

    List<Event> calCC = new ArrayList<>();
    //List<String> dStart = new ArrayList<>();
    //List<String> dEnd = new ArrayList<>();
    //List<String> summary = new ArrayList<>();

    String cName = "";
    String dEnd = "";
    String summary = "";
    public String readTextFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            //Log.i("======================INSIDE WHILE======================", line);
            if(line.contains("CATEGORIES")){
                cName= parseCatagories(line);
            }
            else if(line.contains("DTEND")){
                dEnd = fixDate(line);
            }
            else if(line.contains("SUMMARY")){
                summary= parseSummary(line);
            }

            //Testing----------
            calCC.add(new Event(cName, 0, dEnd, summary, System.currentTimeMillis()));

            //sEvent.setmEventUID(System.currentTimeMillis());
        }//end of while-loop


        Log.i("---------", calCC.get(0).getmEventName());
        //Log.i("======================END DATE======================", dEnd.toString());

        inputStream.close();

        return stringBuilder.toString();
    }

    private String fixDate(String d){

        String fdate = "";
        String newDate = null;
        Date date = null;

        //parsing the ics to reformt the date
        fdate = parseDates(d);

        //old format of parsed dates
        String inputPattern = "yyyyMMddhhmmss";

        //the new output format
        String outputPattern = "MM/dd/yyyy hh:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        //reformating the dates
        try {

            date = inputFormat.parse(fdate);
            newDate = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }//end of fixDate

    //Parse ICS date to only use date & hour
    public String parseDates(String s){
        //(\d*) grabs all numbers
        Pattern p = Pattern.compile("(\\d*)");

        //matching date numbers & hours only
        Matcher m = p.matcher(s);
        StringBuilder i = new StringBuilder();

        while(m.find()){
            i.append(m.group());
        }
        return i.toString();
    }//return only fixed start date numbers


    @org.jetbrains.annotations.NotNull
    private String parseSummary(String s) {
        //regex code to ignore Summary and just grab discription
        Pattern p = Pattern.compile("\\b(?!(?:SUMMARY:\\b))[\\w ]+");
        //matching date numbers & hours only
        Matcher m = p.matcher(s);
        StringBuilder i = new StringBuilder();

        while(m.find()){
            i.append(m.group());
        }//end of whileloop
       return i.toString();
    }//end of summary parse method


    @NotNull
    private String parseCatagories(String s) {
        //regex code to ignore Summary and just grab discription
        Pattern p = Pattern.compile("\\b(?!(?:CATEGORIES:\\b))[\\w-]+");
        //matching date numbers & hours only
        Matcher m = p.matcher(s);
        StringBuilder i = new StringBuilder();

        while(m.find()){
            i.append(m.group());
        }//end of whileloop
        return i.toString();
    }//end of catagories parse method
}//end of class

