package com.example.infinimood.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.CreateAccountController;
import com.example.infinimood.model.CreateAccountModel;
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

    private CreateAccountModel model;
    private CreateAccountController controller;

    private FrameLayout progressOverlayContainer;

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        model = new CreateAccountModel();
        controller = new CreateAccountController(this, model);

        progressOverlayContainer = findViewById(R.id.progressOverlayContainer);

        editTextUsername = findViewById(R.id.createAccountUsernameEditText);
        editTextEmail = findViewById(R.id.loginCreateAccountEmailEditText);
        editTextPassword = findViewById(R.id.loginCreateAccountPasswordEditText);
        editTextPasswordRepeat = findViewById(R.id.createAccountPasswordRepeatEditText);
    }

    public void onSubmitClicked(View view) {
        model.setUsername(editTextUsername.getText().toString());
        model.setEmail(editTextEmail.getText().toString());
        model.setPassword(editTextPassword.getText().toString());
        model.setPasswordRepeat(editTextPasswordRepeat.getText().toString());
        controller.createAccount();
    }

    public void focusUsernameField() {
        editTextUsername.requestFocus();
    }

    public void focusEmailField() {
        editTextEmail.requestFocus();
    }

    public void focusPasswordField() {
        editTextPassword.requestFocus();
    }

    public void focusPasswordRepeatField() {
        editTextPasswordRepeat.requestFocus();
    }

    public void showOverlay() {
        progressOverlayContainer.setVisibility(View.VISIBLE);
        progressOverlayContainer.bringToFront();
    }

    public void hideOverlay() {
        progressOverlayContainer.setVisibility(View.GONE);
    }

    public void onBackClicked(View view) {
        finish();
    }

}
