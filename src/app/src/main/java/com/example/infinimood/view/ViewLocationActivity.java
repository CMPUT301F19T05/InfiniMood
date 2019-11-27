package com.example.infinimood.view;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.infinimood.R;
import com.example.infinimood.model.Mood;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * ViewLocationActivity.java
 * Activity for viewing a location
 */
public class ViewLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "ViewLocationActivity";

    private GoogleMap googleMap;

    private Mood mood;

    /**
     * onCreate
     * Ovverides onCreate. Gets the activity ready. Runs when activity is created.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.moodMapMap);

        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        mood = intent.getParcelableExtra("mood");
    }

    /**
     * getMarkerOptions
     * Gets a set of marker options from a mood
     * @param mood Mood - mood to help choose marker options
     * @return MarkerOptions - the desired MarkerOptions
     */
    public MarkerOptions getMarkerOptions(Mood mood) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(mood.getLocation().getLatitude(),
                mood.getLocation().getLongitude()));

        Location l = mood.getLocation();
        markerOptions.title(l.getLatitude() + " , " + l.getLongitude());

        // get the color of the mood and turn it into a Hue
        String hexColor = mood.getColor();
        float hsv[] = new float[3];
        Color.colorToHSV(Color.parseColor(hexColor), hsv);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(hsv[0]));

        return markerOptions;
    }

    /**
     * onMapReady
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * @param googleMap GoogleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        Location l = mood.getLocation();

        if (l == null) {
            finish();
        }

        MarkerOptions markerOptions = getMarkerOptions(mood);
        Marker marker = this.googleMap.addMarker(markerOptions);
        marker.setTag(mood);

        LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }
}
