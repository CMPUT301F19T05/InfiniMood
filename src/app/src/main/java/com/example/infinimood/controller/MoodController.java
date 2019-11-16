package com.example.infinimood.controller;

import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;
import android.util.Log;

import com.example.infinimood.model.AfraidMood;
import com.example.infinimood.model.AngryMood;
import com.example.infinimood.model.CryingMood;
import com.example.infinimood.model.HappyMood;
import com.example.infinimood.model.InLoveMood;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.SadMood;
import com.example.infinimood.model.SleepyMood;

import java.util.Date;

/**
 *  MoodController.java
 *  Handles creation of moods
 */
public class MoodController {

    private static final String TAG = "FirebaseController";

    public MoodController() {

    }

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
