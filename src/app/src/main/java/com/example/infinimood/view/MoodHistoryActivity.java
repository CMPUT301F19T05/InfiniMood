package com.example.infinimood.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.GetMoodsCallback;
import com.example.infinimood.controller.MoodHistoryAdapter;
import com.example.infinimood.fragment.MoodHistoryFragment;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodComparator;
import com.example.infinimood.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * MoodHistoryActivity.java
 * Activity for viewing your mood events in a ListView
 */

public class MoodHistoryActivity extends MoodCompatActivity {

    private MoodHistoryAdapter adapter;
    private MoodComparator comparator = new MoodComparator();
    private Switch reverseToggle;

    private ListView moodListView;

    private Intent i = null;

    BottomNavigationView navigationView;

    // runs when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().getItem(2).setChecked(true);

        i = getIntent();

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(MainActivity.class);
        }
        else if (i.getExtras() == null){
            moodListView = findViewById(R.id.moodHistoryListView);

            reverseToggle = findViewById(R.id.moodHistorySortOrderButton);
            reverseToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    comparator.reverse();
                    update();
                }
            });
            // update the UI
            update();
        }
        else {
            User user = (User) i.getSerializableExtra("user");
            moodListView = findViewById(R.id.moodHistoryListView);

            reverseToggle = findViewById(R.id.moodHistorySortOrderButton);
            reverseToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    comparator.reverse();
                    update(user);
                }
            });
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
                            update();
                        } else {
                            toast("Could not delete mood");
                        }
                    }
                }).show(getSupportFragmentManager(), "SHOW_MOOD");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        update();
    }

    public void update() {
        firebaseController.refreshUserMoods(new GetMoodsCallback() {
            @Override
            public void onCallback(ArrayList<Mood> moodsArrayList) {
                moods = moodsArrayList;
                Collections.sort(moods, comparator);
                adapter = new MoodHistoryAdapter(MoodHistoryActivity.this, moods);
                moodListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();  // update the ListView
            }
        });
    }
    public void update(User user) {
        firebaseController.refreshOtherUserMoods(user, new GetMoodsCallback() {
            @Override
            public void onCallback(ArrayList<Mood> moodsArrayList) {
                moods = moodsArrayList;
                Collections.sort(moods, comparator);
                adapter = new MoodHistoryAdapter(MoodHistoryActivity.this, moods);
                moodListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();  // update the ListView
            }
        });
    }

    public void moodMapClick(View view) {
        final Intent intent = new Intent(this, MoodMapActivity.class);
        startActivity(intent);
    }

    // We should have a NavBar class for these methods
    public void onSearchUsersClicked(MenuItem item) {
        final Intent intent = new Intent(this, UsersActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    public void onAddMoodClicked(MenuItem item) {
        final Intent intent = new Intent(this, AddEditMoodActivity.class);
        intent.putExtra("requestCode", ADD_MOOD);
        item.setChecked(true);
        startActivity(intent);
    }

    public void onMoodHistoryClicked(MenuItem item) {
        final Intent intent = new Intent(this, MoodHistoryActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    public void onUserProfileClicked(MenuItem item) {
        final Intent intent = new Intent(this, UserProfileActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }


}
