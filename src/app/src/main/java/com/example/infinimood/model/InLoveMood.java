package com.example.infinimood.model;

import android.content.Context;
import android.location.Location;
import android.media.Image;

import com.example.infinimood.R;

import java.util.Date;

import androidx.core.content.ContextCompat;

public class InLoveMood extends Mood {
    private String moodString = "In Love";
    private String moodIcon;
    private int moodColor;

    public InLoveMood(String id, Context c) {
        super(id);
        this.moodIcon = c.getString(R.string.in_love_icon);
        this.moodColor = ContextCompat.getColor(c, R.color.in_love_color);
    }

    public InLoveMood(String id,
                      Date date,
                      String reason,
                      Location location,
                      Image image,
                      Context c)
    {
        super(id, date, reason, location, image);

        this.moodIcon = c.getString(R.string.in_love_icon);
        this.moodColor = ContextCompat.getColor(c, R.color.in_love_color);
    }

    public InLoveMood(String id, Date date, String reason, String social_situation)
    {
        super(id, date, reason, social_situation);
    }

    public String getMoodIcon() {
        return this.moodIcon;
    }
    public int getMoodColor() {
        return this.moodColor;
    }
}