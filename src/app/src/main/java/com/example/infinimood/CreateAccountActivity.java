package com.example.infinimood;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateAccountActivity extends MoodCompatActivity {

    EditText editTextUsername;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPasswordRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

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

        }
    }

    public void onBackClicked(View view) {
        finish();
    }

}
