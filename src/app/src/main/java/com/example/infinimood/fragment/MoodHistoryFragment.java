package com.example.infinimood.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.model.Mood;
import com.example.infinimood.view.AddEditMoodActivity;
import com.example.infinimood.view.ViewLocationActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MoodHistoryFragment extends DialogFragment {

    private static final String TAG = "MoodHistoryFragment";

    // request codes
    protected static final int ADD_MOOD = 3;
    protected static final int EDIT_MOOD = 4;
    protected static final int VIEW_LOCATION = 5;

    private FirebaseController firebaseController = new FirebaseController();

    private Mood mood;
    private BooleanCallback onDeleteCallback;

    public MoodHistoryFragment(Mood mood, BooleanCallback onDeleteCallback) {
        this.mood = mood;
        this.onDeleteCallback = onDeleteCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mood_history, null);

        TextView moodTextView = view.findViewById(R.id.moodFragmentMoodFieldTextView);
        TextView socialSituationTextView = view.findViewById(R.id.moodFragmentSocialSituationFieldTextView);
        TextView reasonTextView = view.findViewById(R.id.moodFragmentReasonFieldTextView);
        TextView timeTextView = view.findViewById(R.id.moodFragmentTimeFieldTextView);
        TextView dateTextView = view.findViewById(R.id.moodFragmentDateFieldTextView);
        ImageView imageImageView = view.findViewById(R.id.moodImageImageView);
        Button viewLocationButton = view.findViewById(R.id.moodFragmentLocationButton);

        Date date = new Date(mood.getDate());

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String timeString = timeFormat.format(date);
        String dateString = dateFormat.format(date);

        timeTextView.setText(timeString);
        dateTextView.setText(dateString);

        String moodText = mood.getIcon() + ' ' + mood.getMood();
        moodTextView.setText(moodText);

        if (!mood.getReason().equals("")) {
            reasonTextView.setText(mood.getReason());
        } else {
            reasonTextView.setVisibility(View.GONE);
            view.findViewById(R.id.moodFragmentReasonTextView).setVisibility(View.GONE);
        }

        socialSituationTextView.setText(mood.getSocialSituation());

        imageImageView.setImageBitmap(mood.getImage());

        if (mood.getLocation() == null) {
            viewLocationButton.setVisibility(View.GONE);
        } else {
            viewLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ViewLocationActivity.class);
                    intent.putExtra("mood", mood);
                    getActivity().startActivityForResult(intent, VIEW_LOCATION);
                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
//                .setTitle("Mood")
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseController.deleteMoodEventFromDB(mood, onDeleteCallback);
                    }
                })
                .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), AddEditMoodActivity.class);

                        intent.putExtra("requestCode", EDIT_MOOD);
                        intent.putExtra("mood", mood);

                        getActivity().startActivityForResult(intent, EDIT_MOOD);
                    }
                })
                .setPositiveButton("OK", null)
                .create();
    }

    private String locationToString(Location location) {
        return String.valueOf(location.getLatitude()).concat(",").concat(String.valueOf(location.getLongitude()));
    }
}
