package com.example.infinimood.view;

import android.content.Intent;
import android.location.Location;
import android.media.Image;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infinimood.model.AfraidMood;
import com.example.infinimood.model.AngryMood;
import com.example.infinimood.model.CryingMood;
import com.example.infinimood.model.HappyMood;
import com.example.infinimood.model.InLoveMood;
import com.example.infinimood.model.Mood;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class MoodCompatActivity extends AppCompatActivity {

    private static final String TAG = "MoodCompatActivity";
    protected static ArrayList<Mood> moods = new ArrayList<>();
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

        if (firebaseUser != null) {
            refreshUserMoods();
        }
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
        moodMap.put("timestamp", mood.getTime());
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

    public Mood createMood(String id, String mood, Date mood_date, String mood_reason, Location mood_location, String mood_social_situation, Image mood_image) {
        Mood newMood;

        switch (mood) {
            case "Happy":
                newMood = new HappyMood(id, mood_date, mood_reason, mood_location, mood_social_situation, mood_image);
                break;
            case "Angry":
                newMood = new AngryMood(id, mood_date, mood_reason, mood_location, mood_social_situation, mood_image);
                break;
            case "Crying":
                newMood = new CryingMood(id, mood_date, mood_reason, mood_location, mood_social_situation, mood_image);
                break;
            case "In Love":
                newMood = new InLoveMood(id, mood_date, mood_reason, mood_location, mood_social_situation, mood_image);
                break;
            case "Sad":
                newMood = new SadMood(id, mood_date, mood_reason, mood_location, mood_social_situation, mood_image);
                break;
            case "Sleepy":
                newMood = new SleepyMood(id, mood_date, mood_reason, mood_location, mood_social_situation, mood_image);
                break;
            case "Afraid":
                newMood = new AfraidMood(id, mood_date, mood_reason, mood_location, mood_social_situation, mood_image);
                break;
            default:
                Log.e(TAG, "Default case in addEdit switch: " + mood);
                throw new IllegalArgumentException("Invalid mood in createMood");
        }
        return newMood;
    }

    public void refreshUserMoods() {
        if (firebaseUser == null) {
            return;
        }

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd - hh:mm a", Locale.getDefault());

        CollectionReference moodCollection = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("moods");

        moodCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            moods.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String mood_emotion = (String) document.get("mood");
                                String id = (String) document.get("id");
                                String date_string = (String) document.get("date");
                                String reason = (String) document.get("reason");
                                String location_string = (String) document.get("location");
                                String social_situation = (String) document.get("social_situation");
                                String image_string = (String) document.get("image");

                                Date date;
                                try {
                                    date = dateFormat.parse(date_string);
                                } catch (java.text.ParseException e) {
                                    date = null;
                                    e.printStackTrace();
                                }

                                Mood mood = createMood(id, mood_emotion, date, reason, null, social_situation, null);
                                moods.add(mood);
                            }
                        } else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });
    }

}
