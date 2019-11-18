package com.example.infinimood.view;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.model.MoodFactory;
import com.example.infinimood.model.Mood;

import java.util.ArrayList;

/**
 * MoodCompatActivity.java
 * Superclass for all other activities
 * Contains common functionality for all activities
 */

public abstract class MoodCompatActivity extends AppCompatActivity {

    private static final String TAG = "MoodCompatActivity";

    protected static ArrayList<Mood> moods = new ArrayList<>();

    protected static FirebaseController firebaseController = new FirebaseController();
    protected static MoodFactory moodFactory = new MoodFactory();

    public MoodCompatActivity() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void toast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void toast(int id) {
        toast(getString(id));
    }

    protected void startActivityNoHistory(Class<? extends Activity> activity) {
        final Intent intent = new Intent(this, activity);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
