package com.example.infinimood.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.model.Mood;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MoodHistoryFragment extends DialogFragment {

    private FirebaseController firebaseController = new FirebaseController();

    private TextView moodDateTextView;
    private TextView moodMoodTextView;
    private TextView moodReasonTextView;
    private TextView moodLocationTextView;
    private TextView moodSocialSituationTextView;
    private ImageView moodImageImageView;

    private Mood mood;
    private Context context;

    private Date moodDate;
    private String moodMood;
    private String moodReason = "";
    private Location moodLocation = null;
    private String moodSocialSituation;
    private Bitmap moodImage = null;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d yyyy h:mm a");

    public MoodHistoryFragment(Mood mood, Context context) {
        this.mood = mood;
        this.context = context;

        this.moodDate = mood.getDate();
        this.moodMood = mood.getMood();
        this.moodReason = mood.getReason();
        this.moodLocation = mood.getLocation();
        this.moodSocialSituation = mood.getSocialSituation();
        this.moodImage = mood.getImage();
    }

    public void toast(String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
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
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseController.deleteMoodEventFromDB(mood, new BooleanCallback() {
                            @Override
                            public void onCallback(boolean bool) {
                                if (bool) {
                                    toast("Mood deleted");
                                } else {
                                    toast("Could not delete mood");
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("Edit", null)
                .setPositiveButton("OK", null)
                .create();
    }


    private String locationToString(Location location) {
        return String.valueOf(location.getLatitude()).concat(",").concat(String.valueOf(location.getLongitude()));
    }
}
