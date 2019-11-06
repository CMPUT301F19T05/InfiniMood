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
                     String socialSituation,
                     Image image)
    {
        super(id, date, reason, location, socialSituation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.AFRAID_STRING);
        super.setIcon(constants.AFRAID_ICON);
        super.setColor(constants.AFRAID_COLOR);
    }
}