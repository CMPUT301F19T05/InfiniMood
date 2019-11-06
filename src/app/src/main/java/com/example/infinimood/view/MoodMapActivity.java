package com.example.infinimood.view;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.infinimood.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;

import static com.example.infinimood.view.MoodCompatActivity.moods;

public class MoodMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Location l;
        LatLng latlong = new LatLng(0,0);
        for (int i = 0; i < moods.size(); i++) {
            l = moods.get(i).getLocation();
            if( l == null ) {
                continue;
            }
            Log.i("", String.valueOf( l.getLatitude() ));
            Log.i("", String.valueOf( l.getLongitude() ));
            latlong = new LatLng( l.getLatitude(), l.getLongitude() );
            mMap.addMarker( new MarkerOptions().position( latlong ).title( moods.get(i).getMood() ) );
        }

        // Kind of random zoom preferences
        mMap.setMaxZoomPreference(20);
        mMap.setMinZoomPreference(15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng( latlong ));

        if( latlong.latitude == 0.0 && latlong.longitude == 0.0 ) {
            Log.i("", "No moods");
        }
        else {
            Log.i("", "Got a Mood");
        }
    }
}
