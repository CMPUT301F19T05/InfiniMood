package com.example.infinimood.model;

import android.location.Location;
import android.media.Image;

import java.util.Date;

/**
 * AngryMood.java
 * Subclass for Angry mood events
 */

public class AngryMood extends Mood {
    public AngryMood(String id,
                     Date date,
                     String reason,
                     Location location,
                     String socialSituation,
                     Image image) {
        super(id, date, reason, location, socialSituation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.ANGRY_STRING);
        super.setIcon(constants.ANGRY_ICON);
        super.setColor(constants.ANGRY_COLOR);
    }
}
