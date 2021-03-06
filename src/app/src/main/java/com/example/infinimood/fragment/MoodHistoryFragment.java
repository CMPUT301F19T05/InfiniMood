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
import com.example.infinimood.controller.BitmapCallback;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.model.Mood;
import com.example.infinimood.view.AddEditMoodActivity;
import com.example.infinimood.view.ViewLocationActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * MoodHistoryFragment.java
 * Fragment for displaying Mood information. Also allows deleting or editing the mood. Used in
 * MoodHistoryActivity and MoodMapActivity.
 */
public class MoodHistoryFragment extends DialogFragment {

    private static final String TAG = "MoodHistoryFragment";

    // request codes
    protected static final int ADD_MOOD = 3;
    protected static final int EDIT_MOOD = 4;
    protected static final int VIEW_LOCATION = 5;

    private FirebaseController firebaseController = new FirebaseController();

    private Mood mood;
    private BooleanCallback onDeleteCallback;
    private boolean selfMode = false;

    /**
     * MoodHistoryFragment
     * Simple constructor for MoodHistoryFragment
     * @param mood Mood - The mood to be displayed
     * @param onDeleteCallback BooleanCallback - Boolean callback for indicating success or failure
     */
    public MoodHistoryFragment(Mood mood, boolean selfMode, BooleanCallback onDeleteCallback) {
        this.mood = mood;
        this.selfMode = selfMode;
        this.onDeleteCallback = onDeleteCallback;
    }

    /**
     * onCreateDialog
     * Overrides onCreateDialog. Fills the fields of the fragment with the corresponding data from
     * the mood.
     * @param savedInstanceState Bundle
     * @return Dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mood_history, null);

        TextView moodTextView = view.findViewById(R.id.moodFragmentMoodFieldTextView);
        TextView socialSituationTextView = view.findViewById(R.id.moodFragmentSocialSituationFieldTextView);
        TextView reasonTextView = view.findViewById(R.id.moodFragmentReasonFieldTextView);
        TextView timeTextView = view.findViewById(R.id.moodFragmentTimeFieldTextView);
        TextView dateTextView = view.findViewById(R.id.moodFragmentDateFieldTextView);
        Button viewLocationButton = view.findViewById(R.id.moodFragmentLocationButton);
        Button viewImageButton = view.findViewById(R.id.moodFragmentImageButton);

        Date date = new Date(mood.getDate());

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d yyyy", Locale.getDefault());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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

        if (mood.getSocialSituation().equals("None")) {
            socialSituationTextView.setVisibility(View.GONE);
            view.findViewById(R.id.moodFragmentSocialSituationTextView).setVisibility(View.GONE);
        } else {
            socialSituationTextView.setText(mood.getSocialSituation());
        }

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

        if (mood.hasImage()) {
            viewImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ViewImageFragment(mood).show(getActivity().getSupportFragmentManager(), "VIEW_IMAGE");
                }
            });
        } else {
            viewImageButton.setVisibility(View.GONE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setPositiveButton("OK", null);

        if (selfMode) {
            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    firebaseController.deleteMoodEventFromDB(mood, onDeleteCallback);
                }
            });
            builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getActivity(), AddEditMoodActivity.class);

                    intent.putExtra("requestCode", EDIT_MOOD);
                    intent.putExtra("mood", mood);

                    getActivity().startActivityForResult(intent, EDIT_MOOD);
                }
            });
        }

        return builder.create();
    }
}
