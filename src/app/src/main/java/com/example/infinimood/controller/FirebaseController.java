package com.example.infinimood.controller;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodFactory;
import com.example.infinimood.model.User;
import com.example.infinimood.view.CreateAccountActivity;
import com.example.infinimood.view.MoodCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * FirebaseController.java
 * Handles firebase-related functionality
 */
public class FirebaseController {

    private static final String TAG = "FirebaseController";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser = null;

    private MoodFactory moodFactory = new MoodFactory();

    private Set<String> requestedFollowCurrentUser = new HashSet<String>();
    private Set<String> followingCurrentUser = new HashSet<String>();
    private Set<String> currentUserFollowing = new HashSet<String>();
    private Set<String> currentUserRequestedFollow = new HashSet<String>();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd - HH:mm a", Locale.getDefault());


    private boolean firebaseOperation1Successful;
    private boolean firebaseOperation2Successful;


    public FirebaseController() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public boolean userAuthenticated() {
        firebaseUser = firebaseAuth.getCurrentUser();
        return (firebaseUser != null);
    }

    public void signOut() {
        assert (userAuthenticated());
        firebaseAuth.signOut();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public String getCurrentUID() {
        return firebaseAuth.getUid();
    }

    public void getUsername(StringCallback callback) {
        assert (userAuthenticated());

        firebaseFirestore.collection("users")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot result = task.getResult();
                            if (result != null) {
                                callback.onCallback((String) result.get("username"));
                            }
                        } else {
                            Log.e(TAG, "Getting username failed");
                        }
                    }
                });
    }

    public void createUser(Context context, String newUsername, String email, String password, BooleanCallback callback) {
        firebaseFirestore
                .collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String username = (String) document.get("username");
                                if (newUsername.equals(username)) {
                                    callback.onCallback(false);
                                    ((CreateAccountActivity) context).toast(R.string.error_username_taken);
                                }
                            }
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            firebaseUser = firebaseAuth.getCurrentUser();
                                            if (task.isSuccessful()) {
                                                callback.onCallback(true);
                                            } else {
                                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                callback.onCallback(false);

                                                try {
                                                    throw task.getException();
                                                }
                                                // if user enters wrong email.
                                                catch (FirebaseAuthWeakPasswordException weakPassword) {
                                                    Log.d(TAG, "onComplete: weak_password");
                                                    ((CreateAccountActivity) context).toast(R.string.error_password_too_short);
                                                }
                                                // if user enters wrong password.
                                                catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                                    Log.d(TAG, "onComplete: malformed_email");
                                                    ((CreateAccountActivity) context).toast(R.string.error_email_invalid);
                                                } catch (FirebaseAuthUserCollisionException existEmail) {
                                                    Log.d(TAG, "onComplete: exist_email");
                                                    ((CreateAccountActivity) context).toast(R.string.error_email_taken);
                                                } catch (Exception e) {
                                                    Log.d(TAG, "onComplete: " + e.getMessage());
                                                }
                                            }
                                        }
                                    });
                        } else {
                            ((CreateAccountActivity) context).toast("Failed creating account");
                            callback.onCallback(false);
                        }
                    }
                });
    }

    public void setCurrentUserData(String username, BooleanCallback callback) {
        assert (userAuthenticated());

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);

        firebaseFirestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful());
                    }
                });
    }

    public void signIn(Context context, String email, String password, BooleanCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        firebaseUser = firebaseAuth.getCurrentUser();
                        if (task.isSuccessful()) {
                            callback.onCallback(true);
                        } else {
                            callback.onCallback(false);
                            try {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthInvalidUserException invalidEmail) {
                                Log.d(TAG, "onComplete: invalid_email");
                                ((MoodCompatActivity) context).toast("Invalid email");
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                Log.d(TAG, "onComplete: wrong_password");
                                ((MoodCompatActivity) context).toast("Incorrect password");
                            } catch (Exception e) {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                                ((MoodCompatActivity) context).toast(R.string.login_failed);
                            }
                        }
                    }
                });
    }

    public void addMoodEventToDB(Mood mood, BooleanCallback callback) {
        assert (userAuthenticated());

        String uid = firebaseUser.getUid();

        Map<String, Object> moodMap = new HashMap<>();

        moodMap.put("id", mood.getId());
        moodMap.put("mood", mood.getMood());
        moodMap.put("socialSituation", mood.getSocialSituation());
        moodMap.put("reason", mood.getReason());
        moodMap.put("date", mood.getDate());
        if (mood.getLocation() != null) {
            moodMap.put("location", locationToString(mood.getLocation()));
        }
        if (mood.getImage() != null) {
            moodMap.put("image", convertBitmapToString(mood.getImage()));
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
                        callback.onCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        callback.onCallback(false);
                    }
                });
    }

    public void deleteMoodEventFromDB(Mood mood, BooleanCallback callback) {
        assert (userAuthenticated());

        final String uid = firebaseUser.getUid();

        firebaseFirestore
                .collection("users")
                .document(uid)
                .collection("moods")
                .document(mood.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully deleted mood");
                        callback.onCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting mood", e);
                        callback.onCallback(false);
                    }
                });
    }

    public void refreshMood(Mood mood, GetMoodCallback callback) {
        assert (userAuthenticated());

        firebaseFirestore
                .collection("users")
                .document(firebaseUser.getUid())
                .collection("moods")
                .document(mood.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            String moodEmotion = (String) document.get("mood");
                            String id = (String) document.get("id");

                            long dateTimestamp = (long) document.get("date");

                            String reason = (String) document.get("reason");
                            String locationString = (String) document.get("location");
                            String socialSituation = (String) document.get("socialSituation");
                            String imageString = (String) document.get("image");

                            Location l = null;
                            if (locationString != null) {
                                l = new Location("dummy provider");
                                String[] location = locationString.split(",");
                                l.setLatitude(Double.parseDouble(location[0]));
                                l.setLongitude(Double.parseDouble(location[1]));
                            }

                            Bitmap image = null;
                            if (imageString != null) {
                                image = convertStringToBitmap(imageString);
                            }

                            Mood mood = moodFactory.createMood(id, moodEmotion, dateTimestamp, reason, l, socialSituation, image);

                            callback.onCallback(mood);
                        } else {
                            Log.e(TAG, "Error getting document");
                        }
                    }
                });
    }

    public void refreshUserMoods(GetMoodsCallback callback) {
        assert (userAuthenticated());

        firebaseFirestore
                .collection("users")
                .document(firebaseUser.getUid())
                .collection("moods")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Mood> moods = new ArrayList<Mood>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String moodEmotion = (String) document.get("mood");
                                String id = (String) document.get("id");

                                long dateTimestamp = (long) document.get("date");

                                String reason = (String) document.get("reason");
                                String locationString = (String) document.get("location");
                                String socialSituation = (String) document.get("socialSituation");
                                String imageString = (String) document.get("image");

                                Location l = null;
                                if (locationString != null) {
                                    l = new Location("dummy provider");
                                    String[] location = locationString.split(",");
                                    l.setLatitude(Double.parseDouble(location[0]));
                                    l.setLongitude(Double.parseDouble(location[1]));
                                }

                                Bitmap image = null;
                                if (imageString != null) {
                                    image = convertStringToBitmap(imageString);
                                }

                                Mood mood = moodFactory.createMood(id, moodEmotion, dateTimestamp, reason, l, socialSituation, image);

                                moods.add(mood);
                            }
                            callback.onCallback(moods);
                        } else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });
    }

    public void refreshOtherUserMoods(User user, GetMoodsCallback callback) {

        firebaseFirestore
                .collection("users")
                .document(user.getUserID())
                .collection("moods")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Mood> otherUserMoods = new ArrayList<Mood>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String moodEmotion = (String) document.get("mood");
                                String id = (String) document.get("id");

                                long dateTimestamp = (long) document.get("date");

                                String reason = (String) document.get("reason");
                                String locationString = (String) document.get("location");
                                String socialSituation = (String) document.get("socialSituation");
                                String imageString = (String) document.get("image");

                                Location l = null;
                                if (locationString != null) {
                                    l = new Location("dummy provider");
                                    String[] location = locationString.split(",");
                                    l.setLatitude(Double.parseDouble(location[0]));
                                    l.setLongitude(Double.parseDouble(location[1]));
                                }

                                Bitmap image = null;
                                if (imageString != null) {
                                    image = convertStringToBitmap(imageString);
                                }

                                Mood mood = moodFactory.createMood(id, moodEmotion, dateTimestamp, reason, l, socialSituation, image);

                                otherUserMoods.add(mood);
                            }
                            callback.onCallback(otherUserMoods);
                        } else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });
    }

    public void getUsers(GetUsersCallback callback) {
        assert (userAuthenticated());

        ArrayList<User> users = new ArrayList<User>();
        String currentUserId = firebaseUser.getUid();

        getFollowing(new BooleanCallback() {
            @Override
            public void onCallback(boolean success) {
                if (success) {
                    getFollowers(new BooleanCallback() {
                        @Override
                        public void onCallback(boolean success) {
                            if (success) {
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
                                                        if (!userId.equals(currentUserId)) {
                                                            users.add(user);
                                                        }

                                                    }
                                                    callback.onCallback(users);
                                                } else {
                                                    Log.e(TAG, "Error getting documents");
                                                }
                                            }
                                        });
                            }
                        }
                    });
                }
            }
        });
    }

    public void requestToFollow(User user, BooleanCallback callback) {
        assert (userAuthenticated());

        // make sure the current user doesn't already follow them
        // and the current user hasn't already requested to follow them
        assert (!user.isCurrentUserFollows());
        assert (!user.isCurrentUserRequestedFollow());

        String followerId = firebaseAuth.getUid();
        String followeeId = user.getUserID();

        // make sure we're not trying to follow ourself
        assert (!followerId.equals(followeeId));

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
                        if (task.isSuccessful()) {
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
                                            if (task.isSuccessful()) {
                                                callback.onCallback(true);
                                            } else {
                                                Log.e(TAG, "write failed");
                                                callback.onCallback(false);
                                            }
                                        }
                                    });
                        } else {
                            Log.e(TAG, "write failed");
                            callback.onCallback(false);
                        }
                    }
                });
    }

    public void declineFollowRequest(User user, BooleanCallback callback) {
        assert (userAuthenticated());

        // make sure that they requested to follow the current user
        // and that they aren't already following the current user
        assert (user.isRequestedFollowCurrentUser());
        assert (!user.isFollowsCurrentUser());

        String followerId = user.getUserID();
        String followeeId = firebaseAuth.getUid();

        assert (!followerId.equals(followeeId));

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
                        if (task.isSuccessful()) {
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
                                            if (task.isSuccessful()) {
                                                callback.onCallback(true);
                                            } else {
                                                Log.e(TAG, "delete failed");
                                                callback.onCallback(false);
                                            }
                                        }
                                    });
                        } else {
                            Log.e(TAG, "delete failed");
                            callback.onCallback(false);
                        }
                    }
                });
    }

    public void acceptFollowRequest(User user, BooleanCallback callback) {
        assert (userAuthenticated());

        // make sure that they requested to follow the current user
        // and that they aren't already following the current user
        assert (user.isRequestedFollowCurrentUser());
        assert (!user.isFollowsCurrentUser());

        String followerId = user.getUserID();
        String followeeId = firebaseAuth.getUid();

        assert (!followerId.equals(followeeId));

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
                        if (task.isSuccessful()) {
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
                                            if (task.isSuccessful()) {
                                                callback.onCallback(true);
                                            } else {
                                                Log.e(TAG, "write failed");
                                                callback.onCallback(false);
                                            }
                                        }
                                    });
                        } else {
                            Log.e(TAG, "write failed");
                            callback.onCallback(false);
                        }
                    }
                });
    }

    public void unfollowUser(User user, BooleanCallback callback) {
        assert (userAuthenticated());

        // make sure that the current user follows them
        assert (user.isCurrentUserFollows());
        assert (!user.isCurrentUserRequestedFollow());

        String followerId = firebaseAuth.getUid();
        String followeeId = user.getUserID();

        assert (!followerId.equals(followeeId));

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
                        if (task.isSuccessful()) {
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
                                            if (task.isSuccessful()) {
                                                callback.onCallback(true);
                                            } else {
                                                Log.e(TAG, "delete failed");
                                                callback.onCallback(false);
                                            }
                                        }
                                    });
                        } else {
                            Log.e(TAG, "delete failed");
                            callback.onCallback(false);
                        }
                    }
                });
    }

    private void getFollowing(BooleanCallback callback) {
        assert (userAuthenticated());

        currentUserFollowing.clear();
        currentUserRequestedFollow.clear();

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
                                } else {
                                    currentUserRequestedFollow.add(followeeId);
                                }
                            }
                            callback.onCallback(true);
                        } else {
                            Log.e(TAG, "Error getting documents");
                            callback.onCallback(false);
                        }
                    }
                });
    }

    private void getFollowers(BooleanCallback callback) {
        assert (userAuthenticated());

        followingCurrentUser.clear();
        requestedFollowCurrentUser.clear();

        String currentUserId = firebaseUser.getUid();

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
                                } else {
                                    requestedFollowCurrentUser.add(followerId);
                                }
                            }
                            callback.onCallback(true);
                        } else {
                            Log.e(TAG, "Error getting documents");
                            callback.onCallback(false);
                        }
                    }
                });
    }

    private String locationToString(Location location) {
        return String.valueOf(location.getLatitude()).concat(",").concat(String.valueOf(location.getLongitude()));
    }

    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    private Bitmap convertStringToBitmap(String encoded) {
        try {
            byte[] encodedByte = Base64.decode(encoded, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodedByte, 0,
                    encodedByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
