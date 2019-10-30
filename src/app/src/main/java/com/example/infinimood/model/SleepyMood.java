package com.example.infinimood.model;

import android.content.Context;
import android.location.Location;
import android.media.Image;

import com.example.infinimood.R;

import java.util.Date;

import androidx.core.content.ContextCompat;

public class SleepyMood extends Mood {
    private String moodIcon;
    private int moodColor;

    public SleepyMood(String id, Context c) {
        super(id);
        this.moodIcon = c.getString(R.string.sleepy_icon);
        this.moodColor = ContextCompat.getColor(c, R.color.sleepy_color);
    }

    public SleepyMood(String id,
                    Date date,
                    String reason,
                    Location location,
                    Image image,
                    Context c)
    {
        super(id, date, reason, location, image);

        this.moodIcon = c.getString(R.string.sleepy_icon);
        this.moodColor = ContextCompat.getColor(c, R.color.sleepy_color);
    }


    public String getMoodIcon() {
        return this.moodIcon;
    }
    public int getMoodColor() {
        return this.moodColor;
    }
}
