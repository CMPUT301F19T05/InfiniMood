package com.example.infinimood.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.example.infinimood.model.CreateAccountModel;
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

    private CreateAccountActivity view;
    private CreateAccountModel model;

    /**
     * CreatAccountController
     * Basic constructor for CreateAccountController
     * @param view CreateAccountActivity to be controlled
     * @param model CreateAccountModel backing the activity
     */
    public CreateAccountController(CreateAccountActivity view, CreateAccountModel model) {
        this.view = view;
        this.model = model;
    }

    /**
     * verifyAndCreateUser
     * Method for verifying user validity and creating user for firebase
     * @param newUsername String requested username
     * @param email String requested email
     * @param password String requested password
     * @param callback Callback function
     */
    private void verifyAndCreateUser(String newUsername, String email, String password, BooleanCallback callback) {
        firebaseFirestore
                .collection("users")
                .get()
                .addOnCompleteListener((@NonNull Task<QuerySnapshot> task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = (String) document.get("username");
                            if (newUsername.equals(username)) {
                                view.toast(R.string.error_username_taken);
                                view.focusUsernameField();
                                callback.onCallback(false);
                                return;
                            }
                        }
                        createUser(email, password, callback);
                    } else {
                        view.toast(R.string.account_create_failed);
                        callback.onCallback(false);
                    }
                });
    }

    /**
     * createUser
     * Checks to see if all fields have been entered, if so, create user in firebase
     * @param email User's email
     * @param password User's password
     * @param callback Callback function
     */
    private void createUser(String email, String password, BooleanCallback callback) {
        firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((@NonNull Task<AuthResult> task) -> {
                    view.refreshAuth();
                    if (task.isSuccessful()) {
                        callback.onCallback(true);
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException weakPassword) {
                            view.toast(R.string.error_password_too_short);
                            view.focusPasswordField();
                        } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                            view.toast(R.string.error_email_invalid);
                            view.focusEmailField();
                        } catch (FirebaseAuthUserCollisionException existEmail) {
                            view.toast(R.string.error_email_taken);
                            view.focusEmailField();
                        } catch (Exception e) {
                            Log.w(TAG, e);
                            view.toast(R.string.account_create_failed);
                        }
                        callback.onCallback(false);
                    }
                });
    }

    /**
     * setupUserData
     * Sets user username in firebase
     * @param username String username to set
     * @param callback Callback method
     */
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

    /**
     * createAccount
     * Checks input fields for valid input, then creates account
     */
    public void createAccount() {
        final String username = model.getUsername();
        final String email = model.getEmail();
        final String password = model.getPassword();
        final String passwordRepeat = model.getPasswordRepeat();

        if (username.isEmpty()) {
            view.toast(R.string.error_username_required);
            view.focusUsernameField();
        } else if (email.isEmpty()) {
            view.toast(R.string.error_email_required);
            view.focusEmailField();
        } else if (password.isEmpty()) {
            view.toast(R.string.error_password_required);
            view.focusPasswordField();
        } else if (password.length() < 6) {
            view.toast(R.string.error_password_too_short);
            view.focusPasswordField();
        } else if (!password.equals(passwordRepeat)) {
            view.toast(R.string.error_password_mismatch);
            view.focusPasswordRepeatField();
        } else {
            view.showOverlay();
            verifyAndCreateUser(username, email, password, (boolean userCreated) -> {
                if (userCreated) {
                    view.toast(R.string.account_create_successful);
                    setupUserData(username, (boolean userDataSaved) -> {
                        if (userDataSaved) {
                            view.startActivityNoHistory(UserProfileActivity.class);
                        } else {
                            view.toast(R.string.could_not_save_username);
                        }
                        view.hideOverlay();
                    });
                } else {
                    view.hideOverlay();
                }
            });
        }
    }

}
