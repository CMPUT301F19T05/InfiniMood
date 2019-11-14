package com.example.infinimood.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.appcompat.app.ActionBar;

import com.example.infinimood.R;
        import com.example.infinimood.controller.MoodHistoryAdapter;
        import com.example.infinimood.model.MoodComparator;

import java.util.Collections;

/**
 *  MoodHistoryActivity.java
 *  Activity for viewing your mood events in a ListView
 *  TODO: Click to edit, swipe right to delete (with confirmation)
 */

public class MoodHistoryActivity extends MoodCompatActivity {

    private MoodHistoryAdapter adapter;
    private MoodComparator comparator;
    private Switch reverseToggle;

    // runs when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(MainActivity.class);
        }

        firebaseController.refreshUserMoods(moods);
        ListView moodListView = findViewById(R.id.moodHistoryListView);

        reverseToggle = findViewById(R.id.moodHistorySortOrderButton);
        reverseToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                comparator.reverse();
                update();
            }
        });

        comparator = new MoodComparator();

        adapter = new MoodHistoryAdapter(this, moods);
        moodListView.setAdapter(adapter);

        // update the UI
        update();
    }

    public void update() {
        firebaseController.refreshUserMoods(moods);
        Collections.sort(moods, comparator);
        adapter.notifyDataSetChanged();  // update the ListView
    }

    public void moodMapClick( View view ) {
        final Intent intent = new Intent(this, MoodMapActivity.class);
        startActivity(intent);
    }
}
