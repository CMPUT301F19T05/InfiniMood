package com.example.infinimood.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.infinimood.R;
import com.example.infinimood.controller.FilterCallback;
import com.example.infinimood.model.MoodConstants;

import java.util.ArrayList;

public class FilterFragment extends DialogFragment {
    private ArrayList<String> filter;
    private FilterCallback filterCallback;
    public FilterFragment(ArrayList<String> filter, FilterCallback callback) {
        this.filter = filter;
        this.filterCallback = callback;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_filter, null);

        MoodConstants constants = new MoodConstants();

        CheckBox angryCheckbox = view.findViewById(R.id.angryCheck);
        angryCheckbox.setChecked(filter.contains(constants.ANGRY_STRING));

        CheckBox sadCheckbox = view.findViewById(R.id.sadCheck);
        sadCheckbox.setChecked(filter.contains(constants.SAD_STRING));

        CheckBox happyCheckbox = view.findViewById(R.id.happyCheck);
        happyCheckbox.setChecked(filter.contains(constants.HAPPY_STRING));

        CheckBox afraidCheckbox = view.findViewById(R.id.afraidCheck);
        afraidCheckbox.setChecked(filter.contains(constants.AFRAID_STRING));

        CheckBox sleepyCheckbox = view.findViewById(R.id.sleepyCheck);
        sleepyCheckbox.setChecked(filter.contains(constants.SLEEPY_STRING));

        CheckBox inLoveCheckbox = view.findViewById(R.id.inLoveCheck);
        inLoveCheckbox.setChecked(filter.contains(constants.INLOVE_STRING));

        CheckBox cryingCheckbox = view.findViewById(R.id.cryingCheck);
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
