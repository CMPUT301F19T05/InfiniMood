package com.example.infinimood.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FilterCallback;
import com.example.infinimood.controller.GetMoodCallback;
import com.example.infinimood.fragment.FilterFragment;
import com.example.infinimood.fragment.MoodHistoryFragment;
import com.example.infinimood.model.Mood;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.example.infinimood.view.MoodCompatActivity.firebaseController;
import static com.example.infinimood.view.MoodCompatActivity.moods;

public class MoodMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MoodMapActivity";

    private GoogleMap googleMap;
    private FloatingActionButton filterButton;

    private HashMap<String, Marker> markerHashMap = new HashMap<>();

    private boolean filtered = false;
    private HashSet<String> filter = new HashSet<>();
    private ArrayList<Mood> filteredList = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        filterButton = findViewById(R.id.moodMapFilterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterFragment frag = new FilterFragment(filter, filtered, new FilterCallback() {
                    @Override
                    public void onCallback(HashSet<String> newFilter) {
                        filter = newFilter;
                        filteredList.clear();
                        for (Mood mood: moods) {
                            if (filter.contains(mood.getMood())) {
                                filteredList.add(mood);
                            }
                        }
                        filtered = true;
                        updateMoods(filteredList);
                    }
                });
                frag.show(getSupportFragmentManager(), "SHOW_FILTER");
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.moodMapMap);

        mapFragment.getMapAsync(this);
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

    public void toast(String str) {
        Toast toast = Toast.makeText(getApplicationContext(),
                str, Toast.LENGTH_SHORT);
        toast.show();
    }

    public String getMoodStringInfo(Mood mood) {
        return "You were ".concat(mood.getMood())
                .concat(" on ").concat(String.valueOf(mood.getDate()));
    }

    public void toastMood(Mood mood) {
        Toast toast = Toast.makeText(getApplicationContext(),
                getMoodStringInfo(mood), Toast.LENGTH_SHORT);
        toast.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Mood mood = data.getParcelableExtra("mood");

                Marker oldMarker = markerHashMap.get(mood.getId());
                oldMarker.remove();

                MarkerOptions markerOptions = getMarkerOptions(mood);
                Marker newMarker = googleMap.addMarker(markerOptions);
                newMarker.setTag(mood);
                markerHashMap.put(mood.getId(), newMarker);
            }
        }
    }

    public void updateMoods(ArrayList<Mood> selectedMoods) {
        googleMap.clear();
        markerHashMap.clear();

        Location l;
        LatLng latLng = new LatLng(0, 0);
        for (int i = 0; i < selectedMoods.size(); i++) {
            Mood mood = selectedMoods.get(i);

            l = mood.getLocation();
            if (l == null) {
                continue;
            }

            MarkerOptions markerOptions = getMarkerOptions(mood);
            Marker marker = this.googleMap.addMarker(markerOptions);
            marker.setTag(mood);

            markerHashMap.put(mood.getId(), marker);

            latLng = new LatLng(l.getLatitude(), l.getLongitude());
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.setOnMarkerClickListener(this);

        updateMoods(moods);

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
}
