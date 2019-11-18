package com.example.infinimood.view;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.infinimood.R;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodComparator;
import com.example.infinimood.model.MoodConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.widget.Toast;

import java.util.Collections;
import java.util.Date;

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

    public MarkerOptions getMarkerOptions( Mood mood ) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position( new LatLng( mood.getLocation().getLatitude(),
                        mood.getLocation().getLongitude() ) );
        markerOptions.title( getMoodStringInfo( mood ) );
        // get the color of the mood and turn it into a Hue
        String color = mood.getColor();
        float hue[] = new float[3];
        int red = Integer.parseInt( color.substring(1, 3), 16 );
        int green = Integer.parseInt( color.substring(3, 5), 16 );
        int blue = Integer.parseInt( color.substring(5, 7), 16 );
        android.graphics.Color.RGBToHSV(
                red,
                green,
                blue,
                hue
        );
        markerOptions.icon( BitmapDescriptorFactory.defaultMarker(hue[1] * 360.0f) );
        return markerOptions;
    }

    public void toast( String str ) {
        Toast toast = Toast.makeText(getApplicationContext(),
                str,Toast.LENGTH_SHORT);
        toast.show();
    }

    public String getMoodStringInfo( Mood mood ) {
        return "You were ".concat( mood.getMood() )
                .concat(" on ").concat( mood.getDate().toString() );
    }

    public void toastMood( Mood mood ) {
        Toast toast = Toast.makeText(getApplicationContext(),
                getMoodStringInfo( mood ),Toast.LENGTH_SHORT);
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
            //CircleOptions circleOptions = getCircleOptions( moods.get(i) );
            //this.googleMap.addCircle( circleOptions );

            MarkerOptions markerOptions = getMarkerOptions(moods.get(i) );
            this.googleMap.addMarker( markerOptions );

            latlong = new LatLng( l.getLatitude(), l.getLongitude() );
        }

        this.googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( latlong, 15.0f ) );


        this.googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                String color = Integer.toHexString( circle.getFillColor() );
                LatLng loc = circle.getCenter();

                MoodComparator moodComparator = new MoodComparator();
                moodComparator.reverse();
                Collections.sort(moods, moodComparator);

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
