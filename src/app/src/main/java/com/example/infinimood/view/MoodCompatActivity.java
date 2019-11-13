package com.example.infinimood.view;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.media.Image;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.controller.MoodController;
import com.example.infinimood.model.AfraidMood;
import com.example.infinimood.model.AngryMood;
import com.example.infinimood.model.CryingMood;
import com.example.infinimood.model.HappyMood;
import com.example.infinimood.model.InLoveMood;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodComparator;
import com.example.infinimood.model.SadMood;
import com.example.infinimood.model.SleepyMood;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * MoodCompatActivity.java
 * Superclass for all other activities
 * Contains common functionality for all activities
 */

public abstract class MoodCompatActivity extends AppCompatActivity {

    private static final String TAG = "MoodCompatActivity";

    protected static ArrayList<Mood> moods = new ArrayList<>();

    protected static FirebaseController firebaseController = new FirebaseController();
    protected static MoodController moodController = new MoodController();

    public MoodCompatActivity() {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseController.userAuthenticated()) {
            firebaseController.refreshUserMoods(moods);
        }
    }

    protected void toast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void toast(int id) {
        toast(getString(id));
    }

    protected void startActivityNoHistory(Class<? extends Activity> activity) {
        final Intent intent = new Intent(this, activity);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
