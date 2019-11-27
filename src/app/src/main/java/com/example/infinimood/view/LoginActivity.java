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

    /**
     * onCreate
     * Ovverides onCreate. Gets the activity ready. Runs when activity is created.
     * @param savedInstanceState Bundle
     */
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

    /**
     * onStart
     * Overrides onStart. Logs user in if user is validly authenticated
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseController.userAuthenticated()) {
            controller.userLoggedIn();
        }
    }

    /**
     * update
     * updates editTexts from Login Model
     * @param o Observable
     * @param args Object
     */
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

    /**
     * onLoginClicked
     * Sets the model with information from the EditTexts
     * @param view View
     */
    public void onLoginClicked(View view) {
        model.setEmail(editTextEmail.getText().toString());
        model.setPassword(editTextPassword.getText().toString());
        model.notifyObservers();
        controller.login();
    }

    /**
     * onCreateAccountClicked
     * Tries to sign up
     * @param view View
     */
    public void onCreateAccountClicked(View view) {
        controller.signUp();
    }

    /**
     * focusEmailField
     * Requests focus on email editText
     */
    public void focusEmailField() {
        editTextEmail.requestFocus();
    }

    /**
     * focusPasswordField
     * Requests focus on Password editText
     */
    public void focusPasswordField() {
        editTextPassword.requestFocus();
    }

    /**
     * showOverlay
     */
    public void showOverlay() {
        progressOverlayContainer.setVisibility(View.VISIBLE);
        progressOverlayContainer.bringToFront();
    }

    /**
     * hideOverlay
     */
    public void hideOverlay() {
        progressOverlayContainer.setVisibility(View.GONE);
    }

}
