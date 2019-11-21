package com.example.infinimood.view;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.GetMoodCallback;
import com.example.infinimood.fragment.MoodHistoryFragment;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.widget.Toast;

import java.util.Collections;
import java.util.HashMap;

import static com.example.infinimood.view.MoodCompatActivity.firebaseController;
import static com.example.infinimood.view.MoodCompatActivity.moods;

public class MoodMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MoodMapActivity";

    private GoogleMap googleMap;
    private HashMap<String, Mood> moodHashMap = new HashMap<>();
    private HashMap<String, Marker> markerHashMap = new HashMap<>();

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
//        markerOptions.title( getMoodStringInfo( mood ) );
        markerOptions.title(mood.getId());
        moodHashMap.put(mood.getId(), mood);
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
                .concat(" on ").concat( String.valueOf(mood.getDate()) );
    }

    public void toastMood( Mood mood ) {
        Toast toast = Toast.makeText(getApplicationContext(),
                getMoodStringInfo( mood ),Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        String id = marker.getTitle();
        Mood mood = moodHashMap.get(id);
        markerHashMap.put(id, marker);
        firebaseController.refreshMood(mood, new GetMoodCallback() {
            @Override
            public void onCallback(Mood mood) {
                moodHashMap.put(id, mood);
                new MoodHistoryFragment(mood, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean success) {
                        if (success) {
                            toast("Mood deleted");
                            moodHashMap.remove(mood.getId());
                            marker.remove();
                        } else {
                            toast("Could not delete mood");
                        }
                    }
                }, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean success) {
                        if (success) {
                            firebaseController.refreshMood(mood, new GetMoodCallback() {
                                @Override
                                public void onCallback(Mood mood) {
                                    Log.i(TAG, marker.getPosition().toString());
                                    marker.setPosition(new LatLng(mood.getLocation().getLatitude(), mood.getLocation().getLongitude()));
                                    Log.i(TAG, marker.getPosition().toString());
                                    moodHashMap.put(mood.getId(), mood);
                                }
                            });
                        }
                    }
                }).show(getSupportFragmentManager(), "SHOW_MOOD");
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "This is running");
        Log.i(TAG, String.valueOf(requestCode));
        Log.i(TAG, String.valueOf(resultCode));
        Log.i(TAG, data.getStringExtra("moodId"));
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "After super");
        Log.i(TAG, String.valueOf(requestCode));
        Log.i(TAG, String.valueOf(resultCode));
        Log.i(TAG, data.getStringExtra("moodId"));

        if (requestCode == 1) {
            Log.i(TAG, "This is also running");
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "This is also also running");
                String moodId = data.getStringExtra("moodId");
                Mood mood = moodHashMap.get(moodId);
                firebaseController.refreshMood(mood, new GetMoodCallback() {
                    @Override
                    public void onCallback(Mood mood) {
                        Log.i(TAG, "This is also also also running");
                        mood.print();
                        markerHashMap.get(mood.getId()).remove();
                        markerHashMap.remove(mood.getId());
                        MarkerOptions markerOptions = getMarkerOptions(mood);
                        googleMap.addMarker( markerOptions );
                        moodHashMap.put(mood.getId(), mood);
                    }
                });
            }
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

        Location l;
        LatLng latlong = new LatLng(0,0);
        for (int i = 0; i < moods.size(); i++) {
            l = moods.get(i).getLocation();
            if( l == null ) {
                continue;
            }

            MarkerOptions markerOptions = getMarkerOptions(moods.get(i) );
            this.googleMap.addMarker( markerOptions );

            latlong = new LatLng( l.getLatitude(), l.getLongitude() );
        }

        this.googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( latlong, 15.0f ) );
    }
}
