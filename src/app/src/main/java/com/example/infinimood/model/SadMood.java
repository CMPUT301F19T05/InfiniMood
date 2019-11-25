package com.example.infinimood.model;

import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;

import java.util.Date;

/**
 *  SadMood.java
 *  Subclass for Sad mood events
 */

public class SadMood extends Mood {

    public SadMood(String id,
                     long date,
                     String reason,
                     Location location,
                     String socialSituation,
                   boolean hasImage) {
        super(id, date, reason, location, socialSituation, hasImage);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.SAD_STRING);
        super.setIcon(constants.SAD_ICON);
        super.setColor(constants.SAD_COLOR);
    }
}