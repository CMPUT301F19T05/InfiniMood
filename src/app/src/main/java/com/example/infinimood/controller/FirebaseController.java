package com.example.infinimood.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodFactory;
import com.example.infinimood.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private FirebaseStorage firebaseStorage;

    private MoodFactory moodFactory = new MoodFactory();

    private Set<String> requestedFollowCurrentUser = new HashSet<String>();
    private Set<String> followingCurrentUser = new HashSet<String>();
    private Set<String> currentUserFollowing = new HashSet<String>();
    private Set<String> currentUserRequestedFollow = new HashSet<String>();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd - HH:mm a", Locale.getDefault());


    private boolean firebaseOperation1Successful;
    private boolean firebaseOperation2Successful;

    /**
     * FirebaseController
     * Base constructor for firebase controller
     */
    public FirebaseController() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    /**
     * userAuthenticated
     * Check validity of current user
     * @return if current user was successfully found in firebase
     */
    public boolean userAuthenticated() {
        firebaseUser = firebaseAuth.getCurrentUser();
        return (firebaseUser != null);
    }

    /**
     * signOut
     * signs out of current user
     */
    public void signOut() {
        assert (userAuthenticated());
        firebaseAuth.signOut();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    /**
     * getCurrentUID
     * @return String containing the current user's UID
     */
    public String getCurrentUID() {
        return firebaseAuth.getUid();
    }

    /**
     * getUsername
     * gets the current user's username
     * @param callback the string callback to be called when username successfully grabbed from
     *                 firebase
     */
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

    /**
     * addImageToDB
     * Method that serializes a bitmap and uploads it to firebase
     * @param mood The mood related to the image, for filename setting
     * @param bitmap  Bitmap of the image in question
     * @param callback A boolean callback to indicate success or failure of the add to DB
     */
    public void addImageToDB(Mood mood, Bitmap bitmap, BooleanCallback callback) {
        assert (userAuthenticated());

        String filename = "images" + '/' + firebaseUser.getUid() + '/' + mood.getId();

        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference imageRef = storageRef.child(filename);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        UploadTask uploadTask = imageRef.putBytes(data, metadata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "Image upload failed");
                callback.onCallback(false);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Log.i(TAG, "Image upload successful");
                callback.onCallback(true);
            }
        });
    }

    /**
     * getMoodImageFromDB
     * Gets a mood's image from Firebase
     * @param mood The mood whose image is requested
     * @param callback The Bitmap callback with the loaded image
     */
    public void getMoodImageFromDB(Mood mood, BitmapCallback callback) {
        assert (userAuthenticated());

        String filename = "images" + '/' + firebaseUser.getUid() + '/' + mood.getId();

        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference imageRef = storageRef.child(filename);

        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    Log.i(TAG, "Successfully downloaded image");
                    callback.onCallback(bitmap);
                } else {
                    Log.i(TAG, "Failed to download image");
                    callback.onCallback(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "Failed to download image");
                callback.onCallback(null);
            }
        });
    }

    /**
     * deleteMoodImageFromDB
     * Method that deletes a mood's image from firebase
     * @param mood the mood whose image we wnt to delete
     * @param callback a boolean callback indicating success or failure
     */
    public void deleteMoodImageFromDB(Mood mood, BooleanCallback callback) {
        assert (userAuthenticated());

        String filename = "images" + '/' + firebaseUser.getUid() + '/' + mood.getId();

        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference imageRef = storageRef.child(filename);

        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "Successfully deleted image");
                callback.onCallback(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to delete image");
                callback.onCallback(false);
            }
        });
    }

    /**
     * addMoodEvenToDB
     * Method to add mood to Firebase
     * @param mood the mood to add to firebase
     * @param callback a boolean callback indicating success or failure
     */
    public void addMoodEventToDB(Mood mood, BooleanCallback callback) {
        assert (userAuthenticated());

        String uid = firebaseUser.getUid();

        Map<String, Object> moodMap = new HashMap<>();

        moodMap.put("id", mood.getId());
        moodMap.put("mood", mood.getMood());
        moodMap.put("socialSituation", mood.getSocialSituation());
        moodMap.put("reason", mood.getReason());
        moodMap.put("date", mood.getDate());
        moodMap.put("hasImage", String.valueOf(mood.hasImage()));
        if (mood.getLocation() != null) {
            moodMap.put("location", locationToString(mood.getLocation()));
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

    /**
     * deleteMoodEventFromDB
     * Method to delete a mood from firebase
     * @param mood The mood to delete
     * @param callback a boolean callback indicating success or failure
     */
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
                        deleteMoodImageFromDB(mood, new BooleanCallback() {
                            @Override
                            public void onCallback(boolean bool) {
                                callback.onCallback(true);
                            }
                        });
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

    /**
     * refreshMood
     * Get a mood's most information from firebase
     * @param mood the mood whose information we're querying
     * @param callback a mood callback to be called with the mood we're requesting
     */
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
                            boolean hasImage = Boolean.valueOf((String) document.get("hasImage"));

                            Location l = null;
                            if (locationString != null) {
                                l = new Location("dummy provider");
                                String[] location = locationString.split(",");
                                l.setLatitude(Double.parseDouble(location[0]));
                                l.setLongitude(Double.parseDouble(location[1]));
                            }

                            Mood mood = moodFactory.createMood(id, moodEmotion, dateTimestamp, reason, l, socialSituation, hasImage);

                            callback.onCallback(mood);
                        } else {
                            Log.e(TAG, "Error getting document");
                        }
                    }
                });
    }

    /**
     * refreshUserMoods
     * Method that gets all the current user's moods from firebase
     * @param callback Moods callback that will be called with an ArrayList of all the user's moods
     */
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
                                boolean hasImage = Boolean.valueOf((String) document.get("hasImage"));

                                Location l = null;
                                if (locationString != null) {
                                    l = new Location("dummy provider");
                                    String[] location = locationString.split(",");
                                    l.setLatitude(Double.parseDouble(location[0]));
                                    l.setLongitude(Double.parseDouble(location[1]));
                                }

                                Mood mood = moodFactory.createMood(id, moodEmotion, dateTimestamp, reason, l, socialSituation, hasImage);

                                moods.add(mood);
                            }
                            callback.onCallback(moods);
                        } else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });
    }

    /**
     * refreshOtherUserMoods
     * Method that gets a specific user's moods
     * @param user The user whose mood's we want
     * @param callback A moods callback that will be called with the specified user's moods
     */
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
                                boolean hasImage = Boolean.valueOf((String) document.get("hasImage"));

                                Location l = null;
                                if (locationString != null) {
                                    l = new Location("dummy provider");
                                    String[] location = locationString.split(",");
                                    l.setLatitude(Double.parseDouble(location[0]));
                                    l.setLongitude(Double.parseDouble(location[1]));
                                }

                                Mood mood = moodFactory.createMood(id, moodEmotion, dateTimestamp, reason, l, socialSituation, hasImage);

                                otherUserMoods.add(mood);
                            }
                            callback.onCallback(otherUserMoods);
                        } else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });
    }

    /**
     * getUsers
     * Get a list of all users not including the currently logged in user from firebase
     * @param callback A user callback that will be called with an ArrayList of all users except
     *                 the currently logged in user
     */
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

    /**
     * requestToFollow
     * Method that sends a follow request to another user through firebase
     * @param user The user to send the follow request to
     * @param callback a boolean callback that indicates success or failure
     */
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

    /**
     * declineFollowRequest
     * Method for updating firebase with the information that a certain incoming follow request
     * was denied, removing the currently logged in user from the requesting user's following list
     * @param user The user's whose follow request is being denied
     * @param callback a boolean callback indicating success or failure
     */
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

    /**
     * acceptFollowRequest
     * Method for updating firebase with the information that the logged in user has accepted a
     * follow request from a certain user, updating the following collection of the requesting
     * user, and the followers collection of the currently logged in user accordingly
     * @param user The user whose follow request is being accepted
     * @param callback a boolean callback indicating success or failure
     */
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

    /**
     * unfollowUser
     * A method for updating firebase with the information that the currently logged in user has
     * unfollowed a certain user.
     * @param user The user who is being unfollowed
     * @param callback A boolean callback indicating success or failure
     */
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
}
