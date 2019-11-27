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

    /**
     * MoodFactory
     * Simple constructor for MoodFactory
     */
    public MoodFactory() {
    }

    /**
     * createMood
     * Creates a mood with given mood information
     * @param id String - mood's unique ID
     * @param mood String - mood's string
     * @param moodDate long - mood's date
     * @param moodReason String - mood's reason
     * @param moodLocation Location - mood's location
     * @param moodSocialSituation String - mood's social situation
     * @param hasImage boolean - whether the mood has an image
     * @return Mood - The resulting mood
     */
    public Mood createMood(String id, String mood, long moodDate, String moodReason, Location moodLocation, String moodSocialSituation, boolean hasImage) {
        Mood newMood;

        switch (mood) {
            case "Happy":
                newMood = new HappyMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "Angry":
                newMood = new AngryMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "Crying":
                newMood = new CryingMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "In Love":
                newMood = new InLoveMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "Sad":
                newMood = new SadMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "Sleepy":
                newMood = new SleepyMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            case "Afraid":
                newMood = new AfraidMood(id, moodDate, moodReason, moodLocation, moodSocialSituation, hasImage);
                break;
            default:
                Log.e(TAG, "Default case in addEdit switch: " + mood);
                throw new IllegalArgumentException("Invalid mood in createMood");
        }
        return newMood;
    }

}
