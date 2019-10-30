package com.example.infinimood.model;

import android.content.Context;
import android.location.Location;
import android.media.Image;

import com.example.infinimood.R;

import java.util.Date;

public class InLoveMood extends Mood {
    private String moodIcon;
    private String moodColor;

    public InLoveMood(String id, Context c) {
        super(id);
        this.moodIcon = c.getString(R.string.in_love_icon);
        this.moodColor = c.getString(R.string.in_love_color);
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
        this.moodColor = c.getString(R.string.in_love_color);
    }


    public String getMoodIcon() {
        return this.moodIcon;
    }
    public String getMoodColor() {
        return this.moodColor;
    }
}