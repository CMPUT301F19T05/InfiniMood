package com.example.infinimood.model;

import android.location.Location;
import android.media.Image;

import java.util.Date;

/**
 *  AfraidMood.java
 *  Subclass for Afraid mood events
 */

public class AfraidMood extends Mood {

    public AfraidMood(String id,
                     Date date,
                     String reason,
                     Location location,
                     String social_situation,
                     Image image)
    {
        super(id, date, reason, location, social_situation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.afraid_string);
        super.setIcon(constants.afraid_icon);
        super.setColor(constants.afraid_color);
    }
}