package com.example.infinimood.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.infinimood.R;
import com.example.infinimood.model.Mood;

import java.util.ArrayList;

public class MoodHistoryAdapter extends ArrayAdapter<Mood> {
    private ArrayList<Mood> moods;
    private Context context;

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

        TextView emoticon = view.findViewById(R.id.emoticon_view);
//        emoticon.setText(mood.getImage());

        TextView social_situ = view.findViewById(R.id.social_situ_view);
//        social_situ.setText(mood.getSocialSitu());

        TextView reason = view.findViewById(R.id.reason_view);
//        reason.setText(mood.getReason());

        TextView location = view.findViewById(R.id.location_view);
//        location.setText(mood.getLocation());

        return view;
    }
}