package com.example.infinimood.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.infinimood.R;
import com.example.infinimood.model.Mood;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 *  MoodHistoryAdapter.java
 *  Adapter for the ListView in MoodHistoryActivity
 *  TODO: Change the ListView to a RecyclerView (functionality for swiping, etc.)
 */

public class MoodHistoryAdapter extends ArrayAdapter<Mood> {

    private ArrayList<Mood> moods;
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d yyyy h:mm a", Locale.getDefault());

    public MoodHistoryAdapter(Context context, ArrayList<Mood> moods) {
        super(context, 0, moods);
        this.context = context;
        this.moods = moods;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;
        LayoutInflater inflator = ((android.app.Activity) this.context).getLayoutInflater();

        view = inflator.inflate(R.layout.historyentry, parent, false);

        Mood mood = moods.get(position);

        TextView mood_icon, mood_social_situation, mood_date, mood_reason;
        mood_icon = view.findViewById(R.id.moodEventIconTextView);
        mood_social_situation = view.findViewById(R.id.moodEventSocialSituationTextView);
        mood_date = view.findViewById(R.id.moodEventDateTextView);
        mood_reason = view.findViewById(R.id.moodEventReasonTextView);

        ConstraintLayout layout = (ConstraintLayout) mood_icon.getParent();

        mood_icon.setText(mood.getIcon());
        mood_social_situation.setText(mood.getSocialSituation());
        mood_date.setText(dateFormat.format(mood.getDate()));
        mood_reason.setText(mood.getReason());

        Drawable gradient = context.getDrawable(R.drawable.gradient);
        gradient.setTint(Color.parseColor(mood.getColor()));

        layout.setBackground(gradient);

        if (mood.getReason() == "") {
            mood_reason.setVisibility(View.GONE);
        } else {
            mood_reason.setVisibility(View.VISIBLE);
        }

        return view;
    }
}