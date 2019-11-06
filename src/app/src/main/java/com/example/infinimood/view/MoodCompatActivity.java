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

/**
 *  MoodCompatActivity.java
 *  Superclass for all other activities
 *  Contains common functionality for all activities
 */

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
        moodMap.put("socialSituation", mood.getSocialSituation());
        moodMap.put("reason", mood.getReason());
        moodMap.put("date", dateFormat.format(mood.getDate()));
        moodMap.put("timestamp", mood.getTime());
        if (mood.getLocation() != null) {
            moodMap.put("location", locationToString( mood.getLocation() ) );
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

    public String locationToString( Location location )
    {
        return String.valueOf( location.getLatitude() ).concat(",").concat( String.valueOf( location.getLongitude() ) );
    }

    public Mood createMood(String id, String mood, Date moodDate, String moodReason, Location moodLocation, String moodSocialSituation, Image moodImage) {
        Mood newMood;

        switch (mood) {
            case "Happy":
                newMood = new HappyMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "Angry":
                newMood = new AngryMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "Crying":
                newMood = new CryingMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "In Love":
                newMood = new InLoveMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "Sad":
                newMood = new SadMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "Sleepy":
                newMood = new SleepyMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "Afraid":
                newMood = new AfraidMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
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
                                String moodEmotion = (String) document.get("mood");
                                String id = (String) document.get("id");
                                String dateString = (String) document.get("date");
                                String reason = (String) document.get("reason");
                                String locationString = (String) document.get("location");
                                String socialSituation = (String) document.get("socialSituation");
                                String imageString = (String) document.get("image");

                                Date date;
                                try {
                                    date = dateFormat.parse(dateString);
                                } catch (java.text.ParseException e) {
                                    date = null;
                                    e.printStackTrace();
                                }

                                Location l = new Location("dummy provider");
                                String[] location = locationString.split(",");
                                l.setLatitude( Double.parseDouble(location[0] ) );
                                l.setLongitude( Double.parseDouble(location[1]) );

                                Log.i("", "updating moods");
                                Log.i("", "location: ");
                                Log.i("", String.valueOf( l.getLatitude() ));
                                Log.i("", String.valueOf( l.getLongitude() ));

                                Mood mood = createMood(id, moodEmotion, date, reason, l, socialSituation, null);

                                moods.add(mood);
                            }
                        } else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });
    }

}
