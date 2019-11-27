package com.example.infinimood.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.DatePickerCallback;
import com.example.infinimood.controller.TimePickerCallback;
import com.example.infinimood.fragment.DatePickerFragment;
import com.example.infinimood.fragment.TimePickerFragment;
import com.example.infinimood.fragment.ViewImageFragment;
import com.example.infinimood.model.Mood;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * AddEditMoodActivity.java
 * Activity for adding new mood events and editing existing mood events
 */

public class AddEditMoodActivity extends MoodCompatActivity {
    private static final String TAG = "AddEditMoodActivity";

    private static final int PICK_IMAGE = 1;
    private static final int PICK_LOCATION = 2;
    private static final int TAKE_IMAGE = 4;
    protected static final int VIEW_LOCATION = 5;


    private int requestCode;

    // views
    private TextView titleTextView;
    private Spinner moodSpinner;
    private Spinner socialSituationSpinner;
    private EditText reasonEditText;
    private TextView selectedTimeTextView;
    private TextView selectedDateTextView;
    private BottomNavigationView navigationView;

    // buttons
    private Button cameraButton;
    private Button galleryButton;
    private Button photoViewButton;

    // mood attributes
    private String moodId;
    private String moodEmotion;
    private long moodDate;
    private String moodReason;
    private String moodSocialSituation;
    private Location moodLocation = null;
    private Bitmap moodImage = null;
    private boolean moodHasImage = false;
    private boolean uploadedImage = false;
    private String moodIcon;
    private String moodColor;

    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_mood);

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(LoginActivity.class);
        }

        // find views
        titleTextView = findViewById(R.id.addEditTitleTextView);
        moodSpinner = findViewById(R.id.addEditMoodSpinner);
        socialSituationSpinner = findViewById(R.id.addEditSocialSituationSpinner);
        reasonEditText = findViewById(R.id.addEditReasonEditText);
        selectedTimeTextView = findViewById(R.id.addEditSelectedTimeTextView);
        selectedDateTextView = findViewById(R.id.addEditSelectedDateTextView);
        navigationView = findViewById(R.id.bottom_navigation);
        galleryButton = findViewById(R.id.uploadPhotoButton);
        cameraButton = findViewById(R.id.takePhotoButton);
        photoViewButton = findViewById(R.id.addEditViewImageButton);

        updatePhotoButtons();

        navigationView.getMenu().getItem(1).setChecked(true);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // extract mood info from intent
        Intent intent = getIntent();

        requestCode = intent.getIntExtra("requestCode", ADD_MOOD);

        if (requestCode == ADD_MOOD) {
            titleTextView.setText(R.string.add_mood_title);

            moodId = UUID.randomUUID().toString();

            // set date and time pickers to the mood's date and time
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            moodDate = calendar.getTime().getTime();

            updateDate();

        } else if (requestCode == EDIT_MOOD) {
            titleTextView.setText(R.string.edit_mood_title);

            Mood mood = intent.getParcelableExtra("mood");

            // set widgets to match the mood event
            if (mood != null) {
                fillWithMoodEvent(mood);
            } else {
                Log.e(TAG, "Mood is null in AddEditMoodActivity");
                finish();
            }
        }

        // change moodEmotion according to the mood spinner
        moodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                moodEmotion = (String) moodSpinner.getItemAtPosition(i);
                moodEmotion = moodEmotion.substring(3);
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
                moodSocialSituation = (String) socialSituationSpinner.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e(TAG, "This shouldn't happen (empty social situation spinner)");
            }
        });
    }

    // set the EditTexts, Spinners, Pickers, etc. to match a given mood event
    public void fillWithMoodEvent(Mood mood) {
        moodId = mood.getId();
        moodDate = mood.getDate();
        moodReason = mood.getReason();
        moodLocation = mood.getLocation();
        moodSocialSituation = mood.getSocialSituation();
        moodEmotion = mood.getMood();
        moodHasImage = mood.hasImage();
        moodIcon = mood.getIcon();
        moodColor = mood.getColor();

        // set date and time pickers to the mood's date and time
        Date date = new Date(moodDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        updateDate();

        // set mood spinner to mood's emotion
        ArrayAdapter<CharSequence> oldAdapter = ArrayAdapter.createFromResource(this, R.array.moods_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.moods_icons_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(adapter);
        if (moodEmotion != null) {
            int spinnerPosition = oldAdapter.getPosition(moodEmotion);
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
        reasonEditText.setText(moodReason);
    }

    // start android image selection
    public void onUploadPhotoClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    public void onTakePhotoClicked(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_IMAGE);
    }

    // handle the result of selecting images and locations
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                moodImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                moodHasImage = true;
                uploadedImage = true;
                updatePhotoButtons();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(requestCode == TAKE_IMAGE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            moodImage = (Bitmap) extras.get("data");
            moodHasImage = true;
            uploadedImage = true;
            updatePhotoButtons();
        } else if (requestCode == PICK_LOCATION && resultCode == RESULT_OK) {

            boolean addedLocation = data.getBooleanExtra("ADDED", false);

            if (addedLocation) {
                double latitude = data.getDoubleExtra("LAT", 0);
                double longitude = data.getDoubleExtra("LON", 0);
                moodLocation = new Location("");
                moodLocation.setLatitude(latitude);
                moodLocation.setLongitude(longitude);
            } else {
                moodLocation = null;
            }
        }
    }

    // start ChooseLocationActivity
    public void onChooseLocationPicked(View view) {
        final Intent intent = new Intent(this, ChooseLocationActivity.class);
        if (moodLocation != null) {
            intent.putExtra("HAS_LOCATION", true);
            intent.putExtra("location", moodLocation);
        } else {
            intent.putExtra("HAS_LOCATION", false);
        }
        startActivityForResult(intent, PICK_LOCATION);
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
                    toast("Current location added");
                } else {
                    // https://stackoverflow.com/questions/29441384/fusedlocationapi-getlastlocation-always-null
                    toast("Could not get location");
                }
            }
        });
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(moodDate, new TimePickerCallback() {
            @Override
            public void OnCallback(int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(moodDate));
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                moodDate = calendar.getTime().getTime();
                updateDate();
            }
        });
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(moodDate, new DatePickerCallback() {
            @Override
            public void OnCallback(int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(moodDate));
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                moodDate = calendar.getTime().getTime();
                updateDate();
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void updateDate() {
        Date date = new Date(moodDate);

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d yyyy", Locale.getDefault());

        String timeString = timeFormat.format(date);
        String dateString = dateFormat.format(date);

        selectedTimeTextView.setText(timeString);
        selectedDateTextView.setText(dateString);
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

        moodReason = reasonEditText.getText().toString();

        Mood newMood = moodFactory.createMood(moodId, moodEmotion, moodDate, moodReason, moodLocation, moodSocialSituation, moodHasImage);

        if (uploadedImage) {
            firebaseController.addImageToDB(newMood, moodImage, new BooleanCallback() {
                @Override
                public void onCallback(boolean bool) {
                    if (bool) {
                        toast("Image upload successful");
                        addMoodToDB(newMood);
                    } else {
                        toast("Image upload failed");
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                    }
                }
            });
        } else {
            addMoodToDB(newMood);
        }
    }

    public void addMoodToDB(Mood newMood) {
        // update the mood event in firebase
        firebaseController.addMoodEventToDB(newMood, new BooleanCallback() {
            @Override
            public void onCallback(boolean success) {
                if (success) {
                    if (requestCode == ADD_MOOD) {
                        toast("Successfully added Mood event");
                    } else if (requestCode == EDIT_MOOD) {
                        toast("Successfully edited Mood event");
                    }

                    Intent returnIntent = new Intent();
                    // return the new mood event to the activity that called this
                    returnIntent.putExtra("mood", newMood);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    if (requestCode == ADD_MOOD) {
                        toast("Failed to add Mood event");
                    } else if (requestCode == EDIT_MOOD) {
                        toast("Failed to edit Mood event");
                    }
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }
        });
    }

    public void onViewImageClicked(View view) {

        if (moodHasImage && moodImage == null) {
            Mood newMood = moodFactory.createMood(moodId, moodEmotion, moodDate, moodReason, moodLocation, moodSocialSituation, true);
            new ViewImageFragment(newMood, new BooleanCallback() {
                @Override
                public void onCallback(boolean bool) {
                    uploadedImage = false;
                    moodHasImage = false;
                    moodImage = null;
                    updatePhotoButtons();
                }
            }).show(getSupportFragmentManager(), "VIEW_IMAGE");
        }
        if (moodHasImage && moodImage != null) {
            new ViewImageFragment(moodImage, new BooleanCallback() {
                @Override
                public void onCallback(boolean bool) {
                    uploadedImage = false;
                    moodHasImage = false;
                    moodImage = null;
                    updatePhotoButtons();
                }
            }).show(getSupportFragmentManager(), "VIEW_IMAGE");
        }
    }

    public void onViewLocationClicked(View view) {
        if (moodLocation == null) {
            toast("Choose a location first");
            return;
        } else {
            Mood newMood = moodFactory.createMood(moodId, moodEmotion, moodDate, moodReason, moodLocation, moodSocialSituation, moodHasImage);
            Intent intent = new Intent(this, ViewLocationActivity.class);
            intent.putExtra("mood", newMood);
            startActivityForResult(intent, VIEW_LOCATION);
        }
    }

    public void updatePhotoButtons(){
        if(moodImage != null){
            galleryButton.setVisibility(View.INVISIBLE);
            cameraButton.setVisibility(View.INVISIBLE);
            photoViewButton.setVisibility(View.VISIBLE);
        } else{
            galleryButton.setVisibility(View.VISIBLE);
            cameraButton.setVisibility(View.VISIBLE);
            photoViewButton.setVisibility(View.INVISIBLE);
        }
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
