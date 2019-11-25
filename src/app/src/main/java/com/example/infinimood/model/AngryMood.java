package com.example.infinimood.model;

import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;

import java.util.Date;

/**
 * AngryMood.java
 * Subclass for Angry mood events
 */

public class AngryMood extends Mood {
    public AngryMood(String id,
                     long date,
                     String reason,
                     Location location,
                     String socialSituation,
                     boolean hasImage) {
        super(id, date, reason, location, socialSituation, hasImage);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.ANGRY_STRING);
        super.setIcon(constants.ANGRY_ICON);
        super.setColor(constants.ANGRY_COLOR);
    }
}
