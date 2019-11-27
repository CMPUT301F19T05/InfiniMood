package com.example.infinimood.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.infinimood.R;
import com.example.infinimood.controller.CreateAccountController;
import com.example.infinimood.model.CreateAccountModel;

import java.util.Observable;
import java.util.Observer;

/**
 * CreateAccountActivity.java
 * Activity for creating user accounts (fields for username, password, etc.)
 */

public class CreateAccountActivity extends MoodCompatActivity implements Observer {

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
        model.addObserver(this);
        controller = new CreateAccountController(this, model);

        progressOverlayContainer = findViewById(R.id.progressOverlayContainer);

        editTextUsername = findViewById(R.id.createAccountUsernameEditText);
        editTextEmail = findViewById(R.id.loginCreateAccountEmailEditText);
        editTextPassword = findViewById(R.id.loginCreateAccountPasswordEditText);
        editTextPasswordRepeat = findViewById(R.id.createAccountPasswordRepeatEditText);
    }

    @Override
    public void update(Observable o, Object args) {
        final String newUsername = model.getUsername();
        final String newEmail = model.getEmail();
        final String newPassword = model.getPassword();
        final String newPasswordRepeat = model.getPasswordRepeat();

        if (!editTextUsername.getText().toString().equals(newUsername)) {
            editTextUsername.setText(newUsername);
        }
        if (!editTextEmail.getText().toString().equals(newEmail)) {
            editTextEmail.setText(newEmail);
        }
        if (!editTextPassword.getText().toString().equals(newPassword)) {
            editTextPassword.setText(newPassword);
        }
        if (!editTextPasswordRepeat.getText().toString().equals(newPasswordRepeat)) {
            editTextPasswordRepeat.setText(newPasswordRepeat);
        }
    }

    public void onSubmitClicked(View view) {
        model.setUsername(editTextUsername.getText().toString());
        model.setEmail(editTextEmail.getText().toString());
        model.setPassword(editTextPassword.getText().toString());
        model.setPasswordRepeat(editTextPasswordRepeat.getText().toString());
        model.notifyObservers();
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
