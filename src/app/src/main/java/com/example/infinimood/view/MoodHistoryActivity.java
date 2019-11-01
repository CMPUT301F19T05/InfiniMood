package com.example.infinimood.view;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.ListView;

        import androidx.appcompat.widget.Toolbar;

        import com.example.infinimood.R;
        import com.example.infinimood.controller.MoodHistoryAdapter;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.android.material.snackbar.Snackbar;

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
