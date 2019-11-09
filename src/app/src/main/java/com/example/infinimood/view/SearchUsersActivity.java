package com.example.infinimood.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.example.infinimood.controller.FollowAdapter;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchUsersActivity extends MoodCompatActivity  {

    private static final String TAG = "SearchUsersActivity";
    private SearchView searchView;
    private FollowAdapter searchAdapter;
    protected static ArrayList<User> users = new ArrayList<>();
    protected static ArrayList<User> following = new ArrayList<>();
    protected static ArrayList<Boolean> accepted = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_users);
        searchView = (SearchView) findViewById(R.id.searchSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findUser(query);
                update();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



    }

    public void findUser(String query){
        CollectionReference userCollection = firebaseFirestore.collection("users");
        userCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            users.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userID = (String) document.getId();
                                String username = (String) document.get("username");
                                if (query.equals(username)) {
                                    User user = new User(userID, username);
                                    users.add(user);
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
    public void getFollowing() {

        String uid = firebaseAuth.getUid();
        firebaseFirestore
                .collection("users")
                .document(uid)
                .collection("following")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            following.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String getUserID = document.getString("FolloweeID");
                                String getUsername = document.getString("FolloweeUsername");
                                User getFollowee = new User(getUserID, getUsername);
                                following.add(getFollowee);
                            }
                        } else {
                            Log.e(TAG, "Error getting documents");
                        }
                    }
                });
    }

    public void onFollowClicked(View view){

        ListView searchListView = findViewById(R.id.searchListView);
        View item = (View) view.getParent();
        int pos = searchListView.getPositionForView(item);
        final User follower = searchAdapter.getItem(pos);
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
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    public void update(){
        getFollowing();
        ListView searchListView = findViewById(R.id.searchListView);
        searchAdapter = new FollowAdapter(SearchUsersActivity.this,users,following,accepted);
        searchListView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();
    }


}
