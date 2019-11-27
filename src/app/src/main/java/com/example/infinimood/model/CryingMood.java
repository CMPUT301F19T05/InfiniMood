package com.example.infinimood.model;

import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;

import java.util.Date;

/**
 *  CryingMood.java
 *  Subclass for Crying mood events
 */
public class CryingMood extends Mood {

    public CryingMood(String id,
                     long date,
                     String reason,
                     Location location,
                     String socialSituation,
                      boolean hasImage) {
        super(id, date, reason, location, socialSituation, hasImage);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.CRYING_STRING);
        super.setIcon(constants.CRYING_ICON);
        super.setColor(constants.CRYING_COLOR);
    }
}