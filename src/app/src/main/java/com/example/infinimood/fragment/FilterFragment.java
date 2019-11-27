package com.example.infinimood.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.infinimood.R;
import com.example.infinimood.controller.FilterCallback;
import com.example.infinimood.model.MoodConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * FilterFragment.java
 * Fragment for filtering out certain moods from a display. Used in MoodHistoryActivity and
 * in MoodMapActivity
 */
public class FilterFragment extends DialogFragment {

    private HashSet<String> filter;
    private FilterCallback filterCallback;
    private boolean filtered;

    private MoodConstants constants;

    private CheckBox angryCheckbox;
    private CheckBox sadCheckbox;
    private CheckBox happyCheckbox;
    private CheckBox afraidCheckbox;
    private CheckBox sleepyCheckbox;
    private CheckBox inLoveCheckbox;
    private CheckBox cryingCheckbox;

    private Button allButton;
    private Button noneButton;

    /**
     * FilterFragment
     * Simple constructor for FilterFragment
     * @param filter HashSet<String> - A set of strings representing the possible filters
     * @param callback FilterCallback - A filter callback to let other activities know which
     *                 filters were selected
     */
    public FilterFragment(HashSet<String> filter, FilterCallback callback) {
        this.filter = filter;
        this.filterCallback = callback;
    }

    /**
     * onCreateDialog
     * Overrides onCreateDialog. Is called when the fragment is created. Begins by setting the
     * filters according to the private filter string ArrayList, then sets an onCheckedChange
     * listener for each one to implement the logic of custom filtering.
     * @param savedInstanceState Bundle
     * @return Dialog
     */
    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_filter, null);

        constants = new MoodConstants();

        allButton = view.findViewById(R.id.filterFragmentAllButton);
        noneButton = view.findViewById(R.id.filterFragmentNoneButton);

        angryCheckbox = view.findViewById(R.id.angryCheck);
        angryCheckbox.setChecked(filter.contains(constants.ANGRY_STRING));

        sadCheckbox = view.findViewById(R.id.sadCheck);
        sadCheckbox.setChecked(filter.contains(constants.SAD_STRING));

        happyCheckbox = view.findViewById(R.id.happyCheck);
        happyCheckbox.setChecked(filter.contains(constants.HAPPY_STRING));

        afraidCheckbox = view.findViewById(R.id.afraidCheck);
        afraidCheckbox.setChecked(filter.contains(constants.AFRAID_STRING));

        sleepyCheckbox = view.findViewById(R.id.sleepyCheck);
        sleepyCheckbox.setChecked(filter.contains(constants.SLEEPY_STRING));

        inLoveCheckbox = view.findViewById(R.id.inLoveCheck);
        inLoveCheckbox.setChecked(filter.contains(constants.INLOVE_STRING));

        cryingCheckbox = view.findViewById(R.id.cryingCheck);
        cryingCheckbox.setChecked(filter.contains(constants.CRYING_STRING));

        angryCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filter.add(constants.ANGRY_STRING);
                } else {
                    filter.remove(constants.ANGRY_STRING);
                }
            }
        });

        sadCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filter.add(constants.SAD_STRING);
                } else {
                    filter.remove(constants.SAD_STRING);
                }
            }
        });
        happyCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filter.add(constants.HAPPY_STRING);
                } else {
                    filter.remove(constants.HAPPY_STRING);
                }
            }
        });
        afraidCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filter.add(constants.AFRAID_STRING);
                } else {
                    filter.remove(constants.AFRAID_STRING);
                }
            }
        });
        inLoveCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filter.add(constants.INLOVE_STRING);
                } else {
                    filter.remove(constants.INLOVE_STRING);
                }
            }
        });
        cryingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filter.add(constants.CRYING_STRING);
                } else {
                    filter.remove(constants.CRYING_STRING);
                }
            }
        });
        sleepyCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filter.add(constants.SLEEPY_STRING);
                } else {
                    filter.remove(constants.SLEEPY_STRING);
                }
            }
        });

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter.clear();
                angryCheckbox.setChecked(true);
                filter.add(constants.ANGRY_STRING);
                sadCheckbox.setChecked(true);
                filter.add(constants.SAD_STRING);
                happyCheckbox.setChecked(true);
                filter.add(constants.HAPPY_STRING);
                afraidCheckbox.setChecked(true);
                filter.add(constants.AFRAID_STRING);
                sleepyCheckbox.setChecked(true);
                filter.add(constants.SLEEPY_STRING);
                inLoveCheckbox.setChecked(true);
                filter.add(constants.INLOVE_STRING);
                cryingCheckbox.setChecked(true);
                filter.add(constants.CRYING_STRING);
            }
        });

        noneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter.clear();
                angryCheckbox.setChecked(false);
                sadCheckbox.setChecked(false);
                happyCheckbox.setChecked(false);
                afraidCheckbox.setChecked(false);
                sleepyCheckbox.setChecked(false);
                inLoveCheckbox.setChecked(false);
                cryingCheckbox.setChecked(false);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        filterCallback.onCallback(filter);
                        dismiss();
                    }
                })
                .create();
    }
}
