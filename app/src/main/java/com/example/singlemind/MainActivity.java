package com.example.singlemind;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static String sURL = "https://cc.csusm.edu/my/index.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AsyncCallWS test = new AsyncCallWS();
        test.execute();

    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method

            CougarCourses test = new CougarCourses();

            //test CC method
            try {
                test.download("test", "test", sURL);
            }
            catch (IOException e) {
                Log.i("IOException:", "IO Exception thrown");
            }

            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //do something
        }

        @Override

        protected void onPreExecute() {
            //do something
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //do something
        }
    }
}
