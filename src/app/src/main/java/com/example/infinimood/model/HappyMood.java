package com.example.infinimood.model;

import android.location.Location;
import android.media.Image;

import java.util.Date;

public class HappyMood extends Mood {

    public HappyMood(String id,
                      Date date,
                      String reason,
                      Location location,
                      String social_situation,
                      Image image)
    {
        super(id, date, reason, location, social_situation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.happy_string);
        super.setIcon(constants.happy_icon);
        super.setColor(constants.happy_color);
    }
}