package com.example.infinimood.model;

import android.graphics.Bitmap;
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
                     String socialSituation,
                      Bitmap image)
    {
        super(id, date, reason, location, socialSituation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.INLOVE_STRING);
        super.setIcon(constants.INLOVE_ICON);
        super.setColor(constants.INLOVE_COLOR);
    }
}