package com.example.infinimood.model;

import android.location.Location;
import android.media.Image;

import java.util.Date;

/**
 *  InLoveMood.java
 *  Subclass for InLove mood events
 */

public class InLoveMood extends Mood {

    public InLoveMood(String id,
                     Date date,
                     String reason,
                     Location location,
                     String social_situation,
                     Image image)
    {
        super(id, date, reason, location, social_situation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.inlove_string);
        super.setIcon(constants.inlove_icon);
        super.setColor(constants.inlove_color);
    }
}