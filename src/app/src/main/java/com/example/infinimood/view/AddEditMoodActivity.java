package com.example.infinimood.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import androidx.core.app.ActivityCompat;

/**
 *  AddEditMoodActivity.java
 *  Activity for creating mood objects and editing existing mood objects
 *  TODO: Edit functionality
 */

public class AddEditMoodActivity extends MoodCompatActivity {

    private static final String TAG = "AddEditMoodActivity";

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Spinner moodSpinner;
    private EditText reasonInput;
    private Spinner socialSituationSpinner;
    private Button addEditSubmitButton;

    private String moodEmotion;
    private Date moodDate;
    private String moodReason;
    private String moodSocialSituation;
    private Location moodLocation = null;
    private Image moodImage = null;

    private FusedLocationProviderClient fusedLocationProviderClient;

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
        addEditSubmitButton = findViewById(R.id.addEditSubmitButton);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        updateCurrentLocation();

        // change moodEmotion according to the mood spinner
        moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String mood = (String) moodSpinner.getItemAtPosition(i);
                moodEmotion = mood;
                Log.i(TAG, moodEmotion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e(TAG, "This shouldn't happen (empty mood spinner)");
            }
        });

        // change moodSocialSituation according to the social situation spinner
        socialSituationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String socialSituation = (String) socialSituationSpinner.getItemAtPosition(i);
                moodSocialSituation = socialSituation;
                Log.i(TAG, moodSocialSituation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e(TAG, "This shouldn't happen (empty social situation spinner)");
            }
        });
    }

    private void updateCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    moodLocation = location;
                    toast("Current Location Added");
                } else {
                    // https://stackoverflow.com/questions/29441384/fusedlocationapi-getlastlocation-always-null
                    toast("See updateCurrentLocation() in AddEditMoodActivity.java");
                }
            }
        });
    }

    public void onSubmitClicked(View view) {
        // check for empty fields
        if (moodEmotion.equals("")) {
            toast("Select a mood");
            return;
        }
        if (moodSocialSituation.equals("")) {
            toast("Select a social situation");
            return;
        }

        moodReason = reasonInput.getText().toString();

        // Extract date / time from datePicker and timePicker
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);
        moodDate = calendar.getTime();

        String uuid = UUID.randomUUID().toString();

        Mood newMood = moodController.createMood(uuid, moodEmotion, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);

        firebaseController.addMoodEventToDB(newMood);

        finish();
    }
}
