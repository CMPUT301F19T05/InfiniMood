package com.example.infinimood.view;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.infinimood.R;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import android.widget.Toast;

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

    // Helper method that takes a color string in hex format
    // and turns it into a tranparent color
    public int makeColorTransparent(String color) {
        Log.i("", color);
        String transparentColor = "#33".concat( color.substring(1) );
        return Color.parseColor( transparentColor );
    }

    // setup the circle to be displayed on the map
    public CircleOptions getCircleOptions( Mood mood ) {
        MoodConstants moodConstants = new MoodConstants();
        CircleOptions c = new CircleOptions();
        LatLng loc = new LatLng( mood.getLocation().getLatitude(), mood.getLocation().getLongitude() );
        c.center( loc );
        c.radius( 200 ); // in meters
        c.strokeColor( Color.BLACK );
        c.fillColor( makeColorTransparent( mood.getColor() ) );
        c.clickable(true);

        return c;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * Currently go through all Moods, and add fun circles to the map
     * for each of them, still need to set the onClickListener to do
     * the right stuff
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
            CircleOptions circleOptions = getCircleOptions( moods.get(i) );

            Log.i("", String.valueOf( l.getLatitude() ));
            Log.i("", String.valueOf( l.getLongitude() ));
            latlong = new LatLng( l.getLatitude(), l.getLongitude() );
            mMap.addCircle( circleOptions );
        }

        // Kind of random zoom preferences based on what I liked
        mMap.setMaxZoomPreference(20);
        mMap.setMinZoomPreference(15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng( latlong ));

        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                /* TODO: Simply iterate through the moods to find the most recent
                mood with the same color and location, and print some information
                about it. Perhaps the toast could be clickable allowing the user to
                edit the mood.
                 */
                Toast toast = Toast.makeText(getApplicationContext(),
                        "This is your Mood",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
