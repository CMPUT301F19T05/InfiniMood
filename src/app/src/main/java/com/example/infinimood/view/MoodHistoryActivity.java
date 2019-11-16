package com.example.infinimood.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.appcompat.app.ActionBar;

import com.example.infinimood.R;
import com.example.infinimood.controller.GetMoodsCallback;
import com.example.infinimood.controller.MoodHistoryAdapter;
import com.example.infinimood.fragment.MoodHistoryFragment;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 *  MoodHistoryActivity.java
 *  Activity for viewing your mood events in a ListView
 *  TODO: Click to edit, swipe right to delete (with confirmation)
 */

public class MoodHistoryActivity extends MoodCompatActivity {

    private MoodHistoryAdapter adapter;
    private MoodComparator comparator = new MoodComparator();
    private Switch reverseToggle;

    private ListView moodListView;

    // runs when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(MainActivity.class);
        }
        else {
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
        moodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mood mood = adapter.getItem(position);
                new MoodHistoryFragment(mood).show(getSupportFragmentManager(), "SHOW_MOOD");
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

    public void moodMapClick( View view ) {
        final Intent intent = new Intent(this, MoodMapActivity.class);
        startActivity(intent);
    }
}
