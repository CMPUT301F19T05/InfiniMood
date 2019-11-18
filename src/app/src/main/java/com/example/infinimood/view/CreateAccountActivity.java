package com.example.infinimood.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CreateAccountActivity.java
 * Activity for creating user accounts (fields for username, password, etc.)
 */

public class CreateAccountActivity extends MoodCompatActivity {

    private FrameLayout progressOverlayContainer;

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        progressOverlayContainer = findViewById(R.id.progressOverlayContainer);

        editTextUsername = findViewById(R.id.createAccountUsernameEditText);
        editTextEmail = findViewById(R.id.loginCreateAccountEmailEditText);
        editTextPassword = findViewById(R.id.loginCreateAccountPasswordEditText);
        editTextPasswordRepeat = findViewById(R.id.createAccountPasswordRepeatEditText);
    }

    public void onSubmitClicked(View view) {
        final String username = editTextUsername.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String passwordRepeat = editTextPasswordRepeat.getText().toString();

        if (username.isEmpty()) {
            toast(R.string.error_username_required);
            editTextUsername.requestFocus();
        } else if (email.isEmpty()) {
            toast(R.string.error_email_required);
            editTextEmail.requestFocus();
        } else if (password.isEmpty()) {
            toast(R.string.error_password_required);
            editTextPassword.requestFocus();
        } else if (password.length() < 6) {
            toast(R.string.error_password_too_short);
            editTextPassword.requestFocus();
        } else if (!password.equals(passwordRepeat)) {
            toast(R.string.error_password_mismatch);
            editTextPasswordRepeat.requestFocus();
        } else {
            progressOverlayContainer.setVisibility(View.VISIBLE);

            firebaseController.createUser(CreateAccountActivity.this, username, email, password, new BooleanCallback() {
                @Override
                public void onCallback(boolean success) {
                    if (success) {
                        toast(R.string.account_create_successful);
                        firebaseController.setCurrentUserData(username, new BooleanCallback() {
                            @Override
                            public void onCallback(boolean success) {
                                if (success) {
                                    startActivityNoHistory(UserProfileActivity.class);
                                } else {
                                    toast("Could not save username, you can set it later");
                                }
                                progressOverlayContainer.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        progressOverlayContainer.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public void onBackClicked(View view) {
        finish();
    }

}
