package com.example.infinimood.model;

import android.location.Location;
import android.media.Image;

import java.util.Date;

/**
 *  HappyMood.java
 *  Subclass for Happy mood events
 */

public class HappyMood extends Mood {

    public HappyMood(String id,
                      Date date,
                      String reason,
                      Location location,
                      String socialSituation,
                      Image image)
    {
        super(id, date, reason, location, socialSituation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.HAPPY_STRING);
        super.setIcon(constants.HAPPY_ICON);
        super.setColor(constants.HAPPY_COLOR);
    }
}