package com.example.infinimood.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.infinimood.R;
import com.example.infinimood.controller.StringCallback;
import com.google.android.material.navigation.NavigationView;

/**
 * UserProfileActivity.java
 * Homepage when authenticated
 * Access point for most functionality
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

        firebaseController.getUsername(new StringCallback() {
            @Override
            public void onCallback(String username) {
                textViewUsername.setText(username);
            }
        });
    }

    public void onAddMoodClicked(View view) {
        final Intent intent = new Intent(this, AddMoodActivity.class);
        startActivity(intent);
    }

    public void onMoodHistoryClicked(View view) {
        final Intent intent = new Intent(this, MoodHistoryActivity.class);
        startActivity(intent);
    }

    public void onSearchUsersClicked(View view) {
        final Intent intent = new Intent(this, UsersActivity.class);
        startActivity(intent);
    }

    public void onLogoutClicked(View view) {
        firebaseController.signOut();
        startActivityNoHistory(MainActivity.class);
    }

    // print all of the current user's mood events to console
    public void onPrintMoodsClicked(View view) {
        Log.i("", "===========================================");
        for (int i = 0; i < moods.size(); i++) {
            moods.get(i).print();
            Log.i("", "===========================================");
        }
    }

    public void onLocationClicked(View view) {
        final Intent intent = new Intent(this, ChooseLocationActivity.class);
        startActivity(intent);
    }

}
