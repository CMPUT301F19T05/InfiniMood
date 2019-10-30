package com.example.infinimood.view;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MoodCompatActivity extends AppCompatActivity {

    private static final String TAG = "MoodCompatActivity";

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

    public void addMoodEventToDB(String mood_id, String mood_emotion, String mood_social_situation, String mood_reason, Date mood_date) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        Map<String, Object> mood = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd - hh:mm a", Locale.getDefault());

        mood.put("id", mood_id);
        mood.put("mood", mood_emotion);
        mood.put("social_situation", mood_social_situation);
        mood.put("reason", mood_reason);
        mood.put("date", dateFormat.format(mood_date));
        mood.put("timestamp", dateFormat.format(new Date()));

        firebaseFirestore
                .collection("users")
                .document(uid)
                .collection("moods")
                .document(mood_id)
                .set(mood)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    // implement this
    public void removeMoodEventFromDB(String mood_id) {};

}
