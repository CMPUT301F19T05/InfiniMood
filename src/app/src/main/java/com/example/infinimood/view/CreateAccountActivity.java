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

        editTextUsername = findViewById(R.id.edit_text_username);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextPasswordRepeat = findViewById(R.id.edit_text_password_repeat);
    }

    public void onSubmitClicked(View view) {
        final String username = editTextUsername.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String passwordRepeat = editTextPasswordRepeat.getText().toString();

        if (username.isEmpty()) {
            toast("Please enter a username");
            editTextUsername.requestFocus();
        } else if (!email.contains("@")) {
            toast("Please enter a valid email");
            editTextEmail.requestFocus();
        } else if (password.isEmpty()) {
            toast("Please enter a password");
            editTextPassword.requestFocus();
        } else if (password.length() < 6) {
            toast("Please enter a longer password");
            editTextPassword.requestFocus();
        } else if (!password.equals(passwordRepeat)) {
            toast("Please enter the same password");
            editTextPasswordRepeat.requestFocus();
        } else {
            progressBarContainer.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseUser = firebaseAuth.getCurrentUser();
                                startActivityNoHistory(UserProfileActivity.class);
                            } else {
                                toast("Account creation failed");
                            }

                            progressBarContainer.setVisibility(View.GONE);
                        }
                    });
        }
    }

    public void onBackClicked(View view) {
        finish();
    }

}
