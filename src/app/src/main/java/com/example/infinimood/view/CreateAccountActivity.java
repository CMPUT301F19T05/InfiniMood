package com.example.infinimood.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.HashMap;
import java.util.Map;

/**
 *  CreateAccountActivity.java
 *  Activity for creating user accounts (fields for username, password, etc.)
 */

public class CreateAccountActivity extends MoodCompatActivity {

    FrameLayout progressBarContainer;

    EditText editTextUsername;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPasswordRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        progressBarContainer = findViewById(R.id.progress_bar_container);

        editTextUsername = findViewById(R.id.createAccountUsernameEditText);
        editTextEmail = findViewById(R.id.loginCreateAccountEmailEditText);
        editTextPassword = findViewById(R.id.loginCreateAccountPasswordEditText);
        editTextPasswordRepeat = findViewById(R.id.createAccountPasswordRepeatEditText);
    }

    public void onSubmitClicked(View view) {
        final String username = editTextUsername.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String passwordRepeat = editTextPasswordRepeat.getText().toString();

        if (username.isEmpty()) {
            toast(R.string.error_username_required);
            editTextUsername.requestFocus();
        } else if (!email.contains("@")) {
            toast(R.string.error_email_invalid);
            editTextEmail.requestFocus();
        } else if (password.isEmpty()) {
            toast(R.string.error_password_required);
            editTextPassword.requestFocus();
        } else if (password.length() < 6) {
            toast(R.string.error_password_too_short);
            editTextPassword.requestFocus();
        } else if (!password.equals(passwordRepeat)) {
            toast(R.string.error_password_mismatch);
            editTextPasswordRepeat.requestFocus();
        } else {
            progressBarContainer.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseUser = firebaseAuth.getCurrentUser();

                                Map<String, Object> map = new HashMap<>();
                                map.put("username", username);

                                firebaseFirestore.collection("users")
                                        .document(firebaseUser.getUid())
                                        .set(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!task.isSuccessful()) {
                                                    toast("Could not save username, you can set it later");
                                                }
                                                startActivityNoHistory(UserProfileActivity.class);
                                                progressBarContainer.setVisibility(View.GONE);
                                            }
                                        });
                            } else {
                                toast("Account creation failed");
                                progressBarContainer.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    public void onBackClicked(View view) {
        finish();
    }

}
