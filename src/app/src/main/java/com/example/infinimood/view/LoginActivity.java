package com.example.infinimood.view;

import com.example.infinimood.R;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.infinimood.controller.LoginController;

/**
 * LoginActivity.java
 * Default activity when not logged in, redirects to user profile if already logged in
 * Options to login or create account
 */

public class LoginActivity extends MoodCompatActivity {

    private LoginController controller;

    private FrameLayout progressOverlayContainer;

    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        controller = new LoginController(this);

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

    public void onLoginClicked(View view) {
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        controller.login(email, password);
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
