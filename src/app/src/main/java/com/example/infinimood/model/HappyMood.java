package com.example.infinimood.model;

import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;

import com.example.infinimood.R;

import java.util.Date;

/**
 *  HappyMood.java
 *  Subclass for Happy mood events
 */

public class HappyMood extends Mood {

    public HappyMood(String id,
                      long date,
                      String reason,
                      Location location,
                      String socialSituation,
                     Bitmap image)
    {
        super(id, date, reason, location, socialSituation, image);
        MoodConstants constants = new MoodConstants();
        super.setMood(constants.HAPPY_STRING);
        super.setIcon(constants.HAPPY_ICON);
        super.setColor(constants.HAPPY_COLOR);
    }
}