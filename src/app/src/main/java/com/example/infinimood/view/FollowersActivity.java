package com.example.infinimood.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.example.infinimood.controller.FollowAdapter;
import com.example.infinimood.controller.FollowingAdapter;
import com.example.infinimood.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.firestore.FieldValue.delete;

public class FollowersActivity extends MoodCompatActivity {

    private FollowAdapter followAdapter;
    private FollowingAdapter followingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_list);
        showFollowers();
    }

    public void showFollowers(){
        final ListView followingList = (ListView) findViewById(R.id.following_list);
        final ListView followerList = (ListView) findViewById(R.id.follower_list);
        update();
        followingList.setVisibility(View.INVISIBLE);
        followerList.setVisibility(View.VISIBLE);


    }

    public void showFollowing(){
        final ListView followingList = (ListView) findViewById(R.id.following_list);
        followingList.setVisibility(View.VISIBLE);
        final ListView followerList = (ListView) findViewById(R.id.follower_list);
        followerList.setVisibility(View.INVISIBLE);

        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<User> getFollowers, ArrayList<User> getFollowing,ArrayList<Boolean> AcceptedList) {
                followAdapter = new FollowAdapter(FollowersActivity.this,getFollowers, getFollowing,AcceptedList);
                followingAdapter = new FollowingAdapter(FollowersActivity.this,getFollowers,getFollowing);
                //ListView followerList = (ListView) findViewById(R.id.follower_list);
                followerList.setAdapter(followAdapter);
                followAdapter.notifyDataSetChanged();
                //ListView followingList = (ListView) findViewById(R.id.following_list);
                followingList.setAdapter(followingAdapter);
                followingAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onFollowersTabClicked(View view){
        showFollowers();
    }

    public void onFollowingTabClicked(View view){
        showFollowing();
    }



    public void readData(final FirebaseCallback firebaseCallback){
        final ArrayList<User> getFollowers = new ArrayList<>();
        final ArrayList<User> getFollowing = new ArrayList<>();
        final ArrayList<Boolean> AcceptedList = new ArrayList<>();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        firebaseFirestore
                .collection("users")
                .document(uid)
                .collection("followers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String getUserID = document.getString("FollowerID");
                                String getUsername = document.getString("FollowerUsername");
                                boolean Accepted = document.getBoolean("Accepted");
                                User getFollower = new User(getUserID,getUsername);
                                getFollowers.add(getFollower);
                                AcceptedList.add(Accepted);
                            }

                        } else {
                            Log.d("FollowersActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });

        firebaseFirestore
                .collection("users")
                .document(uid)
                .collection("following")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String getUserID = document.getString("FolloweeID");
                                String getUsername = document.getString("FolloweeUsername");
                                User getFollowee = new User(getUserID,getUsername);
                                getFollowing.add(getFollowee);
                            }
                            firebaseCallback.onCallback(getFollowers,getFollowing,AcceptedList);
                        } else {
                            Log.d("FollowersActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private interface FirebaseCallback{
        void onCallback(ArrayList<User> followers,ArrayList<User> following,ArrayList<Boolean> AcceptedList);
    }

    public void onUnfollowClicked(View view){
        ListView FollowingList = findViewById(R.id.following_list);
        View item = (View) view.getParent();
        int pos = FollowingList.getPositionForView(item);
        final User followee = followingAdapter.getItem(pos);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String uid = user.getUid();
        firebaseFirestore
                .collection("users")
                .document(uid)
                .collection("following")
                .document(followee.getUserID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Deleting Data", "DocumentSnapshot successfully deleted!");
                        showFollowing();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Deleting Data", "Error deleting document", e);
                    }
                });
        firebaseFirestore
                .collection("users")
                .document(followee.getUserID())
                .collection("followers")
                .document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Deleting Data", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Deleting Data", "Error deleting document", e);
                    }
                });

    }


    public void onFollowClicked(View view) {
        ListView FollowersList = findViewById(R.id.follower_list);
        View item = (View) view.getParent();
        int pos = FollowersList.getPositionForView(item);
        final User follower = followAdapter.getItem(pos);
        Log.i("", follower.getUsername());
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String uid = user.getUid();
        final Map<String, Object> followeeMap = new HashMap<>();
        followeeMap.put("FolloweeID", follower.getUserID());
        followeeMap.put("FolloweeUsername", follower.getUsername());
        firebaseFirestore
                .collection("users")
                .document(uid)
                .collection("following")
                .document(follower.getUserID())
                .set(followeeMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showFollowers();
                        Log.d("FollowersActivity", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FollowersActivity", "Error writing document", e);
                    }
                });
        firebaseFirestore
                .collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String getUsername = document.getString("username");
                            Map<String, Object> followerMap = new HashMap<>();
                            followerMap.put("FollowerID", uid);
                            followerMap.put("FollowerUsername", getUsername);
                            followerMap.put("Accepted", false );
                            firebaseFirestore
                                    .collection("users")
                                    .document(follower.getUserID())
                                    .collection("followers")
                                    .document(uid)
                                    .set(followerMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("FollowersActivity", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("FollowersActivity", "Error writing document", e);
                                        }
                                    });
                        } else {
                            Log.d("FollowersActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public void onAcceptClicked(View view){
        ListView FollowersList = findViewById(R.id.follower_list);
        View item = (View) view.getParent();
        int pos = FollowersList.getPositionForView(item);
        final User follower = followAdapter.getItem(pos);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Map<String,Object> map = new HashMap<>();
        map.put("Accepted",true);
        final String uid = user.getUid();
        firebaseFirestore
                .collection("users")
                .document(uid)
                .collection("followers")
                .document(follower.getUserID())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        update();
                        Log.d("FollowersActivity", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FollowersActivity", "Error writing document", e);
                    }
                });

    }
    public void update(){
        final ListView followingList = (ListView) findViewById(R.id.following_list);
        final ListView followerList = (ListView) findViewById(R.id.follower_list);
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<User> getFollowers, ArrayList<User> getFollowing,ArrayList<Boolean> AcceptedList) {
                followAdapter = new FollowAdapter(FollowersActivity.this,getFollowers, getFollowing,AcceptedList);
                followingAdapter = new FollowingAdapter(FollowersActivity.this,getFollowers,getFollowing);
                followerList.setAdapter(followAdapter);
                followAdapter.notifyDataSetChanged();
                followingList.setAdapter(followingAdapter);
                followingAdapter.notifyDataSetChanged();
            }
        });
    }
    public void onBackClicked(View view) {
        finish();
    }
}
