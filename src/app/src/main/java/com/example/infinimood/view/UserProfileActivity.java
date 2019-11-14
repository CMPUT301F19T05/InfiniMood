package com.example.infinimood.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 *  UserProfileActivity.java
 *  Homepage when authenticated
 *  Access point for most functionality
 */

public class UserProfileActivity extends MoodCompatActivity {

    private static final String TAG = "UserProfileActivity";

    TextView textViewUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textViewUsername = findViewById(R.id.profileUsernameTextView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(MainActivity.class);
        }
        else {
            firebaseController.fetchUsername();
            String username = firebaseController.getUsername();
            textViewUsername.setText(username);
        }
    }

    public void onAddMoodClicked(View view) {
        final Intent intent = new Intent(this, AddEditMoodActivity.class);
        startActivity(intent);
    }

    public void onMoodHistoryClicked(View view) {
        final Intent intent = new Intent(this, MoodHistoryActivity.class);
        startActivity(intent);
    }

    public void onFollowersClicked(View view){
        final Intent intent = new Intent(this, FollowersActivity.class);
        startActivity(intent);
    }

    public void onSearchUsersClicked(View view){
        final Intent intent = new Intent(this, SearchUsersActivity.class);
        startActivity(intent);
    }

    public void onLogoutClicked(View view) {
        firebaseController.signOut();
        startActivityNoHistory(MainActivity.class);
    }

    // print all of the current user's mood events to console
    public void onPrintMoodsClicked(View view) {
        firebaseController.refreshUserMoods(moods);
        Log.i("", "===========================================");
        for (int i = 0; i < moods.size(); i++) {
            moods.get(i).print();
            Log.i("", "===========================================");
        }
    }

    public void onLocationClicked(View view) {
        final Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }
}
