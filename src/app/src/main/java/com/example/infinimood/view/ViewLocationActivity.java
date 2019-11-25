package com.example.infinimood.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.GetMoodCallback;
import com.example.infinimood.fragment.MoodHistoryFragment;
import com.example.infinimood.model.Mood;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import static com.example.infinimood.view.MoodCompatActivity.firebaseController;
import static com.example.infinimood.view.MoodCompatActivity.moods;

public class ViewLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "ViewLocationActivity";

    private GoogleMap googleMap;

    private Mood mood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        mood = intent.getParcelableExtra("mood");
    }

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
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
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