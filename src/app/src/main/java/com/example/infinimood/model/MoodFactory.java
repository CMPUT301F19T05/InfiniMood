package com.example.infinimood.model;

import android.graphics.Bitmap;
import android.location.Location;

import android.util.Log;

import java.util.Date;

/**
 * MoodFactory.java
 * Handles creation of moods
 */
public class MoodFactory {

    private static final String TAG = "MoodFactory";

    public Mood createMood(String id, String mood, Date moodDate, String moodReason, Location moodLocation, String moodSocialSituation, Bitmap moodImage) {
        Mood newMood;

        switch (mood) {
            case "Happy":
                newMood = new HappyMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "Angry":
                newMood = new AngryMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "Crying":
                newMood = new CryingMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "In Love":
                newMood = new InLoveMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "Sad":
                newMood = new SadMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "Sleepy":
                newMood = new SleepyMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            case "Afraid":
                newMood = new AfraidMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, moodImage);
                break;
            default:
                Log.e(TAG, "Default case in addEdit switch: " + mood);
                throw new IllegalArgumentException("Invalid mood in createMood");
        }
        return newMood;
    }

}
