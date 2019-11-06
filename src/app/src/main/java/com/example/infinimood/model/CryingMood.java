package com.example.infinimood.model;

import android.location.Location;
import android.media.Image;

import java.util.Date;

/**
 *  CryingMood.java
 *  Subclass for Crying mood events
 */

public class CryingMood extends Mood {

    public CryingMood(String id,
                     Date date,
                     String reason,
                     Location location,
                     String social_situation,
                     Image image)
    {
        super(id, date, reason, location, social_situation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.crying_string);
        super.setIcon(constants.crying_icon);
        super.setColor(constants.crying_color);
    }
}