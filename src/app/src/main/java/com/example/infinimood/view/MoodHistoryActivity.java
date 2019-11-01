package com.example.infinimood.view;

        import android.os.Bundle;
        import android.widget.ListView;

        import com.example.infinimood.R;
        import com.example.infinimood.controller.MoodHistoryAdapter;

public class MoodHistoryActivity extends MoodCompatActivity {

    private MoodHistoryAdapter adapter;

    // runs when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        refreshUserMoods();
        ListView moodListView = findViewById(R.id.history_list_view);

        adapter = new MoodHistoryAdapter(this, moods);
        moodListView.setAdapter(adapter);

        // update the UI
        update();
    }

    public void update() {
        adapter.notifyDataSetChanged();  // update the ListView
    }
}
