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
    private Date date;
    private long time;
    private String reason = "";
    private Location location = null;
    private String socialSituation;
    private Bitmap image = null;

    private String mood = "";
    private String icon = "";
    private String color;

    public Mood(String id, String icon, String reason, String socialSituation) {
        this.id = id;
        this.icon = icon;
        this.reason = reason;
        this.socialSituation = socialSituation;
    }

    public Mood(String id,
                Date date,
                String reason,
                Location location,
                String socialSituation,
                Bitmap image)
    {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.location = location;
        this.socialSituation = socialSituation;
        this.image = image;
        this.time = date.getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void print() {
        Log.i("", "ID : " + this.id);
        Log.i("", "Mood : " + this.mood);
        Log.i("", "Social Situation : " + this.socialSituation);
        Log.i("", "Reason : " + this.reason);
        Log.i("", "Date : " + this.date.toString());
        Log.i("", "Emoji : " + this.icon);
    }

    //TODO: Implement these function once Firebase is working
    public void commitToDatabase() {

    }

}
