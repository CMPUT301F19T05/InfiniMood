package com.example.infinimood.model;

import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;

import java.util.Date;

/**
 *  SleepyMood.java
 *  Subclass for Sleepy mood events
 */

public class SleepyMood extends Mood {

    public SleepyMood(String id,
                     long date,
                     String reason,
                     Location location,
                     String socialSituation,
                      Bitmap image)
    {
        super(id, date, reason, location, socialSituation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.SLEEPY_STRING);
        super.setIcon(constants.SLEEPY_ICON);
        super.setColor(constants.SLEEPY_COLOR);
    }
}