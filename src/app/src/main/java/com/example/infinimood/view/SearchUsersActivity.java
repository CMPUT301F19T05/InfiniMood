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

    protected ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_users);

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(MainActivity.class);
        }

        users = firebaseController.getUsers();

        searchView = (SearchView) findViewById(R.id.searchSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                findUserBySubstring(newText);
                update();
                return false;
            }
        });
    }

    public void findUserBySubstring(String newText) {
        users = firebaseController.getUsers();

        if (newText.equals("")) {
            return;
        }

        String currentUserUsername = firebaseController.getUsername();

        for (int i = 0; i < users.size(); ++i) {
            String username = users.get(i).getUsername();
            if (!username.contains(newText) || username.equals(currentUserUsername)) {
                users.remove(i);
                continue;
            }
        }

        update();
    }

    public void onFollowClicked(View view) {
        ListView searchListView = findViewById(R.id.searchListView);
        View item = (View) view.getParent();
        int pos = searchListView.getPositionForView(item);
        User followee = searchAdapter.getItem(pos);

        firebaseController.requestToFollow(followee);

        update();
    }

    public void update(){
        ListView searchListView = findViewById(R.id.searchListView);
        searchAdapter = new FollowAdapter(SearchUsersActivity.this, users);
        searchListView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();
    }


}
