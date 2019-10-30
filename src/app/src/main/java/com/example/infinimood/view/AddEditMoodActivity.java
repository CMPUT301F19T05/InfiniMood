package com.example.infinimood.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.infinimood.R;
import com.example.infinimood.model.AfraidMood;
import com.example.infinimood.model.AngryMood;
import com.example.infinimood.model.CryingMood;
import com.example.infinimood.model.HappyMood;
import com.example.infinimood.model.InLoveMood;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.SadMood;
import com.example.infinimood.model.SleepyMood;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AddEditMoodActivity extends MoodCompatActivity {

    private static final String TAG = "AddEditMoodActivity";

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Spinner moodSpinner;
    private EditText reasonInput;
    private Spinner socialSituationSpinner;
    private Button submitButton;

    private String mood_emotion;
    private Date mood_date;
    private String mood_reason;
    private String mood_social_situation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_mood);

        // find views
        datePicker = findViewById(R.id.addEditDatePicker);
        timePicker = findViewById(R.id.addEditTimePicker);
        moodSpinner = findViewById(R.id.addEditMoodSpinner);
        reasonInput = findViewById(R.id.addEditReasonEditText);
        socialSituationSpinner = findViewById(R.id.addEditSocialSituationSpinner);
        submitButton = findViewById(R.id.addEditSubmitButton);

        // change mood_emotion according to the mood spinner
        moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String mood = (String) moodSpinner.getItemAtPosition(i);
                mood_emotion = mood;
                Log.i(TAG, mood_emotion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e(TAG, "This shouldn't happen (empty mood spinner)");
            }
        });

        // change mood_social_situation according to the social situation spinner
        socialSituationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String social_situation = (String) socialSituationSpinner.getItemAtPosition(i);
                mood_social_situation = social_situation;
                Log.i(TAG, mood_social_situation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e(TAG, "This shouldn't happen (empty social situation spinner)");
            }
        });
    }

    public void onSubmitClicked(View view) {
        // check for empty fields
        if (mood_emotion.equals("")) {
            toast("Select a mood");
            return;
        }
        if (mood_social_situation.equals("")) {
            toast("Select a social situation");
            return;
        }

        mood_reason = reasonInput.getText().toString();

        // Extract date / time from datePicker and timePicker
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);
        mood_date = calendar.getTime();

        Mood newMood;

        Random random = new Random();
        String mood_id = String.valueOf(Math.abs(random.nextInt()));

        switch (mood_emotion) {
            case "Happy":
                newMood = new HappyMood(mood_id, mood_date, mood_reason, mood_social_situation);
                break;
            case "Angry":
                newMood = new AngryMood(mood_id, mood_date, mood_reason, mood_social_situation);
                break;
            case "Crying":
                newMood = new CryingMood(mood_id, mood_date, mood_reason, mood_social_situation);
                break;
            case "InLove":
                newMood = new InLoveMood(mood_id, mood_date, mood_reason, mood_social_situation);
                break;
            case "Sad":
                newMood = new SadMood(mood_id, mood_date, mood_reason, mood_social_situation);
                break;
            case "Sleepy":
                newMood = new SleepyMood(mood_id, mood_date, mood_reason, mood_social_situation);
                break;
            case "Afraid":
                newMood = new AfraidMood(mood_id, mood_date, mood_reason, mood_social_situation);
                break;
            default:
                Log.e(TAG, "Default case in addEdit switch");
                break;
        }

        Log.i(TAG, "Created mood object:");
        Log.i(TAG, mood_id);
        Log.i(TAG, mood_emotion);
        Log.i(TAG, mood_social_situation);
        Log.i(TAG, mood_date.toString());
        Log.i(TAG, mood_reason);

        addMoodEventToDB(mood_id, mood_emotion, mood_social_situation, mood_reason, mood_date);

        finish();
    }
}
