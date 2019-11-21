package com.example.infinimood.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.model.Mood;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import androidx.core.app.ActivityCompat;

/**
 *  EditMoodActivity.java
 *  Activity for editing existing mood objects
 */

public class EditMoodActivity extends MoodCompatActivity {

    private static final String TAG = "EditMoodActivity";
    private static final int PICK_IMAGE = 1;
    private static final int PICK_LOCATION = 2;

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Spinner moodSpinner;
    private EditText reasonInput;
    private Spinner socialSituationSpinner;
    private Button addEditSubmitButton;
    private ImageView testImage;

    private String moodId;
    private String moodEmotion;
    private long moodDate;
    private String moodReason;
    private String moodSocialSituation;
    private Location moodLocation = null;
    private Bitmap moodImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_mood);

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(MainActivity.class);
        }

        // find views
        datePicker = findViewById(R.id.addEditDatePicker);
        timePicker = findViewById(R.id.addEditTimePicker);
        moodSpinner = findViewById(R.id.addEditMoodSpinner);
        reasonInput = findViewById(R.id.addEditReasonEditText);
        socialSituationSpinner = findViewById(R.id.addEditSocialSituationSpinner);
        addEditSubmitButton = findViewById(R.id.addEditSubmitButton);
        testImage = findViewById(R.id.testImageView);

        // extract mood info from intent
        Intent intent = getIntent();
        moodId = intent.getStringExtra("moodId");
        moodDate = intent.getLongExtra("moodDate", 0);
        moodReason = intent.getStringExtra("moodReason");
        moodSocialSituation = intent.getStringExtra("moodSocialSituation");
        moodEmotion = intent.getStringExtra("moodMood");

        double latitude = intent.getDoubleExtra("moodLatitude", 0);
        double longitude = intent.getDoubleExtra("moodLongitude", 0);

        moodLocation = new Location("");
        moodLocation.setLatitude(latitude);
        moodLocation.setLongitude(longitude);

        if (moodLocation != null) {
            Log.i(TAG, moodLocation.toString());
        }
        else {
            Log.i(TAG, "Mood location is null");
        }

        // set date and time pickers to the mood's date and time
        Date date = new Date(moodDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        timePicker.setMinute(calendar.get(Calendar.MINUTE));
        timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setIs24HourView(false);

        // set mood spinner to mood's emotion
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.emojis_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(adapter);
        if (moodEmotion != null) {
            int spinnerPosition = adapter.getPosition(moodEmotion);
            moodSpinner.setSelection(spinnerPosition);
        }

        // set social situation spinner to mood's social situation
        adapter = ArrayAdapter.createFromResource(this, R.array.situations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socialSituationSpinner.setAdapter(adapter);
        if (moodSocialSituation != null) {
            int spinnerPosition = adapter.getPosition(moodSocialSituation);
            socialSituationSpinner.setSelection(spinnerPosition);
        }

        // set reason input text to mood's reason
        reasonInput.setText(moodReason);

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

    public void onUploadPhotoClicked(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                moodImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                testImage.setImageBitmap(moodImage);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if( requestCode == PICK_LOCATION && resultCode == RESULT_OK ) {
            String longitude = data.getExtras().getString("LONG");
            String latitude = data.getExtras().getString("LAT");
            Location l = new Location("dummy");
            l.setLatitude( Double.parseDouble(latitude) );
            l.setLongitude(Double.parseDouble(longitude) );
            moodLocation = l;
        }
    }

    public void onChooseLocationPicked( View view ) {
        final Intent intent = new Intent(this, ChooseLocationActivity.class);
        intent.putExtra("EDITING", true);
        intent.putExtra("moodLatitude", moodLocation.getLatitude());
        intent.putExtra("moodLongitude", moodLocation.getLongitude());
        startActivityForResult(intent, PICK_LOCATION);
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
        moodDate = calendar.getTime().getTime();


        Mood newMood = moodFactory.createMood(moodId, moodEmotion, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);

        firebaseController.addMoodEventToDB(newMood, new BooleanCallback() {
            @Override
            public void onCallback(boolean success) {
                if (success) {
                    toast("Successfully edited Mood event");
                }
                else {
                    toast("Failed to edit Mood event");
                }
            }
        });

        finish();
    }
}
