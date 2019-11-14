package com.example.infinimood.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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

/**
 *  FollowersActivity.java
 *  Activity for viewing your followers
 */

public class FollowersActivity extends MoodCompatActivity {

    private FollowAdapter followAdapter;
    private FollowingAdapter followingAdapter;

    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_list);

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(MainActivity.class);
        }

        update();

        showFollowers();

    }

    public void showFollowers() {
        final ListView followingList = (ListView) findViewById(R.id.followFollowingListView);
        final ListView followerList = (ListView) findViewById(R.id.followFollowerListView);
        followerList.setVisibility(View.VISIBLE);
        followingList.setVisibility(View.INVISIBLE);
        update();
    }

    public void showFollowing() {
        final ListView followingList = (ListView) findViewById(R.id.followFollowingListView);
        final ListView followerList = (ListView) findViewById(R.id.followFollowerListView);
        followingList.setVisibility(View.VISIBLE);
        followerList.setVisibility(View.INVISIBLE);
        update();
    }

    public void onFollowersTabClicked(View view){
        showFollowers();
    }

    public void onFollowingTabClicked(View view){
        showFollowing();
    }

    private interface FirebaseCallback {
        void onCallback(ArrayList<User> followers,ArrayList<User> following,ArrayList<Boolean> AcceptedList);
    }

    public void onUnfollowClicked(View view) {
        ListView FollowingList = findViewById(R.id.followFollowingListView);
        View item = (View) view.getParent();
        int pos = FollowingList.getPositionForView(item);
        User user = followingAdapter.getItem(pos);

        firebaseController.unfollowUser(user);

        update();
    }

    public void onFollowClicked(View view) {
        ListView followersList = findViewById(R.id.followFollowerListView);
        View item = (View) view.getParent();
        int pos = followersList.getPositionForView(item);
        User user = followAdapter.getItem(pos);

        firebaseController.requestToFollow(user);

        update();
    }

    public void onAcceptClicked(View view) {
        ListView followersList = findViewById(R.id.followFollowerListView);
        View item = (View) view.getParent();
        int pos = followersList.getPositionForView(item);
        User user = followAdapter.getItem(pos);

        firebaseController.acceptFollowRequest(user);
    }

    public void update() {
        final ListView followingList = (ListView) findViewById(R.id.followFollowingListView);
        final ListView followerList = (ListView) findViewById(R.id.followFollowerListView);

        users = firebaseController.getUsers();

        followAdapter = new FollowAdapter(FollowersActivity.this, users);
        followingAdapter = new FollowingAdapter(FollowersActivity.this, users);
        followerList.setAdapter(followAdapter);
        followingList.setAdapter(followingAdapter);
        followAdapter.notifyDataSetChanged();
        followingAdapter.notifyDataSetChanged();
    }

    public void onFollowBackClicked(View view) {
        finish();
    }
}
