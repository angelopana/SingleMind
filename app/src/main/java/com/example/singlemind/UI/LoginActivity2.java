package com.example.singlemind.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.singlemind.Controllers.DBManager;
import com.example.singlemind.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity2 extends AppCompatActivity {

    //widgets
    private Button mSubmitBtn;
    private TextView mStatusTV;
    private EditText mEmailET , mPasswordET;
    private ProgressBar mWebservicePG;

    //vars
    private FirebaseAuth mAuth;
    private static String sEmail, sPassword;

    //constants
    private static final String TAG = "LoginActivity2";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        init();
    }

    private void init(){

        //initialize widgets
        mEmailET = findViewById(R.id.edit_email);
        mPasswordET = findViewById(R.id.edit_password);
        mStatusTV = findViewById(R.id.text_error);
        mSubmitBtn = findViewById(R.id.button_submit_password);
        mWebservicePG = findViewById(R.id.progress_login);

        //dynamically change color of the progress circle (2nd param)
        //mWebservicePG.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF000000, 0xf04e23));

        //set error text
        mStatusTV.setText("");

        //get instance of Firebase
        mAuth = FirebaseAuth.getInstance();
    }


    public void submitLogin(View view) {

        mWebservicePG.setVisibility(View.VISIBLE);

        Log.d(TAG, "signIn:" + mEmailET.getText().toString());
        if (!validateForm()) {
            return;
        }
        else {
            sEmail = mEmailET.getText().toString();
            sPassword = mPasswordET.getText().toString();
        }

        mAuth.signInWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mWebservicePG.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "signInWithEmail:success");

                          //  DBManager.getInstance().addNewContract(mAuth.getCurrentUser());

                            Intent intent = new Intent(LoginActivity2.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity2.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            mWebservicePG.setVisibility(View.INVISIBLE);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }

                        // [END_EXCLUDE]
                    }
                });
    }



    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailET.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailET.setError("Required.");
            valid = false;
        } else {
            mEmailET.setError(null);
        }

        String password = mPasswordET.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordET.setError("Required.");
            valid = false;
        } else {
            mPasswordET.setError(null);
        }

        return valid;
    }

    //TODO (Evan 3/16) link ForgotPassword
    private void sendEmailVerification() {

        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity2.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity2.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void updateUI(FirebaseUser user) {

        if (user != null) {
          // update for sign-in
        } else {
            //update for fail
        }
    }

}
