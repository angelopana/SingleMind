package com.example.singlemind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mFacebookBtn, mGoogleBtn, mEmailBtn, mPhoneBtn, mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        //initialize all buttons
        mFacebookBtn = findViewById(R.id.button_facebook);
        mGoogleBtn = findViewById(R.id.button_google);
        mEmailBtn = findViewById(R.id.button_email);
        mPhoneBtn = findViewById(R.id.button_phone);
        mRegisterBtn = findViewById(R.id.button_registration);

        //set button listeners
        mFacebookBtn.setOnClickListener(this);
        mGoogleBtn.setOnClickListener(this);
        mEmailBtn.setOnClickListener(this);
        mPhoneBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_facebook: {
                //implement firebase auth
                break;
            }

            case R.id.button_google: {
                //implement firebase auth
                break;
            }

            case R.id.button_email: {
                //go to email sign-in page
                Intent intent = new Intent(this, LoginActivity2.class);
                startActivity(intent);
                break;
            }

            case R.id.button_phone: {
                //implement firebase auth
                break;
            }

            case R.id.button_registration: {
                //go to registration page
                break;
            }
        }
    }
}
