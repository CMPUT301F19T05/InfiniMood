package com.example.infinimood.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.util.Log;

import java.util.Date;

/**
 *  Mood.java
 *  Mood event superclass, contains all functionality for mood objects
 */

public abstract class Mood {

    private String id;
    private long dateTimestamp;
    private String reason = "";
    private Location location = null;
    private String socialSituation;
    private Bitmap image = null;

    private String mood = "";
    private String icon = "";
    private String color;

    public Mood(String id,
                long dateTimestamp,
                String reason,
                Location location,
                String socialSituation,
                Bitmap image)
    {
        this.id = id;
        this.dateTimestamp = dateTimestamp;
        this.reason = reason;
        this.location = location;
        this.socialSituation = socialSituation;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDate() {
        return dateTimestamp;
    }

    public void setDate(long dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(String socialSituation) {
        this.socialSituation = socialSituation;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void print() {
        Log.i("", "ID : " + this.id);
        Log.i("", "Mood : " + this.mood);
        Log.i("", "Social Situation : " + this.socialSituation);
        Log.i("", "Reason : " + this.reason);
        Log.i("", "Date : " + new Date(this.dateTimestamp).toString());
        Log.i("", "Emoji : " + this.icon);
    }
}
