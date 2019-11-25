package com.example.infinimood.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.example.infinimood.view.CreateAccountActivity;
import com.example.infinimood.view.UserProfileActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * CreateAccountController.java
 * Controller for the create account activity
 */

public class CreateAccountController extends BaseController {

    private static final String TAG = "CreateAccountController";

    private CreateAccountActivity activity;

    public CreateAccountController(CreateAccountActivity activity) {
        this.activity = activity;
    }

    private void verifyAndCreateUser(String newUsername, String email, String password, BooleanCallback callback) {
        firebaseFirestore
                .collection("users")
                .get()
                .addOnCompleteListener((@NonNull Task<QuerySnapshot> task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = (String) document.get("username");
                            if (newUsername.equals(username)) {
                                activity.toast(R.string.error_username_taken);
                                callback.onCallback(false);
                                return;
                            }
                        }
                        createUser(email, password, callback);
                    } else {
                        activity.toast(R.string.account_create_failed);
                        callback.onCallback(false);
                    }
                });
    }

    private void createUser(String email, String password, BooleanCallback callback) {
        firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((@NonNull Task<AuthResult> task) -> {
                    activity.refreshAuth();
                    if (task.isSuccessful()) {
                        callback.onCallback(true);
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException weakPassword) {
                            activity.toast(R.string.error_password_too_short);
                        } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                            activity.toast(R.string.error_email_invalid);
                        } catch (FirebaseAuthUserCollisionException existEmail) {
                            activity.toast(R.string.error_email_taken);
                        } catch (Exception e) {
                            Log.w(TAG, e);
                            activity.toast(R.string.account_create_failed);
                        }
                        callback.onCallback(false);
                    }
                });
    }

    private void setupUserData(String username, BooleanCallback callback) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);

        firebaseFirestore
                .collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(userData)
                .addOnCompleteListener((@NonNull Task<Void> task) -> {
                    callback.onCallback(task.isSuccessful());
                });
    }

    public void createAccount(String username, String email, String password, String passwordRepeat) {
        if (username.isEmpty()) {
            activity.toast(R.string.error_username_required);
            activity.focusUsernameField();
        } else if (email.isEmpty()) {
            activity.toast(R.string.error_email_required);
            activity.focusEmailField();
        } else if (password.isEmpty()) {
            activity.toast(R.string.error_password_required);
            activity.focusPasswordField();
        } else if (password.length() < 6) {
            activity.toast(R.string.error_password_too_short);
            activity.focusPasswordRepeatField();
        } else if (!password.equals(passwordRepeat)) {
            activity.toast(R.string.error_password_mismatch);
            activity.focusPasswordField();
        } else {
            activity.showOverlay();
            verifyAndCreateUser(username, email, password, (boolean userCreated) -> {
                if (userCreated) {
                    activity.toast(R.string.account_create_successful);
                    setupUserData(username, (boolean userDataSaved) -> {
                            if (userDataSaved) {
                                activity.startActivityNoHistory(UserProfileActivity.class);
                            } else {
                                activity.toast(R.string.could_not_save_username);
                            }
                            activity.hideOverlay();
                    });
                } else {
                    activity.hideOverlay();
                }
            });
        }
    }

}
