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

import android.location.Location;
import android.widget.Toast;

import static com.example.infinimood.view.MoodCompatActivity.firebaseController;
import static com.example.infinimood.view.MoodCompatActivity.moods;

public class MoodMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_map);

//        firebaseController.refreshUserMoods(moods);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Helper method that takes a color string in hex format
    // and turns it into a transparent color
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
        c.radius( 100 ); // in meters
        c.strokeColor( Color.BLACK );
        c.fillColor( makeColorTransparent( mood.getColor() ) );
        c.clickable(true);

        return c;
    }

    public void toast( String str ) {
        Toast toast = Toast.makeText(getApplicationContext(),
                str,Toast.LENGTH_SHORT);
        toast.show();
    }

    public void toastMood( Mood mood ) {
        String str = "You were ".concat( mood.getMood() )
                .concat(" on ").concat( mood.getDate().toString() );
        Toast toast = Toast.makeText(getApplicationContext(),
                str,Toast.LENGTH_SHORT);
        toast.show();
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
        this.googleMap = googleMap;

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
            this.googleMap.addCircle( circleOptions );
        }

        // Kind of random zoom preferences based on what I liked
        this.googleMap.setMaxZoomPreference(20);
        this.googleMap.setMinZoomPreference(15);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng( latlong ));

        this.googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                /* TODO: Simply iterate through the moods to find the most recent
                mood with the same color and location, and print some information
                about it. Perhaps the toast could be clickable allowing the user to
                edit the mood.
                 */
                String color = Integer.toHexString( circle.getFillColor() );
                LatLng loc = circle.getCenter();

                //toast( color );

                for(int i = 0; i < moods.size(); i++ ) {
                    Mood m = moods.get(i);
                    if (m.getLocation() == null) {
                        continue;
                    }

                    if( m.getColor().substring(1).equals( color.substring(2) ) ){
                        Log.i("", "color matched");
                        if( m.getLocation().getLatitude() == loc.latitude
                                && m.getLocation().getLongitude() == loc.longitude) {
                            toastMood( m );
                            break;
                        }
                    }
                }


            }
        });
    }
}
