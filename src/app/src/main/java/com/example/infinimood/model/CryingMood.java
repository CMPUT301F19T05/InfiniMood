package com.example.infinimood.model;

import android.content.Context;
import android.location.Location;
import android.media.Image;

import com.example.infinimood.R;

import java.util.Date;

import androidx.core.content.ContextCompat;

public class CryingMood extends Mood {
    private String moodIcon;
    private int moodColor;

    public CryingMood(String id, Context c) {
        super(id);
        this.moodIcon = c.getString(R.string.crying_icon);
        this.moodColor = ContextCompat.getColor(c, R.color.crying_color);
    }

    public CryingMood(String id,
                      Date date,
                      String reason,
                      Location location,
                      Image image,
                      Context c)
    {
        super(id, date, reason, location, image);

        this.moodIcon = c.getString(R.string.crying_icon);
        this.moodColor = ContextCompat.getColor(c, R.color.crying_color);
    }


    public String getMoodIcon() {
        return this.moodIcon;
    }
    public int getMoodColor() {
        return this.moodColor;
    }
}