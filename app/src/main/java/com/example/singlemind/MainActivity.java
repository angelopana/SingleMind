package com.example.singlemind;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //vars
    private static String sURL = "https://cc.csusm.edu/my/index.php";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AsyncCallWS test = new AsyncCallWS();
        test.execute();


        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void signOut() {
        mAuth.signOut();
        //updateUI(null);
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
