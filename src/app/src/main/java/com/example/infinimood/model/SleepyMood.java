package com.example.infinimood.model;

import android.location.Location;
import android.media.Image;

import java.util.Date;

public class SleepyMood extends Mood {

    public SleepyMood(String id,
                     Date date,
                     String reason,
                     Location location,
                     String social_situation,
                     Image image)
    {
        super(id, date, reason, location, social_situation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.sleepy_string);
        super.setIcon(constants.sleepy_icon);
        super.setColor(constants.sleepy_color);
    }
}