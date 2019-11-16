package com.example.infinimood.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.infinimood.R;
import com.example.infinimood.model.Mood;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MoodHistoryFragment extends DialogFragment {

    private TextView moodDateTextView;
    private TextView moodMoodTextView;
    private TextView moodReasonTextView;
    private TextView moodLocationTextView;
    private TextView moodSocialSituationTextView;
    private ImageView moodImageImageView;

    private Date moodDate;
    private String moodMood;
    private String moodReason = "";
    private Location moodLocation = null;
    private String moodSocialSituation;
    private Bitmap moodImage = null;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d yyyy h:mm a");

    public MoodHistoryFragment(Mood mood) {
        this.moodDate = mood.getDate();
        this.moodMood = mood.getMood();
        this.moodReason = mood.getReason();
        this.moodLocation = mood.getLocation();
        this.moodSocialSituation = mood.getSocialSituation();
        this.moodImage = mood.getImage();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mood_history, null);
        moodDateTextView = view.findViewById(R.id.moodDateTextView);
        moodMoodTextView = view.findViewById(R.id.moodMoodTextView);
        moodReasonTextView = view.findViewById(R.id.moodReasonTextView);
        moodLocationTextView = view.findViewById(R.id.moodLocationTextView);
        moodSocialSituationTextView = view.findViewById(R.id.moodSocialSituationTextView);
        moodImageImageView = view.findViewById(R.id.moodImageImageView);

        moodDateTextView.setText(dateFormat.format(moodDate));
        moodMoodTextView.setText(moodMood);
        moodReasonTextView.setText(moodReason);
        if (moodLocation != null) {
            moodLocationTextView.setText(locationToString(moodLocation));
        }
        moodSocialSituationTextView.setText(moodSocialSituation);
        if(moodImage != null){
            Log.i("test", "image error");
        }
        moodImageImageView.setImageBitmap(moodImage);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Mood")
                .setNegativeButton("OK", null)
                .create();
    }


    private String locationToString(Location location) {
        return String.valueOf(location.getLatitude()).concat(",").concat(String.valueOf(location.getLongitude()));
    }
}
