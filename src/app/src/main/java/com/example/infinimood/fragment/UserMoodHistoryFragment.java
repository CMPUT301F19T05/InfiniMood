package com.example.infinimood.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FilterCallback;
import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.controller.GetMoodsCallback;
import com.example.infinimood.controller.GetUsersCallback;
import com.example.infinimood.controller.MoodHistoryAdapter;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodComparator;
import com.example.infinimood.model.MoodConstants;
import com.example.infinimood.model.User;
import com.example.infinimood.view.MoodHistoryActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class UserMoodHistoryFragment extends Fragment {

    private static FirebaseController firebaseController = new FirebaseController();

    private User user;

    private TextView usernameMoodHistoryTextView;
    private ListView moodListView;
    private ToggleButton reverseToggle;
    private FrameLayout progressOverlayContainer;

    private MoodHistoryAdapter adapter;

    private MoodComparator comparator = new MoodComparator();

    private ArrayList<Mood> userMoods = new ArrayList<>();

    private HashSet<String> filter = new HashSet<>();

    private ArrayList<Mood> filteredUserMoods = new ArrayList<>();

    private GoogleMap googleMap;
    private HashMap<String, Marker> markerHashMap = new HashMap<>();
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_mood_history, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
        }

        usernameMoodHistoryTextView = view.findViewById(R.id.usernameMoodHistoryTextView);
        usernameMoodHistoryTextView.setText(user.getUsername() + "'s Moods");

        MoodConstants constants = new MoodConstants();
        filter.add(constants.AFRAID_STRING);
        filter.add(constants.ANGRY_STRING);
        filter.add(constants.HAPPY_STRING);
        filter.add(constants.INLOVE_STRING);
        filter.add(constants.CRYING_STRING);
        filter.add(constants.SAD_STRING);
        filter.add(constants.SLEEPY_STRING);

        moodListView = view.findViewById(R.id.fragmentMoodHistoryListView);

        progressOverlayContainer = view.findViewById(R.id.fragmentProgressOverlayContainer);

        reverseToggle = view.findViewById(R.id.fragmentMoodHistorySortOrderButton);
        reverseToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                comparator.reverse();
                Collections.sort(filteredUserMoods, comparator);
                adapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton filterButton = view.findViewById(R.id.fragmentMoodHistoryFilterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterFragment filterFragment = new FilterFragment(filter, new FilterCallback() {
                    @Override
                    public void onCallback(HashSet<String> newFilter) {
                        filter = newFilter;

                        filteredUserMoods.clear();
                        for (Mood mood : userMoods) {
                            if (filter.contains(mood.getMood())) {
                                filteredUserMoods.add(mood);
                            }
                        }
                        Collections.sort(filteredUserMoods, comparator);
                        adapter.notifyDataSetChanged();
                    }
                });
                filterFragment.show(getActivity().getSupportFragmentManager(), "SHOW_FILTER");
            }
        });

        moodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mood mood = adapter.getItem(position);
                new MoodHistoryFragment(mood, true, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean bool) {
                        if (bool) {
                            Toast.makeText(getActivity(), "Mood deleted", Toast.LENGTH_SHORT);
                            update();
                        } else {
                            Toast.makeText(getActivity(), "Could not delete mood", Toast.LENGTH_SHORT);
                        }
                    }
                }).show(getActivity().getSupportFragmentManager(), "SHOW_MOOD");
            }
        });
        update();
        return view;
    }

    public void showOverlay() {
        progressOverlayContainer.setVisibility(View.VISIBLE);
        progressOverlayContainer.bringToFront();
    }

    public void hideOverlay() {
        progressOverlayContainer.setVisibility(View.GONE);
    }

    public void update() {
        showOverlay();
        userMoods.clear();
        filteredUserMoods.clear();

        firebaseController.refreshOtherUserMoods(user, new GetMoodsCallback() {
            @Override
            public void onCallback(ArrayList<Mood> moodsArrayList) {
                userMoods = moodsArrayList;

                for (Mood mood : moodsArrayList) {
                    mood.print();
                }

                for (Mood mood : userMoods) {
                    if (filter.contains(mood.getMood())) {
                        filteredUserMoods.add(mood);
                    }
                }

                Collections.sort(filteredUserMoods, comparator);


                adapter = new MoodHistoryAdapter(getActivity(), filteredUserMoods);
                moodListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                hideOverlay();
            }
        });
    }
}
