package com.example.infinimood.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.example.infinimood.model.LoginModel;
import com.example.infinimood.view.CreateAccountActivity;
import com.example.infinimood.view.LoginActivity;
import com.example.infinimood.view.UserProfileActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**
 * LoginController.java
 * Controller for the login activity
 */
public class LoginController extends BaseController {

    private static final String TAG = "LoginController";

    private LoginActivity view;
    private LoginModel model;

    /**
     * LoginController
     * Basic constructor for the LoginController class
     * @param view LoginActivity - The LoginActivity to be controlled
     * @param model LoginModel - The LoginModel backing the activity
     */
    public LoginController(LoginActivity view, LoginModel model) {
        this.view = view;
        this.model = model;
    }

    /**
     * userLoggedIn
     * Method to be called when user logs in, simply starts UserProfileActivity
     */
    public void userLoggedIn() {
        view.startActivityNoHistory(UserProfileActivity.class);
    }

    /**
     * login
     * Method to be called when user attempts to log in - ensures email and password are
     * entered properly, then tries to log into firebase, catching any error that may arise
     */
    public void login() {
        final String email = model.getEmail();
        final String password = model.getPassword();

        if (email.isEmpty()) {
            view.toast("Please enter your email");
            view.focusEmailField();
        } else if (password.isEmpty()) {
            view.toast("Please enter your password");
            view.focusPasswordField();
        } else {
            view.showOverlay();
            firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((@NonNull Task<AuthResult> task) -> {
                        view.refreshAuth();
                        if (task.isSuccessful()) {
                            view.startActivityNoHistory(UserProfileActivity.class);
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                view.toast(R.string.error_email_invalid);
                            } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                view.toast(R.string.login_wrong_password);
                            } catch (Exception e) {
                                Log.w(TAG, e);
                                view.toast(R.string.login_failed);
                            }
                        }
                        view.hideOverlay();
                    });
        }
    }

    /**
     * signUp
     * Method called when user tries to sign up, simply starts a CreateAccountActivity.
     */
    public void signUp() {
        view.startActivityWithHistory(CreateAccountActivity.class);
    }

}
