package com.example.infinimood.view;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FilterCallback;
import com.example.infinimood.controller.GetMoodCallback;
import com.example.infinimood.controller.GetMoodsCallback;
import com.example.infinimood.controller.GetUsersCallback;
import com.example.infinimood.controller.MoodHistoryAdapter;
import com.example.infinimood.fragment.MoodHistoryFragment;
import com.example.infinimood.fragment.FilterFragment;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodComparator;
import com.example.infinimood.model.MoodConstants;
import com.example.infinimood.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * MoodHistoryActivity.java
 * Activity for viewing your mood events in a ListView
 */

public class MoodHistoryActivity extends MoodCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MoodHistoryActivity";

    private Spinner modeSpinner;
    private ListView moodListView;
    private View mapView;
    private ToggleButton reverseToggle;

    private MoodHistoryAdapter adapter;

    private MoodComparator comparator = new MoodComparator();

    private ArrayList<User> users = new ArrayList<>();

    private ArrayList<Mood> selfMoods = new ArrayList<>();
    private ArrayList<Mood> friendsMoods = new ArrayList<>();

    private HashSet<String> filter = new HashSet<>();

    private ArrayList<Mood> filteredSelfMoods = new ArrayList<>();
    private ArrayList<Mood> filteredFriendsMoods = new ArrayList<>();

    private GoogleMap googleMap;
    private HashMap<String, Marker> markerHashMap = new HashMap<>();
    private FusedLocationProviderClient fusedLocationProviderClient;


    // true -> show own mood events, false -> show friends mood events
    private boolean selfMode = true;

    // runs when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        // set selected navbar item to mood history
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().getItem(2).setChecked(true);

        // ensure that a user is logged in
        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(LoginActivity.class);
        }

        MoodConstants constants = new MoodConstants();
        filter.add(constants.AFRAID_STRING);
        filter.add(constants.ANGRY_STRING);
        filter.add(constants.HAPPY_STRING);
        filter.add(constants.INLOVE_STRING);
        filter.add(constants.CRYING_STRING);
        filter.add(constants.SAD_STRING);
        filter.add(constants.SLEEPY_STRING);

        moodListView = findViewById(R.id.moodHistoryListView);

        mapView = findViewById(R.id.moodHistoryMap);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.moodHistoryMap);

        mapFragment.getMapAsync(this);

        // setup mode spinner
        modeSpinner = findViewById(R.id.moodHistoryModeSpinner);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String mode = (String) modeSpinner.getItemAtPosition(i);
                if (mode.equals("My Mood Events")) {
                    selfMode = true;
                    adapter = new MoodHistoryAdapter(MoodHistoryActivity.this, filteredSelfMoods);
                    moodListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    reverseToggle.setVisibility(View.VISIBLE);
                    moodListView.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.INVISIBLE);
                }
                else if (mode.equals("Friends\' Mood Events")) {
                    selfMode = false;
                    adapter = new MoodHistoryAdapter(MoodHistoryActivity.this, filteredFriendsMoods);
                    moodListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    reverseToggle.setVisibility(View.VISIBLE);
                    moodListView.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.INVISIBLE);
                }
                else if (mode.equals("My Mood Map")) {
                    selfMode = true;

                    googleMap.clear();
                    markerHashMap.clear();

                    for (Mood mood : filteredSelfMoods) {
                        Location loc = mood.getLocation();
                        if (loc == null) {
                            continue;
                        }

                        MarkerOptions markerOptions = getMarkerOptions(mood);
                        Marker marker = googleMap.addMarker(markerOptions);
                        marker.setTag(mood);

                        markerHashMap.put(mood.getId(), marker);
                    }

                    reverseToggle.setVisibility(View.INVISIBLE);
                    moodListView.setVisibility(View.INVISIBLE);
                    mapView.setVisibility(View.VISIBLE);
                }
                else if (mode.equals("Friends\' Mood Map")) {
                    selfMode = false;

                    googleMap.clear();
                    markerHashMap.clear();

                    for (Mood mood : filteredFriendsMoods) {
                        Location loc = mood.getLocation();
                        if (loc == null) {
                            continue;
                        }

                        MarkerOptions markerOptions = getMarkerOptions(mood);
                        Marker marker = googleMap.addMarker(markerOptions);
                        marker.setTag(mood);

                        markerHashMap.put(mood.getId(), marker);
                    }

                    reverseToggle.setVisibility(View.INVISIBLE);
                    moodListView.setVisibility(View.INVISIBLE);
                    mapView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e(TAG, "This shouldn't happen (empty mode spinner)");
            }
        });

        // setup reverse toggle
        reverseToggle = findViewById(R.id.moodHistorySortOrderButton);
        reverseToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                comparator.reverse();
                Collections.sort(filteredSelfMoods, comparator);
                Collections.sort(filteredFriendsMoods, comparator);
                adapter.notifyDataSetChanged();
            }
        });

        // setup filter button
        FloatingActionButton filterButton = findViewById(R.id.moodHistoryFilterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterFragment filterFragment = new FilterFragment(filter, new FilterCallback() {
                    @Override
                    public void onCallback(HashSet<String> newFilter) {
                        filter = newFilter;

                        filteredSelfMoods.clear();
                        for (Mood mood: selfMoods) {
                            if (filter.contains(mood.getMood())) {
                                filteredSelfMoods.add(mood);
                            }
                        }
                        Collections.sort(filteredSelfMoods, comparator);

                        filteredFriendsMoods.clear();
                        for (Mood mood: friendsMoods) {
                            if (filter.contains(mood.getMood())) {
                                filteredFriendsMoods.add(mood);
                            }
                        }
                        Collections.sort(filteredFriendsMoods, comparator);

                        adapter.notifyDataSetChanged();
                    }
                });
                filterFragment.show(getSupportFragmentManager(), "SHOW_FILTER");
            }
        });

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
        firebaseController.getUsers(new GetUsersCallback() {
            @Override
            public void onCallback(ArrayList<User> usersArrayList) {
                users = usersArrayList;

                selfMoods.clear();
                filteredSelfMoods.clear();
                friendsMoods.clear();
                filteredFriendsMoods.clear();

                for (User user : users) {
                    // current user
                    if (user.getUserID().equals(firebaseController.getCurrentUID())) {
                        firebaseController.refreshOtherUserMoods(user, new GetMoodsCallback() {
                            @Override
                            public void onCallback(ArrayList<Mood> moodsArrayList) {
                                selfMoods = moodsArrayList;

                                for (Mood mood : moodsArrayList) {
                                    mood.print();
                                }

                                for (Mood mood: selfMoods) {
                                    if (filter.contains(mood.getMood())) {
                                        filteredSelfMoods.add(mood);
                                    }
                                }

                                Collections.sort(filteredSelfMoods, comparator);

                                if (selfMode) {
                                    adapter = new MoodHistoryAdapter(MoodHistoryActivity.this, filteredSelfMoods);
                                    moodListView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                    // current user follows
                    else if (user.isCurrentUserFollows()) {
                        firebaseController.refreshOtherUserMoods(user, new GetMoodsCallback() {
                            @Override
                            public void onCallback(ArrayList<Mood> moodsArrayList) {
                                Mood mostRecent = null;
                                for (Mood mood : moodsArrayList) {
                                    if (mostRecent == null) {
                                        mostRecent = mood;
                                    } else {
                                        if (mood.getDate() > mostRecent.getDate()) {
                                            mostRecent = mood;
                                        }
                                    }
                                }
                                if (mostRecent != null) {
                                    friendsMoods.add(mostRecent);

                                    mostRecent.print();

                                    for (Mood mood: friendsMoods) {
                                        if (filter.contains(mood.getMood())) {
                                            filteredFriendsMoods.add(mood);
                                        }
                                    }

                                    Collections.sort(filteredFriendsMoods, comparator);

                                    if (!selfMode) {
                                        adapter = new MoodHistoryAdapter(MoodHistoryActivity.this, filteredFriendsMoods);
                                        moodListView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public String getMoodStringInfo(Mood mood) {
        return "You were ".concat(mood.getMood())
                .concat(" on ").concat(String.valueOf(mood.getDate()));
    }

    public MarkerOptions getMarkerOptions(Mood mood) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(mood.getLocation().getLatitude(),
                mood.getLocation().getLongitude()));
        markerOptions.title(getMoodStringInfo(mood));

        // get the color of the mood and turn it into a Hue
        String hexColor = mood.getColor();
        float hsv[] = new float[3];
        Color.colorToHSV(Color.parseColor(hexColor), hsv);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(hsv[0]));

        return markerOptions;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Mood mood = (Mood) marker.getTag();
        firebaseController.refreshMood(mood, new GetMoodCallback() {
            @Override
            public void onCallback(Mood mood) {
                new MoodHistoryFragment(mood, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean success) {
                        if (success) {
                            toast("Mood deleted");
                            marker.remove();
                            markerHashMap.remove(mood.getId());
                        } else {
                            toast("Could not delete mood");
                        }
                    }
                }).show(getSupportFragmentManager(), "SHOW_MOOD");
            }
        });
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.setOnMarkerClickListener(this);

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13.0f));
                }
            }
        });
    }

    /*
        NAVBAR FUNCTIONS
     */

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
