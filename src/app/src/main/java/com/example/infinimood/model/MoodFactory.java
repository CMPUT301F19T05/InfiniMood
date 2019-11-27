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

    public MoodFactory() {
    }

    public Mood createMood(String id, String userId, String mood, long moodDate, String moodReason, Location moodLocation, String moodSocialSituation, boolean hasImage) {
        Mood newMood;

        switch (mood) {
            case "Happy":
                newMood = new HappyMood(id, userId, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "Angry":
                newMood = new AngryMood(id, userId, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "Crying":
                newMood = new CryingMood(id, userId, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "In Love":
                newMood = new InLoveMood(id, userId, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "Sad":
                newMood = new SadMood(id, userId, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "Sleepy":
                newMood = new SleepyMood(id, userId, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "Afraid":
                newMood = new AfraidMood(id, userId, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            default:
                Log.e(TAG, "Default case in addEdit switch: " + mood);
                throw new IllegalArgumentException("Invalid mood in createMood");
        }
        return newMood;
    }

}
