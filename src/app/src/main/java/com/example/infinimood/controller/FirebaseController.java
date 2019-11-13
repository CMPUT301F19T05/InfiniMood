package com.example.infinimood.controller;


import android.location.Location;
import android.media.Image;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.example.infinimood.model.AfraidMood;
import com.example.infinimood.model.AngryMood;
import com.example.infinimood.model.CryingMood;
import com.example.infinimood.model.HappyMood;
import com.example.infinimood.model.InLoveMood;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodComparator;
import com.example.infinimood.model.SadMood;
import com.example.infinimood.model.SleepyMood;
import com.example.infinimood.model.User;
import com.example.infinimood.view.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *  FirebaseController.java
 *  Handles firebase-related functionality
 */
public class FirebaseController {

    private static final String TAG = "FirebaseController";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    private MoodController moodController;

    private String username;

    private boolean accountCreationSuccessful;
    private boolean setUserDataSuccessful;
    private boolean signInSuccessful;


    public FirebaseController() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        moodController = new MoodController();
    }

    public boolean userAuthenticated() {
        return (firebaseUser != null);
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public String getUsername() {
        if (!userAuthenticated()) {
            return "Anonymous";
        }
        else {
            username = "Anonymous";
            firebaseFirestore.collection("users")
                    .document(firebaseUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot result = task.getResult();
                                if (result != null) {
                                    username = (String) result.get("username");
                                }
                            }
                        }
                    });
            return username;
        }
    }

    public boolean createUser(String email, String password) {
        accountCreationSuccessful = false;
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        accountCreationSuccessful = task.isSuccessful();
                    }
                });
        return accountCreationSuccessful;
    }

    public boolean setCurrentUserData(String username) {
        setUserDataSuccessful = false;

        firebaseUser = firebaseAuth.getCurrentUser();

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);

        firebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        setUserDataSuccessful = task.isSuccessful();
                    }
                });
        return setUserDataSuccessful;
    }

    public boolean signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        signInSuccessful = task.isSuccessful();
                    }
                });
        return signInSuccessful;
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
            moodMap.put("location", locationToString(mood.getLocation()));
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

    public void refreshUserMoods(ArrayList<Mood> moods) {
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

                                Date date = null;
                                try {
                                    date = dateFormat.parse(dateString);
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }

                                Location l = null;
                                if (locationString != null) {
                                    l = new Location("dummy provider");
                                    String[] location = locationString.split(",");
                                    l.setLatitude(Double.parseDouble(location[0]));
                                    l.setLongitude(Double.parseDouble(location[1]));
                                }

                                Mood mood = moodController.createMood(id, moodEmotion, date, reason, l, socialSituation, null);

                                moods.add(mood);
                                MoodComparator comparator = new MoodComparator();
                                Collections.sort(moods, comparator);
                            }
                        } else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });
    }

    // find users with a username containing a substring

    // find users that the current user follows
    // find users who the current user has requested to follow

    // find users that follow the current user
    // find users who have requested to follow the current user



    public ArrayList<User> findUsersBySubstring(String newText) {
        ArrayList<User> matchingUsers = new ArrayList<User>();

        String uid = firebaseAuth.getUid();
        CollectionReference userCollection = firebaseFirestore.collection("users");
        userCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            matchingUsers.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userID = (String) document.getId();
                                String username = (String) document.get("username");
                                if (username.contains(newText) && !userID.equals(uid) && !newText.equals("")) {
                                    User user = new User(userID, username);
                                    matchingUsers.add(user);
                                    accepted.add(true);
                                }
                            }
                            update();
                        }
                        else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });
    }


    private String locationToString(Location location) {
        return String.valueOf(location.getLatitude()).concat(",").concat(String.valueOf(location.getLongitude()));
    }
}
