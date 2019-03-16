package com.example.singlemind;

import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity2 extends AppCompatActivity {

    //widgets
    private Button mSubmitBtn;
    private TextView mStatusTV;
    private EditText mUsernameET , mPasswordET;
    private ProgressBar mWebservicePG;

    //vars
    private String mUsername, mPassword;
    private boolean mLoginStatus;
    private static boolean sErrored = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        init();
    }

    private void init(){
        //initialize widgets
        mUsernameET = findViewById(R.id.edit_email);
        mPasswordET = findViewById(R.id.edit_password);
        mStatusTV = findViewById(R.id.text_error);
        mSubmitBtn = findViewById(R.id.button_submit_password);
        mWebservicePG = findViewById(R.id.progress_login);

        //dynamically change color of the progress circle (2nd param)
        mWebservicePG.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF000000, 0xf04e23));

        //set error text
        mStatusTV.setText("");
    }

    public void submitLogin(View view) {
        if (mUsernameET.getText().length() != 0 && mUsernameET.getText().toString() != "") {
            if(mPasswordET.getText().length() != 0 && mPasswordET.getText().toString() != ""){

                //get passwords from user
                mUsername = mUsernameET.getText().toString();
                mPassword = mPasswordET.getText().toString();

                //Create instance for AsyncCallWS
                AsyncCallWS task = new AsyncCallWS();

                //Call execute
                task.execute();
            }
            //If password text control is empty
            else{
                mStatusTV.setText("Please enter password");
            }
            //If email text control is empty
        } else {
            mStatusTV.setText("Please enter email");
        }
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            //Verify authentication here

            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //Make Progress Bar invisible
            mWebservicePG.setVisibility(View.INVISIBLE);
            //Initialize intent
            Intent intObj = new Intent(LoginActivity2.this, MainActivity.class);

            //testing
            mLoginStatus = true;

            //Error status is false
            if(!sErrored){
                //Based on Boolean value returned from WebService
                if(mLoginStatus){
                    //Navigate to Home Screen
                    startActivity(intObj);
                }else{
                    //Set Error message
                    mStatusTV.setText("Login Failed, try again");
                }
                //Error status is true
            }else{
                mStatusTV.setText("Error occured in invoking webservice");
            }
            //Re-initialize Error Status to False
            sErrored = false;
        }

        @Override
        protected void onPreExecute() {
            //Make Progress Bar visible
            mWebservicePG.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
