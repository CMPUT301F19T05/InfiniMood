package com.example.infinimood.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.infinimood.R;
import com.example.infinimood.controller.StringCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * UserProfileActivity.java
 * Homepage when authenticated
 * Access point for most functionality
 */
public class UserProfileActivity extends MoodCompatActivity {

    private static final String TAG = "UserProfileActivity";

    TextView textViewUsername;
    BottomNavigationView navigationView;

    /**
     * onCreate
     * Ovverides onCreate. Gets the activity ready. Runs when activity is created.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textViewUsername = findViewById(R.id.profileUsernameTextView);
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().getItem(3).setChecked(true);
    }

    /**
     * onStart
     * Overrides onStart. Is run on start.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(LoginActivity.class);
        }

        firebaseController.getUsername(new StringCallback() {
            @Override
            public void onCallback(String username) {
                textViewUsername.setText(username);
            }
        });
    }

    /**
     * onAddMoodClicked
     * starts AddEditMoodActivity
     * @param item MenuItem
     */
    public void onAddMoodClicked(MenuItem item) {
        final Intent intent = new Intent(this, AddEditMoodActivity.class);
        intent.putExtra("requestCode", ADD_MOOD);
        item.setChecked(true);
        startActivity(intent);
    }

    /**
     * onMoodHistoryClicked
     * starts MoodHistoryActivity
     * @param item MenuItem
     */
    public void onMoodHistoryClicked(MenuItem item) {
        final Intent intent = new Intent(this, MoodHistoryActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    /**
     * onSearchUsersClicked
     * starts UsersActivity
     * @param item MenuItem
     */
    public void onSearchUsersClicked(MenuItem item) {
        final Intent intent = new Intent(this, UsersActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    /**
     * onUsersProfileClicked
     * starts UserProfileActivity
     * @param item MenuItem
     */
    public void onUserProfileClicked(MenuItem item) {
        final Intent intent = new Intent(this, UserProfileActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    /**
     * onLogoutClicked
     * signs out of firebase and starts LoginActivity
     * @param view View
     */
    public void onLogoutClicked(View view) {
        firebaseController.signOut();
        startActivityNoHistory(LoginActivity.class);
    }

    /**
     * onPrintMoodsClicked
     * print all of the current user's mood events to console
     * @param view View
     */
    public void onPrintMoodsClicked(View view) {
        Log.i("", "===========================================");
        for (int i = 0; i < moods.size(); i++) {
            moods.get(i).print();
            Log.i("", "===========================================");
        }
    }
}
