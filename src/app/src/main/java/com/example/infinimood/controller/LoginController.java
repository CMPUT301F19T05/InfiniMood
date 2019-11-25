package com.example.infinimood.controller;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
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

    private LoginActivity activity;

    public LoginController(LoginActivity activity) {
        this.activity = activity;
    }

    public void userLoggedIn() {
        activity.startActivityNoHistory(UserProfileActivity.class);
    }

    public void login(String email, String password) {
        if (email.isEmpty()) {
            activity.toast("Please enter your email");
            activity.focusEmailField();
        } else if (password.isEmpty()) {
            activity.toast("Please enter your password");
            activity.focusPasswordField();
        } else {
            activity.showOverlay();
            firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((@NonNull Task<AuthResult> task) -> {
                        activity.refreshAuth();
                        if (task.isSuccessful()) {
                            activity.startActivityNoHistory(UserProfileActivity.class);
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                activity.toast(R.string.error_email_invalid);
                            } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                activity.toast(R.string.login_wrong_password);
                            } catch (Exception e) {
                                activity.toast(R.string.login_failed);
                            }
                        }
                        activity.hideOverlay();
                    });
        }
    }

    public void signUp() {
        activity.startActivityWithHistory(CreateAccountActivity.class);
    }

}
