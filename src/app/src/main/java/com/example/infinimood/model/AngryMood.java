package com.example.infinimood.model;

import android.location.Location;
import android.media.Image;

import java.util.Date;

/**
 *  AngryMood.java
 *  Subclass for Angry mood events
 */

public class AngryMood extends Mood {

    public AngryMood(String id,
                     Date date,
                     String reason,
                     Location location,
                     String social_situation,
                     Image image)
    {
        super(id, date, reason, location, social_situation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.angry_string);
        super.setIcon(constants.angry_icon);
        super.setColor(constants.angry_color);
    }
}