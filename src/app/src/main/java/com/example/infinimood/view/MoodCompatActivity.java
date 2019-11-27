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

    // Request codes
    protected static final int ADD_MOOD = 3;
    protected static final int EDIT_MOOD = 4;

    protected static ArrayList<Mood> moods = new ArrayList<>();
    protected static ArrayList<Mood> otherUserMoods = new ArrayList<>();

    protected static FirebaseController firebaseController = new FirebaseController();
    protected static MoodFactory moodFactory = new MoodFactory();

    /**
     * onStart
     * Overrides onStart. Is run on start.
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * toast
     * Displays a message
     * @param msg String - message to be displayed
     */
    public void toast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * toast
     * Dissplay a number
     * @param id int - number to display
     */
    public void toast(int id) {
        toast(getString(id));
    }

    /**
     * startActivityWithHistory
     * Starts an activity
     * @param activity Activity to start
     */
    public void startActivityWithHistory(Class<? extends Activity> activity) {
        final Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    /**
     * startActivityWithNoHistory
     * Starts an activity with no prior knowledge
     * @param activity Activity to start
     */
    public void startActivityNoHistory(Class<? extends Activity> activity) {
        final Intent intent = new Intent(this, activity);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * refreshAuth
     * Ensure user is authorized
     */
    public void refreshAuth() {
        // TODO: Temporary
        firebaseController.userAuthenticated();
    }

}
