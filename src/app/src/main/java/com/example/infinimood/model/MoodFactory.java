package com.example.infinimood.model;

import android.location.Location;
import android.media.Image;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MoodFactory {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd - hh:mm a",
            Locale.getDefault()
    );

    public Mood createMoodFromFirebaseDocument(QueryDocumentSnapshot document) throws IllegalArgumentException {
        String mood = (String) document.get("mood");
        String id = (String) document.get("id");
        String date_string = (String) document.get("date");
        String reason = (String) document.get("reason");
        String location_string = (String) document.get("location");
        String social_situation = (String) document.get("social_situation");
        String image_string = (String) document.get("image");

        if (mood == null) {
            throw new IllegalArgumentException("Mood type cannot be null.");
        }

        if (id == null) {
            throw new IllegalArgumentException("Mood id cannot be null.");
        }

        Date date;
        if (date_string == null) {
            date = null;
        } else {
            try {
                date = dateFormat.parse(date_string);
            } catch (ParseException e) {
                date = null;
            }
        }

        // TODO: Location string to location
        Location location = null;

        // TODO: Image string to image
        Image image = null;

        Mood newMood;
        switch (mood) {
            case "Happy":
                newMood = new HappyMood(id, date, reason, location, social_situation, image);
                break;
            case "Angry":
                newMood = new AngryMood(id, date, reason, location, social_situation, image);
                break;
            case "Crying":
                newMood = new CryingMood(id, date, reason, location, social_situation, image);
                break;
            case "In Love":
                newMood = new InLoveMood(id, date, reason, location, social_situation, image);
                break;
            case "Sad":
                newMood = new SadMood(id, date, reason, location, social_situation, image);
                break;
            case "Sleepy":
                newMood = new SleepyMood(id, date, reason, location, social_situation, image);
                break;
            case "Afraid":
                newMood = new AfraidMood(id, date, reason, location, social_situation, image);
                break;
            default:
                throw new IllegalArgumentException("Mood type not recognized.");
        }

        return newMood;
    }
}
