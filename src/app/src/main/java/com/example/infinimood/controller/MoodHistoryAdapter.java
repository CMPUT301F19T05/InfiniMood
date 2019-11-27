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
 * MoodHistoryAdapter.java
 * Adapter for the ListView in MoodHistoryActivity
 * TODO: Change the ListView to a RecyclerView (functionality for swiping, etc.)
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
        View view = convertView;
        LayoutInflater inflator = ((android.app.Activity) this.context).getLayoutInflater();

        view = inflator.inflate(R.layout.historyentry, parent, false);

        Mood mood = moods.get(position);

        TextView moodIcon, moodEmotion, moodDate, moodReason;
        moodIcon = view.findViewById(R.id.moodEventIconTextView);
        moodEmotion = view.findViewById(R.id.moodEventEmotionTextView);
        moodDate = view.findViewById(R.id.moodEventDateTextView);
        moodReason = view.findViewById(R.id.moodEventReasonTextView);

        ConstraintLayout layout = (ConstraintLayout) moodIcon.getParent();

        moodIcon.setText(mood.getIcon());
        moodEmotion.setText(mood.getMood());
        moodDate.setText(dateFormat.format(mood.getDate()));
        moodReason.setText(mood.getReason());

        Drawable shape = context.getDrawable(R.drawable.listshape);
        shape.setTint(Color.parseColor(mood.getColor()));

        layout.setBackground(shape);

        if (mood.getReason().equals("")) {
            moodReason.setVisibility(View.GONE);
        } else {
            moodReason.setVisibility(View.VISIBLE);
        }

        return view;
    }

}
