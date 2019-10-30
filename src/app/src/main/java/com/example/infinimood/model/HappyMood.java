package com.example.infinimood.model;

import android.content.Context;
import android.location.Location;
import android.media.Image;

import com.example.infinimood.R;

import java.util.Date;

import androidx.core.content.ContextCompat;

public class HappyMood extends Mood {
    private String moodString = "Happy";
    private String moodIcon;
    private int moodColor;

    public HappyMood(String id, Context c) {
        super(id);
        this.moodIcon = c.getString(R.string.happy_icon);
        this.moodColor = ContextCompat.getColor(c, R.color.happy_color);
    }

    public HappyMood(String id,
                      Date date,
                      String reason,
                      Location location,
                      Image image,
                      Context c)
    {
        super(id, date, reason, location, image);

        this.moodIcon = c.getString(R.string.happy_icon);
        this.moodColor = ContextCompat.getColor(c, R.color.happy_color);
    }

    public HappyMood(String id, Date date, String reason, String social_situation)
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