package com.example.infinimood.controller;


import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodComparator;
import com.example.infinimood.model.User;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 *  FirebaseController.java
 *  Handles firebase-related functionality
 */
public class FirebaseController {

    private static final String TAG = "FirebaseController";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser = null;

    private MoodController moodController;

    private String username;

    private boolean accountCreationSuccessful;
    private boolean setUserDataSuccessful;
    private boolean signInSuccessful;

    private boolean firebaseOperation1Successful;
    private boolean firebaseOperation2Successful;

    private Set<String> requestedFollowCurrentUser;
    private Set<String> followingCurrentUser;
    private Set<String> currentUserFollowing;
    private Set<String> currentUserRequestedFollow;
    private ArrayList<User> users;


    public FirebaseController() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        moodController = new MoodController();

        users = new ArrayList<User>();
    }

    public boolean userAuthenticated() {
        firebaseUser = firebaseAuth.getCurrentUser();
        return (firebaseUser != null);
    }

    public void signOut() {
        assert(userAuthenticated());
        firebaseAuth.signOut();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void fetchUsername() {
        firebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Getting username successful");
                            DocumentSnapshot result = task.getResult();
                            if (result != null) {
                                username = (String) result.get("username");
                            }
                        }
                        else {
                            Log.e(TAG, "Getting username failed");
                        }
                    }
                });
    }

    public String getUsername() {
        assert(userAuthenticated());
        fetchUsername();
        return username;
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
        firebaseUser = firebaseAuth.getCurrentUser();
        return accountCreationSuccessful;
    }

    public boolean setCurrentUserData(String username) {
        assert(userAuthenticated());

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
        firebaseUser = firebaseAuth.getCurrentUser();
        return signInSuccessful;
    }

    public void addMoodEventToDB(Mood mood) {
        assert(userAuthenticated());

        String uid = firebaseUser.getUid();

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
        assert(userAuthenticated());

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd - HH:mm a", Locale.getDefault());

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

                                Log.i(TAG, dateString);

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
                        }
                        else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });
    }

    public ArrayList<User> getUsers() {
        assert(userAuthenticated());

        requestedFollowCurrentUser.clear();
        followingCurrentUser.clear();
        currentUserFollowing.clear();
        currentUserRequestedFollow.clear();
        users.clear();

        String currentUserId = firebaseUser.getUid();

        // get users that the current user has requested to follow or follows
        firebaseFirestore
                .collection("users")
                .document(currentUserId)
                .collection("following")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String followeeId = (String) document.get("followee_id");
                                boolean accepted = (boolean) document.get("accepted");
                                if (accepted) {
                                    currentUserFollowing.add(followeeId);
                                }
                                else {
                                    currentUserRequestedFollow.add(followeeId);
                                }
                            }
                        }
                        else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });

        // get users who follow the current user or have requested to follow the current user
        firebaseFirestore
                .collection("users")
                .document(currentUserId)
                .collection("followers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String followerId = (String) document.get("follower_id");
                                boolean accepted = (boolean) document.get("accepted");
                                if (accepted) {
                                    followingCurrentUser.add(followerId);
                                }
                                else {
                                    requestedFollowCurrentUser.add(followerId);
                                }
                            }
                        }
                        else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });

        // get users
        firebaseFirestore
                .collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String username = (String) document.get("username");
                                String userId = (String) document.getId();
                                boolean currentUserFollows = currentUserFollowing.contains(userId);
                                boolean currentUserRequestedToFollow = currentUserRequestedFollow.contains(userId);
                                boolean followsCurrentUser = followingCurrentUser.contains(userId);
                                boolean requestedToFollowCurrentUser = requestedFollowCurrentUser.contains(userId);
                                User user = new User(userId, username, currentUserFollows, currentUserRequestedToFollow, followsCurrentUser, requestedToFollowCurrentUser);
                                users.add(user);
                            }
                        }
                        else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });

        return users;
    }

    public boolean requestToFollow(User user) {
        assert(userAuthenticated());

        // make sure the current user doesn't already follow them
        // and the current user hasn't already requested to follow them
        assert(!user.isCurrentUserFollows());
        assert(!user.isCurrentUserRequestedFollow());

        String followerId = firebaseAuth.getUid();
        String followeeId = user.getUserID();

        // make sure we're not trying to follow ourself
        assert(!followerId.equals(followeeId));

        // the document to write to firestore
        Map<String, Object> followData = new HashMap<>();
        followData.put("follower_id", followerId);
        followData.put("followee_id", followeeId);
        followData.put("accepted", false);

        // write to our following collection
        firebaseFirestore
                .collection("users")
                .document(followerId)
                .collection("following")
                .document(followeeId)
                .set(followData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseOperation1Successful = task.isSuccessful();
                    }
                });

        // write to their followers collection
        firebaseFirestore
                .collection("users")
                .document(followeeId)
                .collection("followers")
                .document(followerId)
                .set(followData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseOperation2Successful = task.isSuccessful();
                    }
                });

        // bad
        if (!firebaseOperation1Successful && !firebaseOperation2Successful) {
            Log.e(TAG, "Both writes failed");
            return false;
        }

        // really bad
        if (firebaseOperation1Successful != firebaseOperation2Successful) {
            Log.e(TAG, "One write successful, one failed");
            return false;
        }

        // success
        else {
            Log.i(TAG, "Follow request successful");
            user.setCurrentUserRequestedFollow(true);
            return true;
        }
    }

    public boolean declineFollowRequest(User user) {
        assert(userAuthenticated());

        // make sure that they requested to follow the current user
        // and that they aren't already following the current user
        assert(user.isRequestedFollowCurrentUser());
        assert(!user.isFollowsCurrentUser());

        String followerId = user.getUserID();
        String followeeId = firebaseAuth.getUid();

        assert(!followerId.equals(followeeId));

        // delete from their following collection
        firebaseFirestore
                .collection("users")
                .document(followerId)
                .collection("following")
                .document(followeeId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseOperation1Successful = task.isSuccessful();
                    }
                });

        // delete from our followers collection
        firebaseFirestore
                .collection("users")
                .document(followeeId)
                .collection("followers")
                .document(followerId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseOperation2Successful = task.isSuccessful();
                    }
                });

        // bad
        if (!firebaseOperation1Successful && !firebaseOperation2Successful) {
            Log.e(TAG, "Both deletes failed");
            return false;
        }

        // really bad
        if (firebaseOperation1Successful != firebaseOperation2Successful) {
            Log.e(TAG, "One delete successful, one failed");
            return false;
        }

        // success
        else {
            Log.i(TAG, "Decline follow request successful");
            user.setRequestedFollowCurrentUser(false);
            user.setFollowsCurrentUser(true);
            return true;
        }
    }

    public boolean acceptFollowRequest(User user) {
        assert(userAuthenticated());

        // make sure that they requested to follow the current user
        // and that they aren't already following the current user
        assert(user.isRequestedFollowCurrentUser());
        assert(!user.isFollowsCurrentUser());

        String followerId = user.getUserID();
        String followeeId = firebaseAuth.getUid();

        assert(!followerId.equals(followeeId));

        // the document to write to firestore
        Map<String, Object> followData = new HashMap<>();
        followData.put("follower_id", followerId);
        followData.put("followee_id", followeeId);
        followData.put("accepted", true);

        // write to their following collection
        firebaseFirestore
                .collection("users")
                .document(followerId)
                .collection("following")
                .document(followeeId)
                .set(followData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseOperation1Successful = task.isSuccessful();
                    }
                });

        // write to our followers collection
        firebaseFirestore
                .collection("users")
                .document(followeeId)
                .collection("followers")
                .document(followerId)
                .set(followData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseOperation2Successful = task.isSuccessful();
                    }
                });

        // bad
        if (!firebaseOperation1Successful && !firebaseOperation2Successful) {
            Log.e(TAG, "Both writes failed");
            return false;
        }

        // really bad
        if (firebaseOperation1Successful != firebaseOperation2Successful) {
            Log.e(TAG, "One write successful, one failed");
            return false;
        }

        // success
        else {
            Log.i(TAG, "Accept follow request successful");
            user.setRequestedFollowCurrentUser(false);
            return true;
        }
    }

    public boolean unfollowUser(User user) {
        assert(userAuthenticated());
        
        // make sure that the current user follows them
        assert(user.isCurrentUserFollows());
        assert(!user.isCurrentUserRequestedFollow());

        String followerId = firebaseAuth.getUid();
        String followeeId = user.getUserID();

        assert(!followerId.equals(followeeId));

        // delete from their followers collection
        firebaseFirestore
                .collection("users")
                .document(followeeId)
                .collection("followers")
                .document(followerId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseOperation1Successful = task.isSuccessful();
                    }
                });

        // delete from our following collection
        firebaseFirestore
                .collection("users")
                .document(followerId)
                .collection("following")
                .document(followeeId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseOperation2Successful = task.isSuccessful();
                    }
                });

        // bad
        if (!firebaseOperation1Successful && !firebaseOperation2Successful) {
            Log.e(TAG, "Both deletes failed");
            return false;
        }

        // really bad
        if (firebaseOperation1Successful != firebaseOperation2Successful) {
            Log.e(TAG, "One delete successful, one failed");
            return false;
        }

        // success
        else {
            Log.i(TAG, "Unfollow successful");
            user.setCurrentUserFollows(false);
            return true;
        }
    }

    private String locationToString(Location location) {
        return String.valueOf(location.getLatitude()).concat(",").concat(String.valueOf(location.getLongitude()));
    }
}
