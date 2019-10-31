package com.example.infinimood.model;

import android.location.Location;
import android.media.Image;

import java.util.Date;

public class SadMood extends Mood {

    public SadMood(String id,
                     Date date,
                     String reason,
                     Location location,
                     String social_situation,
                     Image image)
    {
        super(id, date, reason, location, social_situation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.sad_string);
        super.setIcon(constants.sad_icon);
        super.setColor(constants.sad_color);
    }
}