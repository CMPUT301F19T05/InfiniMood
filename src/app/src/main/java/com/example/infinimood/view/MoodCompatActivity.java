package com.example.infinimood.view;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MoodCompatActivity extends AppCompatActivity {

    protected FirebaseAuth firebaseAuth;

    protected FirebaseFirestore firebaseFirestore;

    protected FirebaseUser firebaseUser = null;

    public MoodCompatActivity() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    protected void toast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void toast(int id) {
        toast(getString(id));
    }

    protected void startActivityNoHistory(Class<?> activity) {
        final Intent intent = new Intent(this, activity);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
