package com.example.infinimood.view;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infinimood.model.Mood;
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

    public void addMoodEventToDB(Mood mood) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        Map<String, Object> moodMap = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd - hh:mm a", Locale.getDefault());

        moodMap.put("id", mood.getId());
        moodMap.put("mood", mood.getMood());
        moodMap.put("social_situation", mood.getSocial_situation());
        moodMap.put("reason", mood.getReason());
        moodMap.put("date", dateFormat.format(mood.getDate()));
        moodMap.put("timestamp", System.currentTimeMillis() / 1000L);
        if (mood.getLocation() != null) {
            moodMap.put("location", mood.getLocation().toString());
        }
        if (mood.getImage() != null) {
            moodMap.put("image", mood.getImage());
        }

        firebaseFirestore
                .collection("users")
                .document(uid)
                .collection("moods")
                .document(mood.getId())
                .set(moodMap)
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
}
