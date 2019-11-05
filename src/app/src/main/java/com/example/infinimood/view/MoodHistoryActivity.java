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

public class MoodHistoryActivity extends MoodCompatActivity {

    private MoodHistoryAdapter adapter;
    private MoodComparator comparator;
    private Switch reverseToggle;

    // runs when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        refreshUserMoods();
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
        Collections.sort(moods, comparator);
        adapter.notifyDataSetChanged();  // update the ListView
    }

//    public boolean onOptionsItemSelected(MenuItem item){
//        finish();
//        return true;
//    }
}
