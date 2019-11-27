package com.example.infinimood.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FilterCallback;
import com.example.infinimood.controller.GetMoodsCallback;
import com.example.infinimood.controller.MoodHistoryAdapter;
import com.example.infinimood.fragment.MoodHistoryFragment;
import com.example.infinimood.fragment.FilterFragment;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodComparator;
import com.example.infinimood.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * MoodHistoryActivity.java
 * Activity for viewing your mood events in a ListView
 */
public class MoodHistoryActivity extends MoodCompatActivity {

    private MoodHistoryAdapter adapter;
    private MoodComparator comparator = new MoodComparator();
    private ToggleButton reverseToggle;
    private FloatingActionButton filterButton;

    private ListView moodListView;
    private TextView moodHistoryTextView;

    private User user = null;

    private boolean filtered = false;
    private HashSet<String> filter = new HashSet<>();
    private ArrayList<Mood> filteredList = new ArrayList<>();

    BottomNavigationView navigationView;

    /**
     * onCreate
     * Ovverides onCreate. Gets the activity ready. Runs when activity is created.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().getItem(2).setChecked(true);

        Intent i = getIntent();
        user =  (User)i.getSerializableExtra("user");

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(LoginActivity.class);
        } else {
            moodHistoryTextView = findViewById(R.id.moodHistoryTitle);
            if (user != null){
                moodHistoryTextView.setText(user.getUsername() + "'s Recent Moods");
            }
            moodListView = findViewById(R.id.moodHistoryListView);

            reverseToggle = findViewById(R.id.moodHistorySortOrderButton);
            reverseToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    comparator.reverse();
                    update(user);
                }
            });

            filterButton = findViewById(R.id.filter_button);
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FilterFragment frag = new FilterFragment(filter, filtered, new FilterCallback() {
                        @Override
                        public void onCallback(HashSet<String> newFilter) {
                            filter = newFilter;
                            filteredList.clear();
                            for (Mood mood: moods) {
                                if (filter.contains(mood.getMood())) {
                                    filteredList.add(mood);
                                }
                            }
                            filtered = true;
                            adapter.notifyDataSetChanged();
                        }
                    });
                    frag.show(getSupportFragmentManager(), "SHOW_FILTER");
                }
            });

            // update the UI
            update(user);
        }

        moodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mood mood = adapter.getItem(position);
                new MoodHistoryFragment(mood, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean bool) {
                        if (bool) {
                            toast("Mood deleted");
                            update(user);
                        } else {
                            toast("Could not delete mood");
                        }
                    }
                }).show(getSupportFragmentManager(), "SHOW_MOOD");
            }
        });
    }

    /**
     * onStart
     * Overrides onStart. Is run on start. updates the user's information
     */
    @Override
    protected void onStart() {
        super.onStart();
        update(user);
    }

    /**
     * update
     * Update user's information from firebase
     * @param user User - user to update
     */
    public void update(User user) {
        if (user == null) {
            firebaseController.refreshUserMoods(new GetMoodsCallback() {
                @Override
                public void onCallback(ArrayList<Mood> moodsArrayList) {
                    moods = moodsArrayList;
                    Collections.sort(moods, comparator);
                    filteredList = new ArrayList<>(moods);
                    adapter = new MoodHistoryAdapter(MoodHistoryActivity.this, filteredList);
                    moodListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();  // update the ListView
                }
            });
        }
        else {
            firebaseController.refreshOtherUserMoods(user, new GetMoodsCallback() {
                @Override
                public void onCallback(ArrayList<Mood> moodsArrayList) {
                    otherUserMoods = moodsArrayList;
                    Collections.sort(otherUserMoods, comparator);
                    adapter = new MoodHistoryAdapter(MoodHistoryActivity.this, otherUserMoods);
                    moodListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();  // update the ListView
                }
            });
        }
    }

    /**
     * moodMapClick
     * Starts MoodMapActivity
     * @param view View
     */
    public void moodMapClick(View view) {
        final Intent intent = new Intent(this, MoodMapActivity.class);
        startActivity(intent);
    }

    /**
     * onSearchUsersClicked
     * Starts UsersActivity
     * @param item MenuItem
     */
    public void onSearchUsersClicked(MenuItem item) {
        final Intent intent = new Intent(this, UsersActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    /**
     * onAddMoodClicked
     * Starts AddEditMoodActivity
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
     * Starts MoodHistoryActivity
     * @param item MenuItem
     */
    public void onMoodHistoryClicked(MenuItem item) {
        final Intent intent = new Intent(this, MoodHistoryActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    /**
     * onUserProfileClicked
     * Starts UserProfileActivity
     * @param item MenuItem
     */
    public void onUserProfileClicked(MenuItem item) {
        final Intent intent = new Intent(this, UserProfileActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

}
