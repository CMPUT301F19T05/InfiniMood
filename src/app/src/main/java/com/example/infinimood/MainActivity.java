package com.example.infinimood;
import com.example.infinimood.view.MoodCreateEditActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends MoodCompatActivity {

    FrameLayout progressBarContainer;

    EditText editTextUsername;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        progressBarContainer = findViewById(R.id.progress_bar_container);

        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);

        // TODO: Debug only
        Button test_addEdit = findViewById(R.id.test_add_edit);
        test_addEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MoodCreateEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseUser != null) {
            toast("Welcome back!");
            startActivityNoHistory(AddEditMood.class);
        }
    }

    public void onCreateAccountClicked(View view) {
        final Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    public void onLoginClicked(View view) {
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        if (username.isEmpty()) {
            toast("Please enter username");
            editTextUsername.requestFocus();
        } else if (password.isEmpty()) {
            toast("Please enter password");
            editTextPassword.requestFocus();
        } else {
            progressBarContainer.setVisibility(View.VISIBLE);
            progressBarContainer.bringToFront();

            firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivityNoHistory(AddEditMood.class);
                            } else {
                                toast("Login failed, please try again");
                                editTextPassword.requestFocus();
                            }

                            progressBarContainer.setVisibility(View.GONE);
                        }
                    });
        }
    }

}
