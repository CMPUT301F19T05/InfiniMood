package com.example.infinimood.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.infinimood.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * ChooseLocationActivity.java
 * Activity for displaying your current location on google maps
 */

public class ChooseLocationActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener {
    private Button backButton;
    private Button currentButton;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap myMap;
    private LatLng selectedLocation;
    private Marker currentMarker = null;
    private static final int PICK_LOCATION = 2;
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        backButton = findViewById(R.id.locationBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        currentButton = findViewById(R.id.locationCurrentButton);
        currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task<Location> task = fusedLocationProviderClient.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            selectedLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                            Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 16.5f));

                            if (currentMarker != null) {
                                currentMarker.remove();
                            }

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(selectedLocation);
                            markerOptions.title("Selected Location");
                            currentMarker = myMap.addMarker(markerOptions);
                        }
                    }
                });
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        updateLastLocation();
    }

    private void updateLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    selectedLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.locationMap);
                    supportMapFragment.getMapAsync(ChooseLocationActivity.this);
                }
            }
        });
    }

    public void onConfirmClick(View view) {
        Intent intent = new Intent();

        if (selectedLocation != null) {
            intent.putExtra("ADDED", true);
            intent.putExtra("LAT", selectedLocation.latitude);
            intent.putExtra("LON", selectedLocation.longitude);
        } else {
            intent.putExtra("ADDED", false);
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        Intent intent = getIntent();

        boolean hasLocation = intent.getBooleanExtra("HAS_LOCATION", false);

        LatLng latLng = null;
        if (hasLocation) {
            Location location = intent.getParcelableExtra("location");
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }

        selectedLocation = latLng;
        MarkerOptions myMarker = new MarkerOptions().position(latLng).title("You");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.5f));

        if (currentMarker != null) {
            currentMarker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Selected Location");
        currentMarker = myMap.addMarker(markerOptions);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (currentMarker != null) {
                    currentMarker.remove();
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Selected Location");
                currentMarker = myMap.addMarker(markerOptions);
                selectedLocation = latLng;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLastLocation();
                }
                break;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Selected Location");
        myMap.addMarker(markerOptions);
        selectedLocation = latLng;
    }

    public void onClearClicked(View view) {
        currentMarker.remove();
        selectedLocation = null;
    }
}
