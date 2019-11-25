package com.example.infinimood.controller;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.example.infinimood.view.CreateAccountActivity;
import com.example.infinimood.view.LoginActivity;
import com.example.infinimood.view.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**
 *
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
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            activity.refreshAuth();
                            if (task.isSuccessful()) {
                                activity.startActivityNoHistory(UserProfileActivity.class);
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                    activity.toast("Invalid email");
                                } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                    activity.toast("Incorrect password");
                                } catch (Exception e) {
                                    activity.toast(R.string.login_failed);
                                }
                            }
                            activity.hideOverlay();
                        }
                    });
        }
    }

    public void signUp() {
        final Intent intent = new Intent(activity, CreateAccountActivity.class);
        activity.startActivity(intent);
    }

}
