package com.example.infinimood.view;

import com.example.infinimood.R;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.infinimood.controller.LoginController;
import com.example.infinimood.model.LoginModel;

import java.util.Observable;
import java.util.Observer;

/**
 * LoginActivity.java
 * Default activity when not logged in, redirects to user profile if already logged in
 * Options to login or create account
 */

public class LoginActivity extends MoodCompatActivity implements Observer {

    private LoginModel model;
    private LoginController controller;

    private FrameLayout progressOverlayContainer;

    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        model = new LoginModel();
        model.addObserver(this);
        controller = new LoginController(this, model);

        progressOverlayContainer = findViewById(R.id.progressOverlayContainer);

        editTextEmail = findViewById(R.id.loginCreateAccountEmailEditText);
        editTextPassword = findViewById(R.id.loginCreateAccountPasswordEditText);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseController.userAuthenticated()) {
            controller.userLoggedIn();
        }
    }

    @Override
    public void update(Observable o, Object args) {
        final String newEmail = model.getEmail();
        final String newPassword = model.getPassword();

        if (!editTextEmail.getText().toString().equals(newEmail)) {
            editTextEmail.setText(newEmail);
        }
        if (!editTextPassword.getText().toString().equals(newPassword)) {
            editTextPassword.setText(newPassword);
        }
    }

    public void onLoginClicked(View view) {
        model.setEmail(editTextEmail.getText().toString());
        model.setPassword(editTextPassword.getText().toString());
        model.notifyObservers();
        controller.login();
    }

    public void onCreateAccountClicked(View view) {
        controller.signUp();
    }

    public void focusEmailField() {
        editTextEmail.requestFocus();
    }

    public void focusPasswordField() {
        editTextPassword.requestFocus();
    }

    public void showOverlay() {
        progressOverlayContainer.setVisibility(View.VISIBLE);
        progressOverlayContainer.bringToFront();
    }

    public void hideOverlay() {
        progressOverlayContainer.setVisibility(View.GONE);
    }

}
