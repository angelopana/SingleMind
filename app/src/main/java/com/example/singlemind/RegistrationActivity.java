package com.example.singlemind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    //widgets
    private EditText mNameET, mEmailET, mPasswordET, mPassword2ET;
    private MaterialButton mSubmitBtn;

    //vars
    private FirebaseAuth mAuth;
    private static String sEmail, sPassword;
    private static final String TAG = "Registration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
    }

    private void init(){
        mNameET = findViewById(R.id.edit_name);
        mEmailET = findViewById(R.id.edit_email);
        mPasswordET = findViewById(R.id.edit_password);
        mPassword2ET = findViewById(R.id.edit_password2);
        mSubmitBtn = findViewById(R.id.button_submit_reg);

        mAuth = FirebaseAuth.getInstance();
    }


    public void accountRegistration (View view){
        //registration logic
        Log.d(TAG, "createAccount:" + mEmailET.getText().toString());
        if (!validateForm()) {
            return;
        }
        else {
            sEmail = mEmailET.getText().toString();
            sPassword = mPasswordET.getText().toString();
        }

        mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity2.class);
                            startActivity(intent);
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUserInfo(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    //add later if we want to..
    private void sendEmailVerification() {

        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(RegistrationActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
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

        String password2 = mPassword2ET.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            mPassword2ET.setError("Required.");
            valid = false;
        } else {
            mPassword2ET.setError(null);
        }

        if (!password.equals(password2)) {
            mPassword2ET.setError("Did not match.");
            Toast.makeText(RegistrationActivity.this,
                    "Passwords do not match, try again.",
                    Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

        private void updateUserInfo(FirebaseUser user) {

        if (user != null) {

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(mNameET.getText().toString())
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User's name updated.");
                            }
                        }
                    });
        } else {
            //update for fail
        }
    }

}
